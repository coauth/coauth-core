package dev.coauth.core.auth.guard.scheduler;

import dev.coauth.core.auth.guard.cache.local.ApiKeyCacheBean;
import dev.coauth.core.auth.guard.repository.CoreAppAuthMstrRepository;
import dev.coauth.core.auth.guard.repository.CoreApplicationMstrRepository;
import dev.coauth.core.auth.guard.entity.CoreAppAuthMstrEntity;
import dev.coauth.core.auth.guard.entity.CoreApplicationMstrEntity;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import io.quarkus.scheduler.Scheduled;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class ApiKeyCacheRefreshScheduler {

    @Inject
    CoreAppAuthMstrRepository coreAppAuthMstrRepository;

    @Inject
    CoreApplicationMstrRepository coreApplicationMstrRepository;

    @Inject
    ApiKeyCacheBean apiKeyCacheBean;


    /**
    * Scheduler is being called on startup so this code is not needed.
    *
    void onStart(@Observes StartupEvent event, Vertx vertx, Mutiny.SessionFactory factory) {
        // We need a duplicated vertx context for hibernate reactive
        Context context = VertxContext.getOrCreateDuplicatedContext(vertx);
        // Don't forget to mark the context safe
        VertxContextSafetyToggle.setContextSafe(context, true);
        // Run the logic on the context created above
        context.runOnContext(new Handler<Void>() {
            @Override
            public void handle(Void event) {
                reFetchKeys()
                        // We need to subscribe to the Uni to trigger the action
                        .subscribe().with(v -> {});
            }
        });
    }*/

    @Scheduled(every = "{coauth.core.auth-guard.refresh-api-keys.interval}", concurrentExecution = Scheduled.ConcurrentExecution.SKIP)
    @WithSpan("scheduler:refresh-api-keys")
    Uni<Void> reFetchKeys() {
        return Uni.createFrom().voidItem()
                .onItem().transformToUni(v -> processKeys());
    }

    private Uni<Void> processKeys() {
        return Uni.combine().all()
                .unis(coreAppAuthMstrRepository.getActiveKeys(), coreApplicationMstrRepository.getActiveApps())
                .asTuple()
                .onItem().transformToUni(tuple -> {
                    List<CoreAppAuthMstrEntity> listCoreAuth = tuple.getItem1();
                    List<CoreApplicationMstrEntity> listCoreAppMstr = tuple.getItem2();
                    Map<Integer, CoreApplicationMstrEntity> mapCoreAppMstr = listToHashMap(listCoreAppMstr);
                    Set<String> valiKeysSet = new HashSet<>();

                    listCoreAuth.forEach(coreAppAuthMstrEntity -> {
                        CoreApplicationMstrEntity coreApplicationMstrEntity = mapCoreAppMstr.get(coreAppAuthMstrEntity.getAppId());
                        if (!apiKeyCacheBean.containsKey(coreAppAuthMstrEntity.getAppKey())) {
                            apiKeyCacheBean.addOrUpdate(coreAppAuthMstrEntity.getAppKey(), coreApplicationMstrEntity);
                        } else {
                            String newValue = coreApplicationMstrEntity.toString();
                            if (!newValue.equals(apiKeyCacheBean.getValue(coreAppAuthMstrEntity.getAppKey()).toString())) {
                                apiKeyCacheBean.addOrUpdate(coreAppAuthMstrEntity.getAppKey(), coreApplicationMstrEntity);
                            }
                        }
                        valiKeysSet.add(coreAppAuthMstrEntity.getAppKey());
                    });
                    apiKeyCacheBean.getApiKeys().keySet().removeIf(key -> !valiKeysSet.contains(key));

                    // Return a Uni<Void>
                    return Uni.createFrom().voidItem();
                });
    }

    private static Map<Integer, CoreApplicationMstrEntity> listToHashMap(List<CoreApplicationMstrEntity> list) {
        return list.stream()
                .collect(Collectors.toMap(CoreApplicationMstrEntity::getAppId, entity -> entity));
    }

    /**
     * This method is used to print the API Keys in the cache. and should be removed in production.
     * /
 /*
    @Scheduled(every = "10s", concurrentExecution = Scheduled.ConcurrentExecution.SKIP)
    Uni<Void> printKeys() {
        System.out.println("Printing API Keys...");
        apiKeyCacheBean.getApiKeys().forEach((key, value) -> {
            System.out.println(key + " : " + value.getAppName());
        });
        return Uni.createFrom().voidItem();
    }
     */
}
