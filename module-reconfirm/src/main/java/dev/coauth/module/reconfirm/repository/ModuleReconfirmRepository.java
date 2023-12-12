package dev.coauth.module.reconfirm.repository;

import dev.coauth.commons.util.ApplicationConstants;
import dev.coauth.module.reconfirm.entity.ModuleReconfirmMstrEntity;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.hibernate.reactive.mutiny.Mutiny;

@ApplicationScoped
public class ModuleReconfirmRepository implements PanacheRepositoryBase<ModuleReconfirmMstrEntity,String> {

    @Inject
    Mutiny.SessionFactory sessionFactory;


//    @WithSession
    public Uni<ModuleReconfirmMstrEntity> get(ModuleReconfirmMstrEntity moduleReconfirmMstrEntity){
        return sessionFactory.withSession(session -> session
                .createQuery("from ModuleReconfirmMstrEntity t WHERE appId = :appId AND userId = :userId and status = :status", ModuleReconfirmMstrEntity.class)
                .setParameter("appId", moduleReconfirmMstrEntity.getAppId())
                .setParameter("userId", moduleReconfirmMstrEntity.getUserId())
                .setParameter("status", ApplicationConstants.STATUS_DISABLED)
                .getSingleResult()
                .onItem().transformToUni(result -> Uni.createFrom().item(result))
                .onFailure().recoverWithUni(throwable -> Uni.createFrom().nullItem())
        );
//        return find("appId = ?1 and userId = ?2 and status= ?3", moduleReconfirmMstrEntity.getAppId(), moduleReconfirmMstrEntity.getUserId(), ApplicationConstants.STATUS_DISABLED).firstResult();
    }

//    @WithSession
//    @WithTransaction
    public Uni<ModuleReconfirmMstrEntity> save(ModuleReconfirmMstrEntity moduleReconfirmMstrEntity){
        return sessionFactory.withTransaction((session,tx) -> session.persist(moduleReconfirmMstrEntity)
                .onItem().transformToUni(result -> Uni.createFrom().item(moduleReconfirmMstrEntity))
                .onFailure().recoverWithUni(throwable -> Uni.createFrom().failure(throwable)));
//        return persist(moduleReconfirmMstrEntity);
    }
}
