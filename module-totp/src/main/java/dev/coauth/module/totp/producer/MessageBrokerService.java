package dev.coauth.module.totp.producer;

import dev.coauth.module.messaging.MessageRegisterStatusDto;
import dev.coauth.module.messaging.MessageVerificationStatusDto;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.MutinyEmitter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;

@ApplicationScoped
public class MessageBrokerService {

    @Inject
    @Channel("totp-verify-status")
    MutinyEmitter<MessageVerificationStatusDto> totpVerifyGenerateEmitter;

    @Inject
    @Channel("totp-register-status")
    MutinyEmitter<MessageRegisterStatusDto> totpRegiserGenerateEmitter;

    public Uni<Void> emitTotpVerifyStatus(MessageVerificationStatusDto messageVerificationStatusDto){
        return totpVerifyGenerateEmitter.send(messageVerificationStatusDto);
    }

    public Uni<Void> emitTotpRegisterStatus(MessageRegisterStatusDto messageRegisterStatusDto){
        return totpRegiserGenerateEmitter.send(messageRegisterStatusDto);
    }
}
