package org.coauth.core.application.rest.totp.controller;

import org.coauth.core.application.rest.totp.json.GenericRequest;
import org.coauth.core.application.rest.totp.json.StatusResponse;
import org.coauth.core.application.rest.totp.mappers.TOtpRequestResponseDtoMapper;
import org.coauth.core.application.rest.validator.AbstractValidationHandler;
import org.coauth.core.commons.auth.config.FetchPrincipalComponent;
import org.coauth.core.domain.auth.dto.UserDto;
import org.coauth.core.domain.totp.dto.TOtpUserStatusDto;
import org.coauth.core.domain.totp.ports.api.TOtpUserServiceAPI;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class TOtpStatusHandler extends AbstractValidationHandler<GenericRequest, Validator> {

  private final TOtpUserServiceAPI tOtpUserServiceAPI;

  private final FetchPrincipalComponent fetchPrincipalComponent;

  TOtpStatusHandler(
      Validator validator,
      TOtpUserServiceAPI tOtpUserServiceAPI,
      FetchPrincipalComponent fetchPrincipalComponent) {
    super(GenericRequest.class, validator);
    this.tOtpUserServiceAPI = tOtpUserServiceAPI;
    this.fetchPrincipalComponent = fetchPrincipalComponent;
  }

  @Override
  public Mono<ServerResponse> processBody(
      GenericRequest genericRequest, ServerRequest serverRequest) {
    return fetchPrincipalComponent
        .getAuthDetails()
        .flatMap(user -> callService(user, genericRequest))
        .flatMap(this::processSuccessResponse)
        .switchIfEmpty(processEmpty());
  }

  private Mono<TOtpUserStatusDto> callService(UserDto user, GenericRequest genericRequest) {
    return tOtpUserServiceAPI.getUserStatus(
        user.getSystemId().trim(), genericRequest.getUserId().trim());
  }

  private Mono<ServerResponse> processSuccessResponse(TOtpUserStatusDto tOtpUserStatusDto) {
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(
            BodyInserters.fromValue(
                TOtpRequestResponseDtoMapper.INSTANCE.statusResponseFromDto(tOtpUserStatusDto)));
  }

  private Mono<ServerResponse> processEmpty() {
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(
            BodyInserters.fromValue(
                StatusResponse.builder()
                    .statusCode("300")
                    .statusDescription("No active subscription")
                    .build()));
  }
}
