package dev.coauth.module.reconfirm.endpoint;


import dev.coauth.commons.util.ResponseFormatter;
import dev.coauth.module.reconfirm.dto.*;
import dev.coauth.module.reconfirm.service.ReconfirmService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/coauth/module/reconfirm/verification")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ModuleReconfirmVerificationEndpoint {

    @Inject
    ReconfirmService totpService;

   /* @POST
    @Path("/generate-request")
    public Uni<Response> generate(@Valid VerificationGenerateRequestDto verificationGenerateRequestDto) {
        return totpService.generateAuthVerification(verificationGenerateRequestDto)
                .onItem().transform(entity -> {
                    return Response.ok()
                            .entity(GenericResponseDto.builder().data(entity).build())
                            .build();
                })
                .onFailure().recoverWithItem(ResponseFormatter::formatFailureResponse);
    }*/

    @POST
    @Path("/view")
    public Uni<Response> view(@Valid VerificationViewRequestDto verificationViewRequestDto) {
        return totpService.viewAuthVerification(verificationViewRequestDto)
                .onItem().transform(entity -> {
                    return Response.ok()
                            .entity(GenericResponseDto.builder().data(entity).build())
                            .build();
                })
                .onFailure().recoverWithItem(ResponseFormatter::formatFailureResponse);
    }

    @POST
    @Path("/authenticate")
    public Uni<Response> authenticateVerification(@Valid VerificationAuthenticateRequestDto verificationAuthenticateRequestDto) {
        return totpService.authenticateVerification(verificationAuthenticateRequestDto)
                .onItem().transform(entity -> {
                    return Response.ok()
                            .entity(GenericResponseDto.builder().data(entity).build())
                            .build();
                })
                .onFailure().recoverWithItem(ResponseFormatter::formatFailureResponse);
    }


/*    @POST
    @Path("/validate")
    public Uni<Response> validateVerification(@Valid VerificationValidateRequestDto verificationValidateRequestDto) {
        return totpService.validateVerification(verificationValidateRequestDto)
                .onItem().transform(entity -> {
                    return Response.ok()
                            .entity(GenericResponseDto.builder().data(entity).build())
                            .build();
                })
                .onFailure().recoverWithItem(ResponseFormatter::formatFailureResponse);
    }*/

}
