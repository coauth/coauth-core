package dev.coauth.core.module.messaging;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;
import lombok.Data;


public class MessageVerificationStatusDtoDeserializer extends ObjectMapperDeserializer<MessageVerificationStatusDto> {
    public MessageVerificationStatusDtoDeserializer() {
        super(MessageVerificationStatusDto.class);
    }
}
