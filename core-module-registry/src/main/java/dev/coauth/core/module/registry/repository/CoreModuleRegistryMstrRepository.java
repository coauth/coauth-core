package dev.coauth.core.module.registry.repository;

import dev.coauth.core.module.registry.entity.CoreModuleRegistryMstrEntity;
import dev.coauth.core.utils.ApplicationConstants;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class CoreModuleRegistryMstrRepository implements PanacheRepositoryBase<CoreModuleRegistryMstrEntity,String> {

    @WithSession
    public Uni<List<CoreModuleRegistryMstrEntity>> getAvailableModules(CoreModuleRegistryMstrEntity coreModuleRegistryMstrEntity){
        return find("appId = ?1 and userId = ?2 and status in (?3, ?4)", coreModuleRegistryMstrEntity.getAppId(), coreModuleRegistryMstrEntity.getUserId(),coreModuleRegistryMstrEntity.getStatus(), ApplicationConstants.STATUS_DISABLED).list();
    }
}
