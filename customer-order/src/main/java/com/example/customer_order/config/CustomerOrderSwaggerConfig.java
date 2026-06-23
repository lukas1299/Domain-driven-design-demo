package com.example.customer_order.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomerOrderSwaggerConfig {

    @Bean
    public OpenAPI customerOrderOpenApiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("System Obsługi Zamówień - API")
                        .description("Dokumentacja techniczna modułu Customer Order (DDD + Hexagonal Architecture)")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Java Developer")
                                .email("developer@example.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://springdoc.org")));
    }

    @Bean
    public GroupedOpenApi customerOrderGroupApi() {
        return GroupedOpenApi.builder()
                .group("1. Customer Order Module")
                .pathsToMatch("/api/orders/**")
                .build();
    }
}