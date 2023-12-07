package dev.coauth.module.reconfirm.dto;

import lombok.Data;

@Data
public class VerificationAuthenticateRequestDto {
    private String appCode;
    private String totpCode;
    private String reconfirmInputText;
}
