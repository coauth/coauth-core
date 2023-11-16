package dev.coauth.module.totp.dto;

import lombok.Data;

@Data
public class RegisterVerifyRequestDto {
    private String appCode;
    private String codeVerifier;
}
