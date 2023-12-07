package dev.coauth.core.module.totp.cache;

import lombok.Builder;
import lombok.NoArgsConstructor;
import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoField;


@Builder
@NoArgsConstructor
public class AvailableVerificationCacheDto {
    private String code;
    private Integer appId;
    private String userId;
    private String status;

    private String codeChallenge;
    private String availableModules;

    private String currentModule;

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




    @ProtoField(number = 6)
    public String getAvailableModules() {
        return availableModules;
    }

    public void setAvailableModules(String availableModules) {
        this.availableModules = availableModules;
    }




    @ProtoField(number = 7)
    public String getCurrentModule() {
        return currentModule;
    }

    public void setCurrentModule(String currentModule) {
        this.currentModule = currentModule;
    }

    @ProtoFactory
    public AvailableVerificationCacheDto(String code, Integer appId, String userId, String status,String codeChallenge, String availableModules, String currentModule) {
        this.code = code;
        this.appId = appId;
        this.userId = userId;
        this.status = status;
        this.availableModules = availableModules;
        this.currentModule = currentModule;
        this.codeChallenge = codeChallenge;
    }
}
