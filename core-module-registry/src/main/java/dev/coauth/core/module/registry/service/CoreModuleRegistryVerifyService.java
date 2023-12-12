package dev.coauth.core.module.registry.service;

import dev.coauth.core.exception.NonFatalException;
import dev.coauth.core.module.messaging.ReconfirmMessageVerificationGenerateDto;
import dev.coauth.core.module.registry.dto.VerifyGenerateRequestDto;
import dev.coauth.core.module.registry.dto.VerifyStatusRequestDto;
import dev.coauth.core.module.registry.dto.VerifyViewResponseDto;
import dev.coauth.core.module.registry.entity.CoreModuleRegistryMstrEntity;
import dev.coauth.core.module.registry.repository.CoreModuleRegistryMstrRepository;
import dev.coauth.core.module.registry.producer.MessageBrokerService;
import dev.coauth.core.module.totp.cache.AvailableVerificationCacheDto;
import dev.coauth.core.module.messaging.MessageVerificationGenerateDto;
import dev.coauth.core.utils.ApplicationConstants;
import dev.coauth.core.utils.CryptoAlgoUtil;
import io.quarkus.infinispan.client.Remote;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.infinispan.client.hotrod.RemoteCache;

import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class CoreModuleRegistryVerifyService {

    @Inject
    @Remote("available_modules_verify")
    RemoteCache<String, AvailableVerificationCacheDto> availableVerificationCacheDtoRemoteCache;

    @Inject
    CoreModuleRegistryMstrRepository coreModuleRegistryMstrRepository;

    @Inject
    MessageBrokerService messageBrokerService;

    public Uni<String> validateModuleRequest(VerifyGenerateRequestDto generateRequestDto) {

        Set<String> requestedModules = Arrays.stream(generateRequestDto.getModules().split(","))
                .map(String::trim)
                .filter(ApplicationConstants.AVAILABLE_MODULES::contains)
                .collect(Collectors.toSet());
        if(requestedModules.isEmpty()){
            return Uni.createFrom().failure(new NonFatalException(1100, "Invalid Modules specified in request"));
        }

        CoreModuleRegistryMstrEntity inputBean = CoreModuleRegistryMstrEntity.builder()
                .status(ApplicationConstants.STATUS_ACTIVE)
                .userId(generateRequestDto.getUserId())
                .appId(generateRequestDto.getAppDetails().getAppId()).build();

        return coreModuleRegistryMstrRepository.getAvailableModules(inputBean)
                .onItem().transformToUni(coreModuleRegistryMstrEntities -> {
                    if (coreModuleRegistryMstrEntities.isEmpty()) {
                        if (requestedModules.contains(ApplicationConstants.MODULE_RECONFIRM)) {
                            return Uni.createFrom().item(java.util.UUID.randomUUID().toString())
                                    .onItem()
                                    .transformToUni(uuid -> sendMessage(uuid, generateRequestDto.getAppDetails().getAppId(),
                                            generateRequestDto.getUserId(),
                                            ApplicationConstants.MODULE_RECONFIRM,generateRequestDto.getReConfirmText())
                                            .onItem()
                                            .transformToUni(
                                                    voidUni -> putAvailableVerificationInCache(uuid, generateRequestDto.getAppDetails().getAppId(),
                                                            generateRequestDto.getUserId(), ApplicationConstants.MODULE_RECONFIRM,
                                                            ApplicationConstants.MODULE_RECONFIRM,
                                                            generateRequestDto.getCodeChallenge()).onItem().transformToUni(voidItem -> Uni.createFrom().item(uuid)))
                                    );

                        } else {
                            return Uni.createFrom().failure(new NonFatalException(1100, "Invalid Module Request / Module Not Available for the given user"));
                        }
                    } else {
                        coreModuleRegistryMstrEntities.forEach(coreModuleRegistryMstrEntity -> {
                            if (requestedModules.contains(coreModuleRegistryMstrEntity.getServiceName()) && coreModuleRegistryMstrEntity.getStatus().equals(ApplicationConstants.STATUS_DISABLED)) {
                                requestedModules.remove(coreModuleRegistryMstrEntity.getServiceName());
                            }
                        });

                        if (requestedModules.isEmpty()) {
                            return Uni.createFrom().failure(new NonFatalException(1100, "Invalid Module Request / Module Not Available for the given user"));
                        } else {
                            return Uni.createFrom().item(java.util.UUID.randomUUID().toString())
                                    .onItem()
                                    .transformToUni(uuid -> {
                                        List<String> availableModules = new ArrayList<>(requestedModules);
                                        String currentModule = availableModules.get(0);
                                        return sendMessage(uuid, generateRequestDto.getAppDetails().getAppId(), generateRequestDto.getUserId(), currentModule,generateRequestDto.getReConfirmText())
                                                .onItem()
                                                .transformToUni(
                                                        voidUni -> putAvailableVerificationInCache(uuid, generateRequestDto.getAppDetails().getAppId(),
                                                                generateRequestDto.getUserId(), String.join(",", requestedModules),
                                                                currentModule,
                                                                generateRequestDto.getCodeChallenge()).onItem().transformToUni(voidItem -> Uni.createFrom().item(uuid)));

                                    });
                        }
                    }
                });
    }

    public Uni<Void> putAvailableVerificationInCache(String uuid, int appId, String userId, String availableModules, String currentModule, String codeChallenge) {
        AvailableVerificationCacheDto availableVerificationCacheDto = AvailableVerificationCacheDto.builder()
                .appId(appId)
                .userId(userId)
                .availableModules(availableModules)
                .currentModule(currentModule)
                .codeChallenge(codeChallenge)
                .status(ApplicationConstants.STATUS_PENDING)
                .build();
        return Uni.createFrom().voidItem().invoke(voidItem -> availableVerificationCacheDtoRemoteCache.putAsync(uuid, availableVerificationCacheDto));
    }

    public Uni<Void> sendMessage(String uuid, int appId, String userId, String serviceName,String reconfirmValue) {
        return Uni.createFrom().voidItem().onItem().transformToUni(voidItem -> {
            MessageVerificationGenerateDto messageVerificationGenerateDto = MessageVerificationGenerateDto.builder()
                    .code(uuid).userId(userId)
                    .appId(appId).build();
            if (serviceName.equals(ApplicationConstants.MODULE_RECONFIRM)) {
                ReconfirmMessageVerificationGenerateDto reconfirmMessageVerificationGenerateDto=ReconfirmMessageVerificationGenerateDto.builder()
                        .code(uuid).userId(userId)
                        .appId(appId).reconfirmFields(ReconfirmMessageVerificationGenerateDto.ReconfirmFields.builder()
                                .reconfirmValue(reconfirmValue)
                                .isVisible(true)
                                .hintMessage("")
                                .build())
                        .build();
                return messageBrokerService.emitReconfirmVerifyGenerate(reconfirmMessageVerificationGenerateDto);
            }else if (serviceName.equals(ApplicationConstants.MODULE_TOTP)) {
                return messageBrokerService.emitTotpVerifyGenerate(messageVerificationGenerateDto);
            }
            return Uni.createFrom().nullItem();
        });
    }


    public Uni<Void> updateCache(String code, int appId,String userId, String status){
        return Uni.createFrom().voidItem().invoke(voidItem -> {
            AvailableVerificationCacheDto availableVerificationCacheDto = availableVerificationCacheDtoRemoteCache.get(code);
            if(availableVerificationCacheDto.getAppId()==appId && availableVerificationCacheDto.getUserId().equals(userId)) {
                availableVerificationCacheDto.setStatus(status);
                availableVerificationCacheDtoRemoteCache.putAsync(code, availableVerificationCacheDto);
            }
        });
    }

    public Uni<String> validateVerification(VerifyStatusRequestDto verifyStatusRequestDto) {
        return Uni.createFrom().item(availableVerificationCacheDtoRemoteCache.get(verifyStatusRequestDto.getCode()))
                .onItem().transformToUni(availableVerificationCacheDto -> {
                    if (availableVerificationCacheDto == null) {
                        return Uni.createFrom().failure(new NonFatalException(1000, "Record not found in cache"));
                    } else {
                        return CryptoAlgoUtil.calculateSHA256(verifyStatusRequestDto.getCodeVerifier())
                                .onItem().transformToUni(sha256 -> {
                                    if (sha256.equals(availableVerificationCacheDto.getCodeChallenge())) {
                                        if (!availableVerificationCacheDto.getStatus().equals(ApplicationConstants.STATUS_SUCCESS)) {
                                            return Uni.createFrom().failure(new NonFatalException(1002, "Verification not completed"));
                                        } else {
                                            return Uni.createFrom().item(availableVerificationCacheDto.getCode()).onItem()
                                                    .transformToUni(uuid -> Uni.createFrom().item(availableVerificationCacheDto.getStatus())
                                                    ).invoke(entity ->
                                                            availableVerificationCacheDtoRemoteCache.remove(availableVerificationCacheDto.getCode())
                                                    );
                                        }
                                    } else {
                                        return Uni.createFrom().failure(new NonFatalException(1002, "Invalid code verifier"));
                                    }
                                });
                    }
                });
    }

    public Uni<VerifyViewResponseDto> getViewDetails(String code) {
        return Uni.createFrom().item(availableVerificationCacheDtoRemoteCache.get(code))
                .onItem().transformToUni(availableVerificationCacheDto -> {
                    if (availableVerificationCacheDto == null) {
                        return Uni.createFrom().failure(new NonFatalException(1000, "Invalid request"));
                    } else {
                        return Uni.createFrom().item(VerifyViewResponseDto.builder()
                                .code(availableVerificationCacheDto.getCode())
                                .availableModules(availableVerificationCacheDto.getAvailableModules())
                                .currentModule(availableVerificationCacheDto.getCurrentModule())
                                .build());
                    }
                });
    }


}
