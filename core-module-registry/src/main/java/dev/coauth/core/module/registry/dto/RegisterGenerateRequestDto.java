package dev.coauth.core.module.registry.dto;

import dev.coauth.core.dto.CoreAppDetails;
import lombok.Data;

@Data
public class RegisterGenerateRequestDto {
    private String userId;
    private CoreAppDetails appDetails;
    private String module;
    private String codeChallenge;
}
