package dev.coauth.core.gateway.filter;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.coauth.core.gateway.dto.CoreAppAuthMstrProperties;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;


@Slf4j
public class RequestBodyRewrite implements RewriteFunction<String, String> {

    private final CoreAppAuthMstrProperties coreAppAuthMstrProperties;
    RequestBodyRewrite(CoreAppAuthMstrProperties coreAppAuthMstrProperties) {
        this.coreAppAuthMstrProperties=coreAppAuthMstrProperties;
    }
                       @Override
    public Publisher<String> apply(ServerWebExchange exchange, String body) {
        try {
            System.out.println("Original Body: " + body);

            System.out.println("coreAppAuthMstrPropertiescoreAppAuthMstrProperties " + coreAppAuthMstrProperties);

            // Example: Convert the JSON body to a Map and modify it
            Map<String, Object> map = convertJsonToMap(body);
            map.put("appDetails", coreAppAuthMstrProperties);

            String modifiedBody = convertMapToJson(map);
            System.out.println("Modified Body: " + modifiedBody);

            return Mono.just(modifiedBody);
        } catch (Exception ex) {
            ex.printStackTrace();

            // Throw a RuntimeException for simplicity; handle as needed in your actual implementation
            throw new IllegalStateException("An error occurred while transforming the request body in class RequestBodyRewrite.");
        }
    }

    private static Map<String, Object> convertJsonToMap(String jsonString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonString, new TypeReference<>() {});
    }

    private static String convertMapToJson(Map<String, Object> map) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(map);
    }
}
