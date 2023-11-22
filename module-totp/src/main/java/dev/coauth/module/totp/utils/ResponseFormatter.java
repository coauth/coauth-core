package dev.coauth.module.totp.utils;

import dev.coauth.module.totp.dto.GenericResponseDto;
import dev.coauth.module.totp.exception.NonFatalException;
import jakarta.ws.rs.core.Response;

public class ResponseFormatter {
    public static Response formatFailureResponse(Throwable failure) {
        if (failure instanceof NonFatalException) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(GenericResponseDto.builder().error(new GenericResponseDto.ErrorDetails(((NonFatalException) failure).getErrCode(), failure.getMessage())).build())
                    .build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(GenericResponseDto.builder().error(new GenericResponseDto.ErrorDetails(0, failure.getMessage())).build())
                    .build();
        }
    }
}
