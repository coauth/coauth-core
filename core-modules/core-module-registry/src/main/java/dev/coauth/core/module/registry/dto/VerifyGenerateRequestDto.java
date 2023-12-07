package dev.coauth.core.module.registry.dto;

import dev.coauth.core.dto.CoreAppDetails;
import lombok.Data;

@Data
public class VerifyGenerateRequestDto {
    private String userId;
    private CoreAppDetails appDetails;
    private String modules;
    private String reConfirmText;
    private String codeChallenge;
}
