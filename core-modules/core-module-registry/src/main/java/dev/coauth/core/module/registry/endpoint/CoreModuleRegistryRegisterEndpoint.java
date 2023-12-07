package dev.coauth.core.module.registry.endpoint;


import dev.coauth.core.dto.GenericResponseDto;
import dev.coauth.core.exception.NonFatalException;
import dev.coauth.core.module.registry.dto.RegisterGenerateRequestDto;
import dev.coauth.core.module.registry.dto.RegisterStatusRequestDto;
import dev.coauth.core.module.registry.dto.RegisterViewRequestDto;
import dev.coauth.core.module.registry.dto.VerifyStatusRequestDto;
import dev.coauth.core.module.registry.service.CoreModuleRegistryRegisterService;
import dev.coauth.core.module.registry.service.CoreModuleRegistryVerifyService;
import io.quarkus.security.UnauthorizedException;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/coauth/core/module-registry/register")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CoreModuleRegistryRegisterEndpoint {

    @Inject
    CoreModuleRegistryRegisterService coreModuleRegistryRegisterService;

    @POST
    @Path("/generate")
    public Uni<Response> authenticate(@Valid RegisterGenerateRequestDto registerGenerateRequestDto)   {
        if(registerGenerateRequestDto.getAppDetails().getAppId() == 0 || registerGenerateRequestDto.getUserId() == null || registerGenerateRequestDto.getUserId().trim().isEmpty()){
            return Uni.createFrom().item(formatFailureResponse(new UnauthorizedException("Invalid Request")));
        }else{
            return coreModuleRegistryRegisterService.validateModuleRequest(registerGenerateRequestDto)
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
    public Uni<Response> verify(@Valid RegisterStatusRequestDto registerStatusRequestDto)   {

        return coreModuleRegistryRegisterService.validateVerification(registerStatusRequestDto)
                .onItem().transform(entity -> {
                    return Response.ok()
                            .entity(GenericResponseDto.builder().data(entity).build())
                            .build();
                })
                .onFailure().recoverWithItem(this::formatFailureResponse);
    }

    @POST
    @Path("/load")
    public Uni<Response> load(@Valid RegisterViewRequestDto registerViewRequestDto)   {

        return coreModuleRegistryRegisterService.getViewDetails(registerViewRequestDto.getCode())
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
