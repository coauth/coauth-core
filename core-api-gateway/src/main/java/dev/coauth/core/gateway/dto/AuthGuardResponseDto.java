package dev.coauth.core.gateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AuthGuardResponseDto {
    @JsonProperty("data")
    CoreAppAuthMstrProperties data;
}
