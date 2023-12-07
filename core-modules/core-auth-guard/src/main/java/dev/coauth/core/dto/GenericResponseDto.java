package dev.coauth.core.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericResponseDto {

    public static class ErrorDetails {
        @JsonProperty("code")
        private int errorCode;

        @JsonProperty("message")
        private String errMessage;

        public ErrorDetails(int errorCode, String errMessage) {
            this.errorCode = errorCode;
            this.errMessage = errMessage;
        }
    }
    @JsonProperty("error")
    private ErrorDetails error;
    @JsonProperty("data")
    private Object data;

    public GenericResponseDto(ErrorDetails error, Object data){
        this.data=data;
        this.error=error;
    }

    public static GenericResponseDto success(Object data) {
        return new GenericResponseDto(null, data);
    }

    public static GenericResponseDto error(int errorCode, String errMessage) {
        return new GenericResponseDto(new ErrorDetails(errorCode, errMessage), null);
    }
}
