package dev.coauth.module.totp.consumer;

import dev.coauth.module.messaging.MessageRegisterGenerateDto;
import dev.coauth.module.messaging.MessageVerificationGenerateDto;
import dev.coauth.module.totp.dto.RegisterGenerateRequestDto;
import dev.coauth.module.totp.dto.VerificationGenerateRequestDto;
import dev.coauth.module.totp.service.TotpService;
import io.quarkus.runtime.Startup;
import io.smallrye.common.annotation.NonBlocking;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@Path("/coauth/module/internal/totp")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GenerateConsumer {



    @Inject
    TotpService totpService;

    @POST
    @Path("/totp-verify-generate")
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

    @POST
    @Path("/totp-register-generate")
    public Uni<Void> consume(MessageRegisterGenerateDto messageRegisterGenerateDto) {
        RegisterGenerateRequestDto registerGenerateRequestDto = new RegisterGenerateRequestDto();
        registerGenerateRequestDto.setAppId(messageRegisterGenerateDto.getAppId());
        registerGenerateRequestDto.setUserId(messageRegisterGenerateDto.getUserId());
        registerGenerateRequestDto.setCodeChallenge(messageRegisterGenerateDto.getCodeChallenge());
        registerGenerateRequestDto.setCode(messageRegisterGenerateDto.getCode());
        return totpService.generateSecret(registerGenerateRequestDto)
                .onItem().transformToUni(entity -> {
                    return Uni.createFrom().voidItem();
                })
                //return void on failure
                .onFailure().recoverWithItem(entity -> {
                    return null;
                });
    }
}
