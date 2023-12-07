package dev.coauth.core.auth.guard.cache.local;

import dev.coauth.core.auth.guard.entity.CoreApplicationMstrEntity;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.concurrent.ConcurrentHashMap;


@ApplicationScoped
public class ApiKeyCacheBean {

    private final ConcurrentHashMap<String, CoreApplicationMstrEntity> apiKeyMap = new ConcurrentHashMap<>();

    public ConcurrentHashMap<String, CoreApplicationMstrEntity> getApiKeys() {
        return apiKeyMap;
    }

    public CoreApplicationMstrEntity getValue(String key) {
        return apiKeyMap.get(key);
    }

    public boolean containsKey(String key) {
        return apiKeyMap.containsKey(key);
    }

    public void addOrUpdate(String key, CoreApplicationMstrEntity coreApplicationMstrEntity) {
        apiKeyMap.put(key, coreApplicationMstrEntity);
    }
    public void delete(String key) {
        apiKeyMap.remove(key);
    }

}
