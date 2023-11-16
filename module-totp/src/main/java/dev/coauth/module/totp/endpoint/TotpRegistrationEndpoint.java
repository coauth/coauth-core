package dev.coauth.module.totp.endpoint;


import dev.coauth.module.totp.dto.*;
import dev.coauth.module.totp.service.TotpService;
import dev.coauth.module.totp.utils.ResponseFormatter;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/module/totp/register")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TotpRegistrationEndpoint {

    @Inject
    TotpService totpService;

    @POST
    @Path("/generate-request")
    public Uni<Response> generate(@Valid RegisterGenerateRequestDto registerGenerateRequestDto) {
        System.out.println(registerGenerateRequestDto.getAppId());
        return totpService.generateSecret(registerGenerateRequestDto)
                .onItem().transform(entity -> {
                    return Response.ok()
                            .entity(GenericResponseDto.builder().data(entity).build())
                            .build();
                })
                .onFailure().recoverWithItem(ResponseFormatter::formatFailureResponse);
    }

    @POST
    @Path("/view")
    public Uni<Response> view(@Valid RegisterViewRequestDto registerViewRequestDto) {
        return totpService.generateQR(registerViewRequestDto)
                .onItem().transform(entity -> {
                    return Response.ok()
                            .entity(GenericResponseDto.builder().data(entity).build())
                            .build();
                })
                .onFailure().recoverWithItem(ResponseFormatter::formatFailureResponse);
    }

    @POST
    @Path("/authenticate")
    public Uni<Response> save(@Valid RegisterSaveRequestDto registerSaveRequestDto) {
        return totpService.save(registerSaveRequestDto)
                .onItem().transform(entity -> {
                    return Response.ok()
                            .entity(GenericResponseDto.builder().data(entity).build())
                            .build();
                })
                .onFailure().recoverWithItem(ResponseFormatter::formatFailureResponse);
    }

    @POST
    @Path("/validate")
    public Uni<Response> verify(@Valid RegisterVerifyRequestDto registerVerifyRequestDto) {
        return totpService.verify(registerVerifyRequestDto)
                .onItem().transform(entity -> {
                    return Response.ok()
                            .entity(GenericResponseDto.builder().data(entity).build())
                            .build();
                })
                .onFailure().recoverWithItem(ResponseFormatter::formatFailureResponse);
    }
}
