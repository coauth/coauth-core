package dev.coauth.module.reconfirm.repository;

import dev.coauth.module.reconfirm.entity.ModuleReconfirmMstrEntity;
import dev.coauth.module.reconfirm.utils.ApplicationConstants;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.hibernate.reactive.mutiny.Mutiny;

@ApplicationScoped
public class ModuleReconfirmRepository implements PanacheRepositoryBase<ModuleReconfirmMstrEntity,String> {

    @Inject
    Mutiny.SessionFactory sessionFactory;


    @WithSession
    public Uni<ModuleReconfirmMstrEntity> get(ModuleReconfirmMstrEntity moduleReconfirmMstrEntity){
        return find("appId = ?1 and userId = ?2 and status= ?3", moduleReconfirmMstrEntity.getAppId(), moduleReconfirmMstrEntity.getUserId(), ApplicationConstants.STATUS_DISABLED).firstResult();
    }

    @WithSession
    @WithTransaction
    public Uni<ModuleReconfirmMstrEntity> save(ModuleReconfirmMstrEntity moduleReconfirmMstrEntity){
        return persist(moduleReconfirmMstrEntity);
    }
}
