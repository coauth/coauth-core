package dev.coauth.module.totp.dto;

import lombok.Data;

@Data
public class RegisterViewRequestDto {
    private String appCode;
}
