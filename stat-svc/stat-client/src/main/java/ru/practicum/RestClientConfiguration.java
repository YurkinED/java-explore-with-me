package ru.practicum;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestClientConfiguration {

    // First Method: default
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    // Second Method: Using RestTemplateBuilder
    @Bean
    public org.springframework.web.client.RestTemplate restTemplate() {
        return restTemplate(null);
    }

    // Second Method: Using RestTemplateBuilder
    @Bean
    public org.springframework.web.client.RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}