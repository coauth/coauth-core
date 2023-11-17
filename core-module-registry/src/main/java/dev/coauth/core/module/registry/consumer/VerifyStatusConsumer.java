package dev.coauth.core.module.registry.consumer;

import dev.coauth.core.module.messaging.MessageVerificationStatusDto;
import dev.coauth.core.module.registry.service.CoreModuleRegistryService;
import io.smallrye.common.annotation.NonBlocking;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class VerifyStatusConsumer {

    @Inject
    CoreModuleRegistryService coreModuleRegistryService;

    @Incoming("totp-verify-status")
    @NonBlocking
    public Uni<Void> consumeTotpVerifyStatus(MessageVerificationStatusDto messageVerificationStatusDto) {
            return coreModuleRegistryService.updateCache(messageVerificationStatusDto.getCode(), messageVerificationStatusDto.getAppId(),
                    messageVerificationStatusDto.getUserId(), messageVerificationStatusDto.getStatus());
    }

    @Incoming("reconfirm-verify-status")
    @NonBlocking
    public Uni<Void> consumeReconfirmVerifyStatus(MessageVerificationStatusDto messageVerificationStatusDto) {
        return coreModuleRegistryService.updateCache(messageVerificationStatusDto.getCode(), messageVerificationStatusDto.getAppId(),
                messageVerificationStatusDto.getUserId(), messageVerificationStatusDto.getStatus());
    }
}
