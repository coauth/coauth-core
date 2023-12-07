package dev.coauth.core.module.messaging;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;
import lombok.Data;


public class MessageRegisterStatusDtoDeserializer extends ObjectMapperDeserializer<MessageRegisterStatusDto> {
    public MessageRegisterStatusDtoDeserializer() {
        super(MessageRegisterStatusDto.class);
    }
}
