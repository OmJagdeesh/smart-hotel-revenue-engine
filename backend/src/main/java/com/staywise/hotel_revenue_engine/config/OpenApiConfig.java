package com.staywise.hotel_revenue_engine.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI stayWiseOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("StayWise Hotel Revenue Management API")
                .description("Multi-tenant SaaS API for hotel pricing, booking, and revenue analytics")
                .version("v1.0.0")
                .contact(new Contact().name("StayWise Platform Team").email("platform@staywise.io"))
                .license(new License().name("Proprietary"))
        );
    }
}
