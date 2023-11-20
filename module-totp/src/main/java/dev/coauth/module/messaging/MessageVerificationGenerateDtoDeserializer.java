package dev.coauth.module.messaging;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;


public class MessageVerificationGenerateDtoDeserializer extends ObjectMapperDeserializer<MessageVerificationGenerateDto> {
    public MessageVerificationGenerateDtoDeserializer() {
        super(MessageVerificationGenerateDto.class);
    }
}
