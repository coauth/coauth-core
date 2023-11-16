package dev.coauth.module.totp.dto;

import lombok.Data;

@Data
public class VerificationViewRequestDto {
    private String appCode;
}
