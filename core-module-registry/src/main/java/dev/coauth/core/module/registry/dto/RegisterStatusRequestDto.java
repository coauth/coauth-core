package dev.coauth.core.module.registry.dto;

import lombok.Data;

@Data
public class RegisterStatusRequestDto {
    private String code;
    private String codeVerifier;
}
