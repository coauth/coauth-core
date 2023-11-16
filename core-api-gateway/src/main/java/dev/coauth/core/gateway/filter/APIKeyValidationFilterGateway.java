package dev.coauth.core.gateway.filter;


import dev.coauth.core.gateway.dto.AuthGuardResponseDto;
import dev.coauth.core.gateway.dto.CoreAppAuthMstrProperties;
import dev.coauth.core.utils.ApplicationConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyRequestBodyGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class APIKeyValidationFilterGateway extends AbstractGatewayFilterFactory<APIKeyValidationFilterGateway.Config> {

    @Autowired
    private ModifyRequestBodyGatewayFilterFactory modifyRequestBodyFilter;

    private final WebClient client;

    public APIKeyValidationFilterGateway(WebClient client) {
        super(Config.class);
        this.client = client;
    }

    private Mono<? extends Void> handleAuthorizationError(ClientResponse response, ServerWebExchange exchange) {
        // Handle the unauthorized error here
        exchange.getResponse().setStatusCode(response.statusCode());
        return Mono.error(new RuntimeException("Unauthorized")); // You can customize the exception type
    }

    private Mono<CoreAppAuthMstrProperties> isAuthorizationValid(String authorizationHeader, ServerWebExchange exchange) {
        System.out.println("HERE NEXT");
        if(authorizationHeader==null || authorizationHeader.trim().isEmpty()){
            return Mono.error(new IllegalStateException("Unauthorized"));
        }
        return Mono.defer(() -> client.get()
                .uri(ApplicationConstants.COAUTH_API_KEY_ENDPOINT)
                .header(ApplicationConstants.COAUTH_API_KEY_HEADER_NAME, authorizationHeader)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        response -> handleAuthorizationError(response, exchange)
                                .then(Mono.error(new IllegalStateException("Unauthorized")))
                )
                .bodyToMono(AuthGuardResponseDto.class)
                .map(authGuardResponseDto -> {
                    System.out.println("BODY:"+authGuardResponseDto.getData());
                    // Process the response body here
                    return authGuardResponseDto.getData(); // Return true or false based on your validation logic
                }));
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }


    @Override
    public GatewayFilter apply(Config config) {
        System.out.println("HERE STARTED");
        return (exchange, chain) -> {
            try {
                ServerHttpRequest request = exchange.getRequest();

                String authorizationHeader = request.getHeaders().getFirst(ApplicationConstants.COAUTH_API_KEY_HEADER_NAME);
                System.out.println("authorizationHeader:"+authorizationHeader);
                return isAuthorizationValid(authorizationHeader, exchange)
                        .flatMap(coreAppAuthMstrProperties -> {
                            System.out.println(coreAppAuthMstrProperties);
                            // If the API key is valid, proceed with the filter chain
                            return modifyRequestBodyFilter
                                    .apply(new ModifyRequestBodyGatewayFilterFactory.Config()
                                            .setRewriteFunction(String.class, String.class, new RequestBodyRewrite(coreAppAuthMstrProperties)))
                                    .filter(exchange, chain);
                        })
                        .onErrorResume(IllegalStateException.class, unauthorizedException -> {
                            System.out.println("Entered Illegal state");
                            return onError(exchange, "You are not allowed to proceed further", HttpStatus.UNAUTHORIZED);
                        })
                        .onErrorResume(Exception.class, e -> {
                            // Handle other exceptions
                            return onError(exchange, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
                        });
            } catch (Exception e) {
                // Handle unexpected exceptions
                return onError(exchange, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        };
    }

    public static class Config {
        // Put the configuration properties
    }
}

