package dev.coauth.core.module.registry.dto;

import dev.coauth.core.dto.CoreAppDetails;
import lombok.Data;

@Data
public class VerifyStatusRequestDto {
    private String code;
    private String codeVerifier;
}
