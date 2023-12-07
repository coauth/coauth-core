package dev.coauth.core.auth.guard.cache.local;

import dev.coauth.core.auth.guard.entity.CoreApplicationMstrEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiKeyDto {

    private String apiKey;
    private CoreApplicationMstrEntity appConfig;
}
