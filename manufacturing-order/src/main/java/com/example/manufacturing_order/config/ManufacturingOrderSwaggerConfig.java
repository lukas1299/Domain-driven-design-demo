package com.example.manufacturing_order.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ManufacturingOrderSwaggerConfig {

    @Bean
    public OpenAPI manufacturingOrderOpenApiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("Manufacturing Order - API")
                        .description("Dokumentacja techniczna modułu Manufacturing Order (DDD + Hexagonal Architecture)")
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
                .group("manufacturing-order")
                .pathsToMatch("/v1/manufacturing-orders/**", "/v1/products/**")
                .build();
    }
}