package dev.coauth.module.reconfirm.service;

import dev.coauth.module.messaging.MessageVerificationStatusDto;
import dev.coauth.module.messaging.ReconfirmMessageVerificationGenerateDto;
import dev.coauth.module.reconfirm.cache.ReconfirmVerificationCacheDto;
import dev.coauth.module.reconfirm.dto.VerificationAuthenticateRequestDto;
import dev.coauth.module.reconfirm.dto.VerificationViewRequestDto;
import dev.coauth.module.reconfirm.entity.ModuleReconfirmMstrEntity;
import dev.coauth.module.reconfirm.exception.NonFatalException;
import dev.coauth.module.reconfirm.producer.MessageBrokerService;
import dev.coauth.module.reconfirm.repository.ModuleReconfirmRepository;
import dev.coauth.module.reconfirm.utils.ApplicationConstants;
import io.quarkus.infinispan.client.Remote;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.infinispan.client.hotrod.RemoteCache;

@ApplicationScoped
public class ReconfirmService {

    @Inject
    ModuleReconfirmRepository moduleReconfirmRepository;

    @Inject
    MessageBrokerService brokerService;

    @Inject
    @Remote("reconfirm_verify")
    RemoteCache<String, ReconfirmVerificationCacheDto> reconfirmVerificationCacheDtoRemoteCache;

    public Uni<ModuleReconfirmMstrEntity> getDetails(int appId, String userId) {
        ModuleReconfirmMstrEntity totpMstrEntity = new ModuleReconfirmMstrEntity();
        totpMstrEntity.setAppId(appId);
        totpMstrEntity.setUserId(userId);
        return moduleReconfirmRepository.get(totpMstrEntity);
    }

    public Uni<Void> generateAuthVerification(ReconfirmMessageVerificationGenerateDto reconfirmMessageVerificationGenerateDto) {
        return getDetails(reconfirmMessageVerificationGenerateDto.getAppId(), reconfirmMessageVerificationGenerateDto.getUserId())
                .onItem().transformToUni(entity -> {
                    if (entity == null) {

                        return Uni.createFrom().item(reconfirmMessageVerificationGenerateDto.getCode())
                                .onItem()
                                .invoke(uuid -> {
                                    ReconfirmVerificationCacheDto reconfirmVerificationCacheDto = new ReconfirmVerificationCacheDto();
                                    reconfirmVerificationCacheDto.setAppId(reconfirmMessageVerificationGenerateDto.getAppId());
                                    reconfirmVerificationCacheDto.setUserId(reconfirmMessageVerificationGenerateDto.getUserId());
                                    reconfirmVerificationCacheDto.setNoOfAttempts(0);
                                    reconfirmVerificationCacheDto.setStatus(ApplicationConstants.STATUS_PENDING);
                                    reconfirmVerificationCacheDto.setCode(reconfirmMessageVerificationGenerateDto.getCode());
                                    reconfirmVerificationCacheDto.setReconfirmFields(ReconfirmVerificationCacheDto.ReconfirmFields.builder().hintMessage("").isVisible(true).reconfirmValue(reconfirmMessageVerificationGenerateDto.getReconfirmFields().getReconfirmValue()).build());
                                    reconfirmVerificationCacheDtoRemoteCache.putAsync(uuid, reconfirmVerificationCacheDto);
                                })
                                .onItem()
                                .transformToUni(entity1 -> Uni.createFrom().voidItem());
                    } else {
                        //do nothing since Reconfirm module has been disabled for your ID
                        return Uni.createFrom().voidItem();

                    }
                });
    }

    public Uni<String> viewAuthVerification(VerificationViewRequestDto verificationViewRequestDto) {
        return Uni.createFrom().item(reconfirmVerificationCacheDtoRemoteCache.get(verificationViewRequestDto.getAppCode()))
                .onItem().transformToUni(reconfirmVerificationCacheDto -> {
                    if (reconfirmVerificationCacheDto == null) {
                        return Uni.createFrom().failure(new NonFatalException(1000, "Record not found"));
                    } else {
                        if(reconfirmVerificationCacheDto.getReconfirmFields().getIsVisible()) {
                            return Uni.createFrom().item("Re-type the word: <span class=\"text-xl font-bold\">"+reconfirmVerificationCacheDto.getReconfirmFields().getReconfirmValue()+"</span> in the input box below");
                        }else {
                            return Uni.createFrom().item(reconfirmVerificationCacheDto.getReconfirmFields().getHintMessage());
                        }
                    }
                });
    }

    public Uni<String> authenticateVerification(VerificationAuthenticateRequestDto verificationAuthenticateRequestDto) {
        return Uni.createFrom().item(reconfirmVerificationCacheDtoRemoteCache.get(verificationAuthenticateRequestDto.getAppCode()))
                .onItem().transformToUni(reconfirmVerificationCacheDto -> {
                    if (reconfirmVerificationCacheDto == null) {
                        return Uni.createFrom().failure(new NonFatalException(1000, "Record not found"));
                    } else {
                        boolean isValid = verificationAuthenticateRequestDto.getReconfirmInputText().equals(reconfirmVerificationCacheDto.getReconfirmFields().getReconfirmValue());

                        if (isValid) {
                            return Uni.createFrom().item(reconfirmVerificationCacheDto.getCode()).onItem()
                                    .transformToUni(entity1 -> {
                                        MessageVerificationStatusDto messageVerificationStatusDto = new MessageVerificationStatusDto();
                                        messageVerificationStatusDto.setAppId(reconfirmVerificationCacheDto.getAppId());
                                        messageVerificationStatusDto.setCode(reconfirmVerificationCacheDto.getCode());
                                        messageVerificationStatusDto.setUserId(reconfirmVerificationCacheDto.getUserId());
                                        messageVerificationStatusDto.setStatus(ApplicationConstants.STATUS_SUCCESS);
                                        return brokerService.emitReconfirmVerifyStatus(messageVerificationStatusDto).onItem()
                                                .transform(entity2 -> reconfirmVerificationCacheDto.getCode());
                                    })
                                    .invoke(unUsed ->
                                            reconfirmVerificationCacheDtoRemoteCache.remove(reconfirmVerificationCacheDto.getCode())
                                    );
                        } else {
                            reconfirmVerificationCacheDto.setNoOfAttempts(reconfirmVerificationCacheDto.getNoOfAttempts() + 1);
                            reconfirmVerificationCacheDtoRemoteCache.replace(reconfirmVerificationCacheDto.getCode(), reconfirmVerificationCacheDto);
                            return Uni.createFrom().failure(new NonFatalException(1003, "Invalid input value entered"));
                        }
                    }
                });

        }


}
