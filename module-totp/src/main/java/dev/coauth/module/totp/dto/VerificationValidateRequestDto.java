package dev.coauth.module.totp.dto;

import lombok.Data;

@Data
public class VerificationValidateRequestDto {
    private String appCode;
    private String codeVerifier;
}
