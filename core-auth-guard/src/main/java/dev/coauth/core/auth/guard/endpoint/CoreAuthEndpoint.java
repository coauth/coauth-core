package dev.coauth.core.auth.guard.endpoint;


import dev.coauth.core.dto.GenericResponseDto;
import dev.coauth.core.exception.NonFatalException;
import dev.coauth.core.auth.guard.service.CoreAuthService;
import io.quarkus.security.UnauthorizedException;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/core/auth-guard")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CoreAuthEndpoint {

    @Inject
    CoreAuthService coreAuthService;

    @GET
    @Path("/authenticate/with-api-key")
    public Uni<Response> authenticate(@HeaderParam("COAUTH-API-KEY") String coAuthApiKey)   {
        if(coAuthApiKey == null || coAuthApiKey.isEmpty()){
            return Uni.createFrom().item(formatFailureResponse(new UnauthorizedException("Invalid API Key")));
        }else{
            return coreAuthService.authenticateApi(coAuthApiKey)
                    .onItem().transform(entity -> {
                        return Response.ok()
                                .entity(GenericResponseDto.builder().data(entity).build())
                                .build();
                    })
                    .onFailure().recoverWithItem(this::formatFailureResponse);
        }
    }


    private  Response formatFailureResponse(Throwable failure) {
        if (failure instanceof NonFatalException) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(GenericResponseDto.builder().error(new GenericResponseDto.ErrorDetails(((NonFatalException) failure).getErrCode(), failure.getMessage())).build())
                    .build();
        }else if(failure instanceof UnauthorizedException){
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(GenericResponseDto.builder().error(new GenericResponseDto.ErrorDetails(401, failure.getMessage())).build())
                    .build();
        }else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(GenericResponseDto.builder().error(new GenericResponseDto.ErrorDetails(0, failure.getMessage())).build())
                    .build();
        }
    }

}
