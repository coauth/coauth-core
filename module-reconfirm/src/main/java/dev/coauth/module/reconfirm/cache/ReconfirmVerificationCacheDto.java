package dev.coauth.module.reconfirm.cache;

import dev.coauth.module.messaging.ReconfirmMessageVerificationGenerateDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoField;

import java.util.Date;


@Data
@NoArgsConstructor
public class ReconfirmVerificationCacheDto {
    @ProtoField(number = 1)
    public String code;
    @ProtoField(number = 2)
    public Integer appId;
    @ProtoField(number = 3)
    public String userId;
    @ProtoField(number = 4)
    public Integer noOfAttempts;
    @ProtoField(number = 5)
    public String codeChallenge;
    @ProtoField(number = 6)
    public String status;
    @ProtoField(number = 7)
    public ReconfirmFields reconfirmFields;
    @ProtoFactory
    public ReconfirmVerificationCacheDto(String code, Integer appId, String userId, Integer noOfAttempts, String codeChallenge, String status, ReconfirmFields reconfirmFields) {
        this.code = code;
        this.appId = appId;
        this.userId = userId;
        this.noOfAttempts = noOfAttempts;
        this.codeChallenge = codeChallenge;
        this.status = status;
        this.reconfirmFields = reconfirmFields;
    }

    @Data
    @Builder
    public static class ReconfirmFields {

        @ProtoField(number = 1)
        public String reconfirmValue;

        @ProtoField(number = 2)
        public Boolean isVisible;

        @ProtoField(number = 3)
        public String hintMessage;

        @ProtoFactory
        public ReconfirmFields(String reconfirmValue, Boolean isVisible, String hintMessage) {
            this.reconfirmValue = reconfirmValue;
            this.isVisible = isVisible;
            this.hintMessage = hintMessage;
        }
    }
}
