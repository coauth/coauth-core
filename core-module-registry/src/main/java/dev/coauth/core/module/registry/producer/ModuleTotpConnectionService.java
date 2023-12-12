package dev.coauth.core.module.registry.producer;

import dev.coauth.core.module.messaging.MessageRegisterGenerateDto;
import dev.coauth.core.module.messaging.MessageVerificationGenerateDto;
import dev.coauth.core.module.messaging.ReconfirmMessageVerificationGenerateDto;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "coauth-module-totp")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/coauth/module/internal/totp")

public interface ModuleTotpConnectionService {
    @POST
    @Path("/totp-verify-generate")
    Uni<Void> emitTotpVerifyGenerate(MessageVerificationGenerateDto messageVerificationGenerateDto);

    @POST
    @Path("/totp-register-generate")
    Uni<Void> emitTotpRegisterGenerate(MessageRegisterGenerateDto messageVerificationGenerateDto);

}
