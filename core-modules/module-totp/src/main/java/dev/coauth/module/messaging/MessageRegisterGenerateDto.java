package dev.coauth.module.messaging;

import lombok.Builder;
import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoField;


@Builder
public class MessageRegisterGenerateDto {
    private String code;
    private Integer appId;

    private String userId;

    private String codeChallenge;

    private String status;

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
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @ProtoField(number = 5)
    public String getCodeChallenge() {
        return codeChallenge;
    }

    public void setCodeChallenge(String codeChallenge) {
        this.codeChallenge = codeChallenge;
    }

    @ProtoFactory
    public MessageRegisterGenerateDto(String code, Integer appId, String userId, String status, String codeChallenge) {
        this.code = code;
        this.appId = appId;
        this.userId = userId;
        this.codeChallenge = codeChallenge;
        this.status = status;
    }
}
