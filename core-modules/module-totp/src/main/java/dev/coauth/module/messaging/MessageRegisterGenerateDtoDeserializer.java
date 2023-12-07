package dev.coauth.module.messaging;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;


public class MessageRegisterGenerateDtoDeserializer extends ObjectMapperDeserializer<MessageRegisterGenerateDto> {
    public MessageRegisterGenerateDtoDeserializer() {
        super(MessageRegisterGenerateDto.class);
    }
}
