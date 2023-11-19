package dev.coauth.core.module.registry.service;

import dev.coauth.core.exception.NonFatalException;
import dev.coauth.core.module.messaging.MessageRegisterGenerateDto;
import dev.coauth.core.module.registry.dto.RegisterGenerateRequestDto;
import dev.coauth.core.module.registry.dto.RegisterStatusRequestDto;
import dev.coauth.core.module.registry.dto.RegisterViewResponseDto;
import dev.coauth.core.module.registry.entity.CoreModuleRegistryMstrEntity;
import dev.coauth.core.module.registry.producer.MessageBrokerService;
import dev.coauth.core.module.registry.repository.CoreModuleRegistryMstrRepository;
import dev.coauth.core.module.totp.cache.AvailableRegisterCacheDto;
import dev.coauth.core.utils.ApplicationConstants;
import dev.coauth.core.utils.CryptoAlgoUtil;
import io.quarkus.infinispan.client.Remote;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.infinispan.client.hotrod.RemoteCache;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
public class CoreModuleRegistryRegisterService {

    @Remote("available_modules_register")
    RemoteCache<String, AvailableRegisterCacheDto> availableRegisterCacheDtoRemoteCache;

    @Inject
    CoreModuleRegistryMstrRepository coreModuleRegistryMstrRepository;

    @Inject
    MessageBrokerService messageBrokerService;

    public Uni<String> validateModuleRequest(RegisterGenerateRequestDto registerGenerateRequestDto) {

        Set<String> requestedModules = Stream.of(registerGenerateRequestDto.getModule())
                .map(String::trim)
                .filter(ApplicationConstants.AVAILABLE_MODULES::contains)
                .collect(Collectors.toSet());

        if (requestedModules.isEmpty()) {
            return Uni.createFrom().failure(new NonFatalException(1100, "Your module request is invalid."));
        }


        CoreModuleRegistryMstrEntity inputBean = CoreModuleRegistryMstrEntity.builder()
                .userId(registerGenerateRequestDto.getUserId())
                .appId(registerGenerateRequestDto.getAppDetails().getAppId()).build();

        return coreModuleRegistryMstrRepository.getRegisteredModule(inputBean)
                .onItem().transformToUni(coreModuleRegistryMstrEntities -> {
                    if (coreModuleRegistryMstrEntities.isEmpty()) {
                        return Uni.createFrom().item(UUID.randomUUID().toString())
                                .onItem()
                                .transformToUni(uuid -> sendMessage(uuid, registerGenerateRequestDto.getAppDetails().getAppId(),
                                        registerGenerateRequestDto.getUserId(),
                                        registerGenerateRequestDto.getModule())
                                        .onItem()
                                        .transformToUni(
                                                voidUni -> putAvailableVerificationInCache(uuid, registerGenerateRequestDto.getAppDetails().getAppId(),
                                                        registerGenerateRequestDto.getUserId(), registerGenerateRequestDto.getModule(),
                                                        registerGenerateRequestDto.getModule(),
                                                        registerGenerateRequestDto.getCodeChallenge()).onItem().transformToUni(voidItem -> Uni.createFrom().item(uuid)))
                                );
                    } else {
                        if (coreModuleRegistryMstrEntities.size() > 1) {
                            return Uni.createFrom().failure(new NonFatalException(1100, "Something went wrong (Found more than one registration). Contact administrator"));
                        } else if (coreModuleRegistryMstrEntities.get(0).getStatus().equals(ApplicationConstants.STATUS_ACTIVE)) {
                            return Uni.createFrom().failure(new NonFatalException(1100, "You have already registered for this request"));
                        } else if (coreModuleRegistryMstrEntities.get(0).getStatus().equals(ApplicationConstants.STATUS_DISABLED)) {
                            return Uni.createFrom().failure(new NonFatalException(1100, "Your account is disabled for this service"));
                        } else {
                            return Uni.createFrom().failure(new NonFatalException(1100, "Something went wrong. Contact administrator"));
                        }
                    }
                });
    }

    public Uni<Void> putAvailableVerificationInCache(String uuid, int appId, String userId, String availableModules, String currentModule, String codeChallenge) {
        AvailableRegisterCacheDto availableRegisterCacheDto = AvailableRegisterCacheDto.builder()
                .appId(appId)
                .userId(userId)
                .availableModules(availableModules)
                .currentModule(currentModule)
                .codeChallenge(codeChallenge)
                .status(ApplicationConstants.STATUS_PENDING)
                .build();
        return Uni.createFrom().voidItem().invoke(voidItem -> availableRegisterCacheDtoRemoteCache.putAsync(uuid, availableRegisterCacheDto));
    }

