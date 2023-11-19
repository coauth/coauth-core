package dev.coauth.core.module.registry.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterViewResponseDto {
    private String code;
    private String module;
}
