package dev.coauth.module.reconfirm.dto;

import lombok.Data;

@Data
public class VerificationGenerateRequestDto {
    public String userId;
    public int appId;
    public String codeChallenge;
    public String code;
}
