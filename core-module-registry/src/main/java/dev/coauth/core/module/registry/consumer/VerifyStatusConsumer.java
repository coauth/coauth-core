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
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@Path("/coauth/module-registry/internal")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VerifyStatusConsumer {

    @Inject
    CoreModuleRegistryVerifyService coreModuleRegistryVerifyService;

    @Inject
    CoreModuleRegistryRegisterService coreModuleRegistryRegisterService;

    @POST
    @Path("/totp-verify-status")
    public Uni<Void> consumeTotpVerifyStatus(MessageVerificationStatusDto messageVerificationStatusDto) {
            return coreModuleRegistryVerifyService.updateCache(messageVerificationStatusDto.getCode(), messageVerificationStatusDto.getAppId(),
                    messageVerificationStatusDto.getUserId(), messageVerificationStatusDto.getStatus());
    }

    @POST
    @Path("/totp-register-status")
    public Uni<Void> consumeTotpRegisterStatus(MessageRegisterStatusDto messageRegisterStatusDto) {
        return coreModuleRegistryRegisterService.updateCache(messageRegisterStatusDto.getCode(), messageRegisterStatusDto.getAppId(),
                messageRegisterStatusDto.getUserId(), messageRegisterStatusDto.getStatus());
    }

    @POST
    @Path("/reconfirm-verify-status")
    public Uni<Void> consumeReconfirmVerifyStatus(MessageVerificationStatusDto messageVerificationStatusDto) {
        return coreModuleRegistryVerifyService.updateCache(messageVerificationStatusDto.getCode(), messageVerificationStatusDto.getAppId(),
                messageVerificationStatusDto.getUserId(), messageVerificationStatusDto.getStatus());
    }

}
