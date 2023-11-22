package dev.coauth.module.reconfirm.dto;

import lombok.Data;

@Data
public class VerificationValidateRequestDto {
    private String appCode;
    private String codeVerifier;
}
