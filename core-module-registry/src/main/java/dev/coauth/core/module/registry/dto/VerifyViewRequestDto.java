package dev.coauth.core.module.registry.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class VerifyViewRequestDto {
    private String code;
}
