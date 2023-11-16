package dev.coauth.module.totp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
@Data

public class VerificationRequest {
    @NotNull
    @Size(min = 1, max = 2)
    private String value;

}
