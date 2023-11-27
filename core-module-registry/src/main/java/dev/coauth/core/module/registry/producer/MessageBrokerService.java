package dev.coauth.core.module.registry.producer;

import dev.coauth.core.module.messaging.MessageRegisterGenerateDto;
import dev.coauth.core.module.messaging.MessageVerificationGenerateDto;
import dev.coauth.core.module.messaging.ReconfirmMessageVerificationGenerateDto;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.MutinyEmitter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class MessageBrokerService {

    @RestClient
    ModuleTotpConnectionService moduleTotpConnectionService;

    @RestClient
    ModuleReconfirmConnectionService moduleReconfirmConnectionService;

    public Uni<Void> emitTotpVerifyGenerate(MessageVerificationGenerateDto messageVerificationGenerateDto){
        return moduleTotpConnectionService.emitTotpVerifyGenerate(messageVerificationGenerateDto);
    }

    public Uni<Void> emitReconfirmVerifyGenerate(ReconfirmMessageVerificationGenerateDto reconfirmMessageVerificationGenerateDto){
        return moduleReconfirmConnectionService.emitReconfirmVerifyGenerate(reconfirmMessageVerificationGenerateDto);
    }

    public Uni<Void> emitTotpRegisterGenerate(MessageRegisterGenerateDto messageVerificationGenerateDto){
        return moduleTotpConnectionService.emitTotpRegisterGenerate(messageVerificationGenerateDto);
    }
}
