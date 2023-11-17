package dev.coauth.module.totp.consumer;

import dev.coauth.module.messaging.MessageVerificationGenerateDto;
import dev.coauth.module.totp.dto.VerificationGenerateRequestDto;
import dev.coauth.module.totp.service.TotpService;
import io.smallrye.common.annotation.NonBlocking;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class GenerateConsumer {

    @Inject
    TotpService totpService;

    @Incoming("totp-verify-generate")
    @NonBlocking
    public Uni<Void> consume(MessageVerificationGenerateDto messageVerificationGenerateDto) {
        VerificationGenerateRequestDto verificationGenerateRequestDto = new VerificationGenerateRequestDto();
        verificationGenerateRequestDto.setAppId(messageVerificationGenerateDto.getAppId());
        verificationGenerateRequestDto.setUserId(messageVerificationGenerateDto.getUserId());
        verificationGenerateRequestDto.setCodeChallenge(messageVerificationGenerateDto.getCodeChallenge());
        verificationGenerateRequestDto.setCode(messageVerificationGenerateDto.getCode());
        return totpService.generateAuthVerification(verificationGenerateRequestDto)
                .onItem().transformToUni(entity -> {
                    return Uni.createFrom().voidItem();
                })
                //return void on failure
                .onFailure().recoverWithItem(entity -> {
                    return null;
                });
    }
}
