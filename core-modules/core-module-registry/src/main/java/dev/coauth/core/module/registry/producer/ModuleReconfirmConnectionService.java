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

@RegisterRestClient(configKey = "coauth-module-reconfirm")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/coauth/module/internal/reconfirm")

public interface ModuleReconfirmConnectionService {

    @POST
    @Path("/reconfirm-verify-generate")
    Uni<Void> emitReconfirmVerifyGenerate(ReconfirmMessageVerificationGenerateDto reconfirmMessageVerificationGenerateDto);

}
