package dev.coauth.core.module.totp.cache;

import org.infinispan.protostream.GeneratedSchema;
import org.infinispan.protostream.annotations.AutoProtoSchemaBuilder;

//,schemaPackageName = "dev.coauth.module.totp.cache"
@AutoProtoSchemaBuilder(includeClasses = { TotpRegisterCacheDto.class, TotpVerificationCacheDto.class,ReconfirmVerificationCacheDto.class,AvailableVerificationCacheDto.class},schemaPackageName = "totp_cache")
interface TotpRegisterCacheDtoSchema  extends GeneratedSchema {
}
