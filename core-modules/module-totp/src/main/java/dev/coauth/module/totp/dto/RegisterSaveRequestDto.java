package dev.coauth.module.totp.dto;

import lombok.Data;

@Data
public class RegisterSaveRequestDto {
    private String appCode;

    private String totpCode;
}
