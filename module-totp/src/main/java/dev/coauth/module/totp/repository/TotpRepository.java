package dev.coauth.module.totp.repository;

import dev.coauth.module.totp.entity.TotpMstrEntity;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import org.hibernate.reactive.mutiny.Mutiny;

@ApplicationScoped
public class TotpRepository implements PanacheRepositoryBase<TotpMstrEntity,String> {

    @Inject
    Mutiny.SessionFactory sessionFactory;


    @WithSession
    public Uni<TotpMstrEntity> get(TotpMstrEntity totpMstrEntity){
        return find("appId = ?1 and userId = ?2", totpMstrEntity.getAppId(), totpMstrEntity.getUserId()).firstResult();
       /* return Uni.createFrom().deferred(() -> sessionFactory.withSession(session -> session
                .createQuery("SELECT t FROM TotpMstrEntity t WHERE appId = :appId AND userId = :userId", TotpMstrEntity.class)
                .setParameter("appId", totpMstrEntity.getAppId())
                .setParameter("userId", totpMstrEntity.getUserId())
                .getSingleResult()
                .onItem().transformToUni(result -> Uni.createFrom().item(result))
                .onFailure().recoverWithUni(throwable -> {
                    if (throwable instanceof NoResultException) {
                        return Uni.createFrom().nullItem();
                    } else {
                        return Uni.createFrom().failure(throwable);
                    }
                })
        ));*/
    }

    @WithSession
    @WithTransaction
    public Uni<TotpMstrEntity> save(TotpMstrEntity totpMstrEntity){
        return persist(totpMstrEntity);
    }
}
