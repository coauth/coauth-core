package dev.coauth.core.module.registry.endpoint;


import dev.coauth.core.dto.GenericResponseDto;
import dev.coauth.core.exception.NonFatalException;
import dev.coauth.core.module.registry.dto.VerifyGenerateRequestDto;
import dev.coauth.core.module.registry.dto.VerifyStatusRequestDto;
import dev.coauth.core.module.registry.service.CoreModuleRegistryService;
import io.quarkus.security.UnauthorizedException;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/core/module-registry")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CoreModuleRegistryEndpoint {

    @Inject
    CoreModuleRegistryService coreModuleRegistryService;

    @POST
    @Path("/generate")
    public Uni<Response> authenticate(@Valid VerifyGenerateRequestDto generateRequestDto)   {
        if(generateRequestDto.getAppDetails().getAppId() == 0 || generateRequestDto.getUserId() == null || generateRequestDto.getUserId().trim().isEmpty()){
            return Uni.createFrom().item(formatFailureResponse(new UnauthorizedException("Invalid Request")));
        }else{
            return coreModuleRegistryService.validateModuleRequest(generateRequestDto)
                    .onItem().transform(entity -> {
                        return Response.ok()
                                .entity(GenericResponseDto.builder().data(entity).build())
                                .build();
                    })
                    .onFailure().recoverWithItem(this::formatFailureResponse);
        }
    }


    @POST
    @Path("/status")
    public Uni<Response> verify(@Valid VerifyStatusRequestDto verifyStatusRequestDto)   {

        return coreModuleRegistryService.validateVerification(verifyStatusRequestDto)
                .onItem().transform(entity -> {
                    return Response.ok()
                            .entity(GenericResponseDto.builder().data(entity).build())
                            .build();
                })
                .onFailure().recoverWithItem(this::formatFailureResponse);
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
