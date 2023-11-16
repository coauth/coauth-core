package dev.coauth.core.auth.guard.repository;

import dev.coauth.core.auth.guard.entity.CoreAppAuthMstrEntity;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class CoreAppAuthMstrRepository implements PanacheRepositoryBase<CoreAppAuthMstrEntity,String> {

//    @Inject
//    Mutiny.SessionFactory sessionFactory;

    @WithSession
    public Uni<List<CoreAppAuthMstrEntity>> getActiveKeys(){
        return listAll();
    }
}
