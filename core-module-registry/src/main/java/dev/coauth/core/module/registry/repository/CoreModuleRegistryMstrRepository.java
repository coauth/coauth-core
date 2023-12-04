package dev.coauth.core.module.registry.repository;

import dev.coauth.commons.util.ApplicationConstants;
import dev.coauth.core.module.registry.entity.CoreModuleRegistryMstrEntity;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.List;

@ApplicationScoped
public class CoreModuleRegistryMstrRepository implements PanacheRepositoryBase<CoreModuleRegistryMstrEntity,String> {

    @Inject
    Mutiny.SessionFactory sessionFactory;

//    @WithSession
    public Uni<List<CoreModuleRegistryMstrEntity>> getAvailableModules(CoreModuleRegistryMstrEntity coreModuleRegistryMstrEntity){
        return sessionFactory.withSession(session -> session
                .createQuery("from CoreModuleRegistryMstrEntity t WHERE appId = :appId AND userId = :userId and status in (:status, :status2)", CoreModuleRegistryMstrEntity.class)
                .setParameter("appId", coreModuleRegistryMstrEntity.getAppId())
                .setParameter("userId", coreModuleRegistryMstrEntity.getUserId())
                .setParameter("status", coreModuleRegistryMstrEntity.getStatus())
                .setParameter("status2", ApplicationConstants.STATUS_DISABLED)
                .getResultList()
        );
//        return find("appId = ?1 and userId = ?2 and status in (?3, ?4)", coreModuleRegistryMstrEntity.getAppId(), coreModuleRegistryMstrEntity.getUserId(),coreModuleRegistryMstrEntity.getStatus(), ApplicationConstants.STATUS_DISABLED).list();
    }

//    @WithSession
    public Uni<List<CoreModuleRegistryMstrEntity>> getRegisteredModule(CoreModuleRegistryMstrEntity coreModuleRegistryMstrEntity){
        return sessionFactory.withSession(session -> session
                .createQuery("from CoreModuleRegistryMstrEntity t WHERE appId = :appId AND userId = :userId and status in (:status, :status2)", CoreModuleRegistryMstrEntity.class)
                .setParameter("appId", coreModuleRegistryMstrEntity.getAppId())
                .setParameter("userId", coreModuleRegistryMstrEntity.getUserId())
                .setParameter("status", ApplicationConstants.STATUS_ACTIVE)
                .setParameter("status2", ApplicationConstants.STATUS_DISABLED)
                .getResultList()
        );
//        return find("appId = ?1 and userId = ?2 and status in (?3, ?4)", coreModuleRegistryMstrEntity.getAppId(), coreModuleRegistryMstrEntity.getUserId(),ApplicationConstants.STATUS_ACTIVE, ApplicationConstants.STATUS_DISABLED).list();
    }

//    @WithSession
//    @WithTransaction
    public Uni<CoreModuleRegistryMstrEntity> save(CoreModuleRegistryMstrEntity coreModuleRegistryMstrEntity){
        return sessionFactory.withTransaction((session,tx) -> session.persist(coreModuleRegistryMstrEntity)
                .onItem().transformToUni(result -> Uni.createFrom().item(coreModuleRegistryMstrEntity))
                .onFailure().recoverWithUni(throwable -> Uni.createFrom().failure(throwable)));
//        return persist(coreModuleRegistryMstrEntity);
    }
}
