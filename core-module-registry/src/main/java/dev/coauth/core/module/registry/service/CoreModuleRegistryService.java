package dev.coauth.core.module.registry.service;

import dev.coauth.core.exception.NonFatalException;
import dev.coauth.core.module.registry.dto.GenerateRequestDto;
import dev.coauth.core.module.registry.entity.CoreModuleRegistryMstrEntity;
import dev.coauth.core.module.registry.repository.CoreModuleRegistryMstrRepository;
import dev.coauth.core.module.totp.cache.AvailableVerificationCacheDto;
import dev.coauth.core.module.totp.cache.ReconfirmVerificationCacheDto;
import dev.coauth.core.utils.ApplicationConstants;
import io.quarkus.infinispan.client.Remote;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.infinispan.client.hotrod.RemoteCache;

import java.util.*;

@ApplicationScoped
public class CoreModuleRegistryService {

    @Remote("available_modules")
    RemoteCache<String, AvailableVerificationCacheDto> availableVerificationCacheDtoRemoteCache;

    @Inject
    CoreModuleRegistryMstrRepository coreModuleRegistryMstrRepository;

    @Inject
    MessageBrokerService messageBrokerService;

    public Uni<String> validateModuleRequest(GenerateRequestDto generateRequestDto) {

        Set<String> requestedModules = new LinkedHashSet<>(Arrays.asList(generateRequestDto.getModules().split(",")));

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
                                    .transformToUni(uuid -> sendMessage(uuid, generateRequestDto.getAppDetails().getAppId(), generateRequestDto.getUserId(), ApplicationConstants.MODULE_RECONFIRM)
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
                                        return sendMessage(uuid, generateRequestDto.getAppDetails().getAppId(), generateRequestDto.getUserId(), ApplicationConstants.MODULE_RECONFIRM)
                                                .onItem()
                                                .transformToUni(
                                                        voidUni -> putAvailableVerificationInCache(uuid, generateRequestDto.getAppDetails().getAppId(),
                                                                generateRequestDto.getUserId(), ApplicationConstants.MODULE_RECONFIRM,
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
                .build();
        return Uni.createFrom().voidItem().invoke(voidItem -> availableVerificationCacheDtoRemoteCache.putAsync(uuid, availableVerificationCacheDto));
    }

    public Uni<Void> sendMessage(String uuid, int appId, String userId, String serviceName) {
        return Uni.createFrom().voidItem().onItem().transformToUni(voidItem -> {
            ReconfirmVerificationCacheDto reconfirmVerificationCacheDto = ReconfirmVerificationCacheDto.builder()
                    .code(uuid).userId(userId)
                    .appId(appId).build();
            if (serviceName.equals(ApplicationConstants.MODULE_RECONFIRM))
                return messageBrokerService.emitReconfirmVerifyGenerate(reconfirmVerificationCacheDto);
            else if (serviceName.equals(ApplicationConstants.MODULE_TOTP)) {
                return messageBrokerService.emitTotpVerifyGenerate(reconfirmVerificationCacheDto);
            }
            return Uni.createFrom().nullItem();
        });
    }


}
