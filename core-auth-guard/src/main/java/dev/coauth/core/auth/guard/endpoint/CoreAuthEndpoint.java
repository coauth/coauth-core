package dev.coauth.core.auth.guard.endpoint;


import dev.coauth.commons.util.ResponseFormatter;
import dev.coauth.core.dto.GenericResponseDto;
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
            return Uni.createFrom().item(ResponseFormatter.formatFailureResponse(new UnauthorizedException("Invalid API Key")));
        }else{
            return coreAuthService.authenticateApi(coAuthApiKey)
                    .onItem().transform(entity -> {
                        return Response.ok()
                                .entity(GenericResponseDto.builder().data(entity).build())
                                .build();
                    })
                    .onFailure().recoverWithItem(ResponseFormatter::formatFailureResponse);
        }
    }




}
