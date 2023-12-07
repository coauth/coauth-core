package dev.coauth.module.reconfirm.consumer;

import dev.coauth.module.messaging.ReconfirmMessageVerificationGenerateDto;
import dev.coauth.module.reconfirm.dto.VerificationGenerateRequestDto;
import dev.coauth.module.reconfirm.service.ReconfirmService;
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

@Path("/coauth/module/internal/reconfirm")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GenerateConsumer {

    @Inject
    ReconfirmService totpService;

    @POST
    @Path("/reconfirm-verify-generate")
    public Uni<Void> consume(ReconfirmMessageVerificationGenerateDto messageVerificationGenerateDto) {
        return totpService.generateAuthVerification(messageVerificationGenerateDto);
    }
}
