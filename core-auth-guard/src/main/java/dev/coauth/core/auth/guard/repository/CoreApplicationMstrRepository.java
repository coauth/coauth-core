package dev.coauth.core.auth.guard.repository;

import dev.coauth.core.auth.guard.entity.CoreApplicationMstrEntity;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class CoreApplicationMstrRepository implements PanacheRepositoryBase<CoreApplicationMstrEntity,String> {

//    @Inject
//    Mutiny.SessionFactory sessionFactory;

    @WithSession
    public Uni<List<CoreApplicationMstrEntity>> getActiveApps(){
        return listAll();
    }
}
