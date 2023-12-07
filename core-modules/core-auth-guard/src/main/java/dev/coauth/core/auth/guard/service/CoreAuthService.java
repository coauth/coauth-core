package dev.coauth.core.auth.guard.service;

import dev.coauth.core.auth.guard.cache.local.ApiKeyCacheBean;
import dev.coauth.core.auth.guard.entity.CoreApplicationMstrEntity;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import io.quarkus.security.UnauthorizedException;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CoreAuthService {

    @Inject
    ApiKeyCacheBean apiKeyCacheBean;

    @WithSpan("CoreAuthService.authenticateApi")
    public Uni<CoreApplicationMstrEntity> authenticateApi(String apiKey) {
        return Uni.createFrom().item(apiKeyCacheBean.getValue(apiKey))
                .onItem().transformToUni(coreApplicationMstrEntity -> {
                    if (coreApplicationMstrEntity == null) {
                        return Uni.createFrom().failure(new UnauthorizedException("Invalid API Key"));
                    } else {
                        return Uni.createFrom().item(coreApplicationMstrEntity);
                    }
                });

    }
}
