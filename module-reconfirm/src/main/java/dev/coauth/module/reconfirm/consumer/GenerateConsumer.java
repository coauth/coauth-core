package dev.coauth.module.reconfirm.consumer;

import dev.coauth.module.messaging.ReconfirmMessageVerificationGenerateDto;
import dev.coauth.module.reconfirm.dto.VerificationGenerateRequestDto;
import dev.coauth.module.reconfirm.service.ReconfirmService;
import io.smallrye.common.annotation.NonBlocking;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class GenerateConsumer {

    @Inject
    ReconfirmService totpService;

    @Incoming("reconfirm-verify-generate")
    @NonBlocking
    public Uni<Void> consume(ReconfirmMessageVerificationGenerateDto messageVerificationGenerateDto) {
        return totpService.generateAuthVerification(messageVerificationGenerateDto);
    }
}
