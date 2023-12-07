package dev.coauth.core.module.messaging;

import lombok.Data;

@Data
public class MessageVerificationStatusDto {
    String code;
    String userId;
    Integer appId;
    String status;
}
