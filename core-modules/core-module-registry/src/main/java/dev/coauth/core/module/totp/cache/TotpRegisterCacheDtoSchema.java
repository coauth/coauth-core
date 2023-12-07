package dev.coauth.core.module.totp.cache;

import dev.coauth.core.module.messaging.MessageRegisterGenerateDto;
import dev.coauth.core.module.messaging.MessageRegisterStatusDto;
import dev.coauth.core.module.messaging.MessageVerificationGenerateDto;
import dev.coauth.core.module.messaging.MessageVerificationStatusDto;
import org.infinispan.protostream.GeneratedSchema;
import org.infinispan.protostream.annotations.AutoProtoSchemaBuilder;

//,schemaPackageName = "dev.coauth.module.totp.cache"
@AutoProtoSchemaBuilder(includeClasses = {
        TotpRegisterCacheDto.class,
        TotpVerificationCacheDto.class,
        AvailableVerificationCacheDto.class,
        AvailableRegisterCacheDto.class,
        MessageRegisterGenerateDto.class,
        MessageRegisterStatusDto.class,
        MessageVerificationStatusDto.class,
        MessageVerificationStatusDto.class },schemaPackageName = "totp_cache")
interface TotpRegisterCacheDtoSchema extends GeneratedSchema {
}
