package dev.coauth.core.module.totp.cache;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.infinispan.commons.marshall.JavaSerializationMarshaller;
import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoField;

import java.sql.Timestamp;
import java.util.Objects;


public class TotpRegisterCacheDto {
    private String code;
    private Integer appId;

    private String userId;

    private String secret;
    private String codeChallenge;


    @ProtoField(number = 1)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @ProtoField(number = 2)
    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }


    @ProtoField(number = 3)
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    @ProtoField(number = 4)
    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    @ProtoField(number = 5)
    public String getCodeChallenge() {
        return codeChallenge;
    }

    public void setCodeChallenge(String codeChallenge) {
        this.codeChallenge = codeChallenge;
    }

    @ProtoFactory
    public TotpRegisterCacheDto(String code, Integer appId, String userId, String secret, String codeChallenge) {
        this.code = code;
        this.appId = appId;
        this.userId = userId;
        this.secret = secret;
        this.codeChallenge = codeChallenge;
    }
}
