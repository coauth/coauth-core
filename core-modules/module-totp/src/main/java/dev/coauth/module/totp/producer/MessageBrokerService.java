package dev.coauth.module.totp.producer;

import dev.coauth.module.messaging.MessageRegisterStatusDto;
import dev.coauth.module.messaging.MessageVerificationStatusDto;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class MessageBrokerService {

    @RestClient
    ModuleTotpConnectionService moduleTotpConnectionService;

    public Uni<Void> emitTotpVerifyStatus(MessageVerificationStatusDto messageVerificationStatusDto){
        return moduleTotpConnectionService.emitTotpVerifyStatus(messageVerificationStatusDto);
    }

    public Uni<Void> emitTotpRegisterStatus(MessageRegisterStatusDto messageRegisterStatusDto){
        return moduleTotpConnectionService.emitTotpRegisterStatus(messageRegisterStatusDto);
    }
}
