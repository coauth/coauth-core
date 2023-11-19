package dev.coauth.core.module.registry.producer;

import dev.coauth.core.module.messaging.MessageRegisterGenerateDto;
import dev.coauth.core.module.messaging.MessageVerificationGenerateDto;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.MutinyEmitter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;

@ApplicationScoped
public class MessageBrokerService {

    @Inject
    @Channel("totp-verify-generate")
    MutinyEmitter<MessageVerificationGenerateDto> totpVerifyGenerateEmitter;

    @Inject
    @Channel("reconfirm-verify-generate")
    MutinyEmitter<MessageVerificationGenerateDto> reconfirmVerifyGenerateEmitter;


    @Inject
    @Channel("totp-register-generate")
    MutinyEmitter<MessageRegisterGenerateDto> totpRegisterGenerateEmitter;


    public Uni<Void> emitTotpVerifyGenerate(MessageVerificationGenerateDto messageVerificationGenerateDto){
        return totpVerifyGenerateEmitter.send(messageVerificationGenerateDto);
    }

    public Uni<Void> emitReconfirmVerifyGenerate(MessageVerificationGenerateDto messageVerificationGenerateDto){
        return reconfirmVerifyGenerateEmitter.send(messageVerificationGenerateDto);
    }

    public Uni<Void> emitTotpRegisterGenerate(MessageRegisterGenerateDto messageVerificationGenerateDto){
        return totpRegisterGenerateEmitter.send(messageVerificationGenerateDto);
    }
}
