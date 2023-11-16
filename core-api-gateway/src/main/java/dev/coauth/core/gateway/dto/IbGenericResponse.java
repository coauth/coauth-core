package dev.coauth.core.gateway.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IbGenericResponse {
    private String results;
    private String errorCode;
    String errorDescription;

}
