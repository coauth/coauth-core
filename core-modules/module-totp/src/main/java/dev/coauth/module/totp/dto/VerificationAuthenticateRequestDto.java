package dev.coauth.module.totp.dto;

import lombok.Data;

@Data
public class VerificationAuthenticateRequestDto {
    private String appCode;
    private String totpCode;
}
