package dev.coauth.core.gateway.exception;

import dev.coauth.core.gateway.dto.IbGenericResponse;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@Order(-2)
public class CustomError extends AbstractErrorWebExceptionHandler {

    public CustomError(
            ErrorAttributes errorAttributes,
            ApplicationContext applicationContext,
            ServerCodecConfigurer serverCodecConfigurer) {
        super(errorAttributes, new WebProperties.Resources(), applicationContext);
        super.setMessageWriters(serverCodecConfigurer.getWriters());
        super.setMessageReaders(serverCodecConfigurer.getReaders());
    }

    /**
     * To route all requests with the specific response
     * @param errorAttributes attribute value.
     * @return will return ServerResponse Object for the client side.
     */
    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes
                                                                        errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(),
                this::renderErrorResponse);
    }

    /**
     * To build response and set them using MONO, and Render Error Response
     * @param request has a request value.
     * @return will return Mono ServerResponse.
     */
    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {

        // To represent Error Attribute as a Map.
        Map<String, Object> errorPropertiesMap = getErrorAttributes(request, ErrorAttributeOptions.defaults());

        // To fetch response status code.
        HttpStatus status = HttpStatus.valueOf((Integer) errorPropertiesMap.get("status"));

        IbGenericResponse response;
        if (status.name().equals(HttpStatus.NOT_FOUND.name())) {

            // To handle any Not Found request.
            response = IbGenericResponse.builder().errorCode("-4" ).results(status.name())
                    .errorDescription("Not found").build();

        } else {
            // To handle any InternalServer.
            response = IbGenericResponse
                            .builder()
                            .errorCode("-1" )
                            .errorDescription("")
                            .results(status.name())
                            .build();
        }

        // return your server response after building it.
        return ServerResponse.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(response));
    }
}