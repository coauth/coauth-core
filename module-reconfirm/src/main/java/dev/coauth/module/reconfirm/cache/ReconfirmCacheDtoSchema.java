package dev.coauth.module.reconfirm.cache;

import org.infinispan.protostream.GeneratedSchema;
import org.infinispan.protostream.annotations.AutoProtoSchemaBuilder;

//,schemaPackageName = "dev.coauth.module.totp.cache"
@AutoProtoSchemaBuilder(includeClasses = { ReconfirmVerificationCacheDto.class},schemaPackageName = "reconfirm_cache")
interface ReconfirmCacheDtoSchema extends GeneratedSchema {
}
