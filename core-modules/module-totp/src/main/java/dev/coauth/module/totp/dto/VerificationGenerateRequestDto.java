package dev.coauth.module.totp.dto;

import lombok.Data;

@Data
public class VerificationGenerateRequestDto {
    public String userId;
    public int appId;
    public String codeChallenge;
    public String code;
}
