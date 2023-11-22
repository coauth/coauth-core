package dev.coauth.core.module.registry.consumer;

import dev.coauth.core.module.messaging.MessageRegisterGenerateDto;
import dev.coauth.core.module.messaging.MessageRegisterStatusDto;
import dev.coauth.core.module.messaging.MessageVerificationStatusDto;
import dev.coauth.core.module.registry.service.CoreModuleRegistryRegisterService;
import dev.coauth.core.module.registry.service.CoreModuleRegistryVerifyService;
import io.smallrye.common.annotation.NonBlocking;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class VerifyStatusConsumer {

    @Inject
    CoreModuleRegistryVerifyService coreModuleRegistryVerifyService;

    @Inject
    CoreModuleRegistryRegisterService coreModuleRegistryRegisterService;

    @Incoming("totp-verify-status")
    @NonBlocking
    public Uni<Void> consumeTotpVerifyStatus(MessageVerificationStatusDto messageVerificationStatusDto) {
            return coreModuleRegistryVerifyService.updateCache(messageVerificationStatusDto.getCode(), messageVerificationStatusDto.getAppId(),
                    messageVerificationStatusDto.getUserId(), messageVerificationStatusDto.getStatus());
    }

    @Incoming("totp-register-status")
    @NonBlocking
    public Uni<Void> consumeTotpRegisterStatus(MessageRegisterStatusDto messageRegisterStatusDto) {
        return coreModuleRegistryRegisterService.updateCache(messageRegisterStatusDto.getCode(), messageRegisterStatusDto.getAppId(),
                messageRegisterStatusDto.getUserId(), messageRegisterStatusDto.getStatus());
    }

    @Incoming("reconfirm-verify-status")
    @NonBlocking
    public Uni<Void> consumeReconfirmVerifyStatus(MessageVerificationStatusDto messageVerificationStatusDto) {
        return coreModuleRegistryVerifyService.updateCache(messageVerificationStatusDto.getCode(), messageVerificationStatusDto.getAppId(),
                messageVerificationStatusDto.getUserId(), messageVerificationStatusDto.getStatus());
    }

}
