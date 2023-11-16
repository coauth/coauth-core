package dev.coauth.core.module.registry.dto;

import dev.coauth.core.dto.CoreAppDetails;
import lombok.Data;

@Data
public class GenerateRequestDto {
    private String userId;
    private CoreAppDetails appDetails;
    private String modules;
    private String reConfirmText;
    private String codeChallenge;
}
