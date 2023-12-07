package dev.coauth.module.totp.cache;

import lombok.NoArgsConstructor;
import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoField;

import java.util.Date;

@NoArgsConstructor
public class TotpVerificationCacheDto {
    private String code;
    private Integer appId;

    private String userId;

    private Integer noOfAttempts;

    private Date expiryTime;

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
    public Integer getNoOfAttempts() {
        return noOfAttempts;
    }

    public void setNoOfAttempts(Integer noOfAttempts) {
        this.noOfAttempts = noOfAttempts;
    }

    @ProtoField(number = 5)
    public Date getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Date expiryTime) {
        this.expiryTime = expiryTime;
    }

    @ProtoField(number = 6)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @ProtoField(number = 7)
    public String getCodeChallenge() {
        return codeChallenge;
    }

    public void setCodeChallenge(String codeChallenge) {
        this.codeChallenge = codeChallenge;
    }

    @ProtoFactory
    public TotpVerificationCacheDto(String code, Integer appId, String userId, Integer noOfAttempts, Date expiryTime, String status,String codeChallenge) {
        this.code = code;
        this.appId = appId;
        this.userId = userId;
        this.noOfAttempts = noOfAttempts;
        this.expiryTime = expiryTime;
        this.codeChallenge = codeChallenge;
        this.status = status;
    }
}
