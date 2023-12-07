package dev.coauth.core.module.registry.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VerifyViewResponseDto {
    private String code;
    private String availableModules;
    private String currentModule;
}
