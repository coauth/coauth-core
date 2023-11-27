package dev.coauth.module.reconfirm.producer;

import dev.coauth.module.messaging.MessageVerificationStatusDto;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "coauth-core-module-registry")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/coauth/module-registry/internal")
public interface ModuleReconfirmConnectionService {

    @POST
    @Path("/reconfirm-verify-status")
    Uni<Void> emitReconfirmVerifyStatus(MessageVerificationStatusDto messageVerificationStatusDto);

}
