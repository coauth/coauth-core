package dev.coauth.module.reconfirm.producer;

import dev.coauth.module.messaging.MessageVerificationStatusDto;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.MutinyEmitter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;

@ApplicationScoped
public class MessageBrokerService {

    @Inject
    @Channel("reconfirm-verify-status")
    MutinyEmitter<MessageVerificationStatusDto> reconfirmVerifyGenerateEmitter;


    public Uni<Void> emitReconfirmVerifyStatus(MessageVerificationStatusDto messageVerificationStatusDto){
        return reconfirmVerifyGenerateEmitter.send(messageVerificationStatusDto);
    }

}
