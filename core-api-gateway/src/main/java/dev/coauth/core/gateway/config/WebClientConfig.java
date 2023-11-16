package dev.coauth.core.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${coauth.core.auth-guard.url}")
    String coauthAuthGuardUrl;

    @Bean
    WebClient client() {
        return WebClient.builder().baseUrl(coauthAuthGuardUrl)
                .build();
    }

}