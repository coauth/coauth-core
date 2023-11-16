package dev.coauth.core.module.registry.service;

import dev.coauth.core.module.totp.cache.ReconfirmVerificationCacheDto;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.MutinyEmitter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;

@ApplicationScoped
public class MessageBrokerService {

    @Inject
    @Channel("totp-verify-generate")
    MutinyEmitter<ReconfirmVerificationCacheDto> totpVerifyGenerateEmitter;

    @Inject
    @Channel("reconfirm-verify-generate")
    MutinyEmitter<ReconfirmVerificationCacheDto> reconfirmVerifyGenerateEmitter;

    public Uni<Void> emitTotpVerifyGenerate(ReconfirmVerificationCacheDto reconfirmVerificationCacheDto){
        return totpVerifyGenerateEmitter.send(reconfirmVerificationCacheDto);
    }

    public Uni<Void> emitReconfirmVerifyGenerate(ReconfirmVerificationCacheDto reconfirmVerificationCacheDto){
        return reconfirmVerifyGenerateEmitter.send(reconfirmVerificationCacheDto);
    }
}