    public Uni<Void> sendMessage(String uuid, int appId, String userId, String serviceName) {
        System.out.println("Sending message for " + serviceName);
        return Uni.createFrom().voidItem().onItem().transformToUni(voidItem -> {
            MessageRegisterGenerateDto messageRegisterGenerateDto = MessageRegisterGenerateDto.builder()
                    .code(uuid).userId(userId)
                    .appId(appId).build();
            if (serviceName.equals(ApplicationConstants.MODULE_TOTP)) {
                return messageBrokerService.emitTotpRegisterGenerate(messageRegisterGenerateDto);
            }
            return Uni.createFrom().nullItem();
        });
    }


    public Uni<Void> updateCache(String code, int appId, String userId, String status) {
        CoreModuleRegistryMstrEntity coreModuleRegistryMstrEntity = new CoreModuleRegistryMstrEntity();
        coreModuleRegistryMstrEntity.setAppId(appId);
        coreModuleRegistryMstrEntity.setUserId(userId);
        coreModuleRegistryMstrEntity.setStatus(ApplicationConstants.STATUS_ACTIVE);
        coreModuleRegistryMstrEntity.setRegId(UUID.randomUUID().toString());
        coreModuleRegistryMstrEntity.setServiceName(ApplicationConstants.MODULE_TOTP);
        return coreModuleRegistryMstrRepository.save(coreModuleRegistryMstrEntity)
                .invoke(entity -> {
                    AvailableRegisterCacheDto availableRegisterCacheDto = availableRegisterCacheDtoRemoteCache.get(code);
                    if (availableRegisterCacheDto.getAppId() == appId && availableRegisterCacheDto.getUserId().equals(userId)) {
                        availableRegisterCacheDto.setStatus(status);
                        availableRegisterCacheDtoRemoteCache.putAsync(code, availableRegisterCacheDto);
                    }
                }).onItem().transformToUni(entity -> Uni.createFrom().voidItem());
    }

    public Uni<String> validateVerification(RegisterStatusRequestDto registerStatusRequestDto) {
        return Uni.createFrom().item(availableRegisterCacheDtoRemoteCache.get(registerStatusRequestDto.getCode()))
                .onItem().transformToUni(availableRegisterCacheDto -> {
                    if (availableRegisterCacheDto == null) {
                        return Uni.createFrom().failure(new NonFatalException(1000, "Record not found in cache"));
                    } else {
                        return CryptoAlgoUtil.calculateSHA256(registerStatusRequestDto.getCodeVerifier())
                                .onItem().transformToUni(sha256 -> {
                                    if (sha256.equals(availableRegisterCacheDto.getCodeChallenge())) {
                                        System.out.println("Status" + availableRegisterCacheDto.getStatus());
                                        if (!availableRegisterCacheDto.getStatus().equals(ApplicationConstants.STATUS_SUCCESS)) {
                                            return Uni.createFrom().failure(new NonFatalException(1002, "Verification not completed"));
                                        } else {
                                            return Uni.createFrom().item(availableRegisterCacheDto.getCode()).onItem()
                                                    .transformToUni(uuid -> Uni.createFrom().item(availableRegisterCacheDto.getStatus())
                                                    )
                                                    .invoke(entity ->
                                                            availableRegisterCacheDtoRemoteCache.remove(availableRegisterCacheDto.getCode())
                                                    );
                                        }
                                    } else {
                                        return Uni.createFrom().failure(new NonFatalException(1002, "Invalid code verifier"));
                                    }
                                });
                    }
                });
    }


    public Uni<RegisterViewResponseDto> getViewDetails(String code) {
        return Uni.createFrom().item(availableRegisterCacheDtoRemoteCache.get(code))
                .onItem().transformToUni(availableRegisterCacheDto -> {
                    if (availableRegisterCacheDto == null) {
                        return Uni.createFrom().failure(new NonFatalException(1000, "Invalid request"));
                    } else {
                        return Uni.createFrom().item(RegisterViewResponseDto.builder()
                                .code(code)
                                .module(availableRegisterCacheDto.getCurrentModule())
                                .build());
                    }
                });
    }


}
