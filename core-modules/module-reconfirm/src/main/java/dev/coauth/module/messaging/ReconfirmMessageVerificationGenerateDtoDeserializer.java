package dev.coauth.module.messaging;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;


public class ReconfirmMessageVerificationGenerateDtoDeserializer extends ObjectMapperDeserializer<ReconfirmMessageVerificationGenerateDto> {
    public ReconfirmMessageVerificationGenerateDtoDeserializer() {
        super(ReconfirmMessageVerificationGenerateDto.class);
    }
}
