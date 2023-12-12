package dev.coauth.module.reconfirm.producer;

import dev.coauth.module.messaging.MessageVerificationStatusDto;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.MutinyEmitter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class MessageBrokerService {


    @RestClient
    ModuleReconfirmConnectionService moduleReconfirmConnectionService;

    public Uni<Void> emitReconfirmVerifyStatus(MessageVerificationStatusDto messageVerificationStatusDto){
        return moduleReconfirmConnectionService.emitReconfirmVerifyStatus(messageVerificationStatusDto);
    }

}
