package com.mzc.lms.organization.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Organization Service API")
                        .description("Organization management service for LMS - Colleges, Departments, Academic Terms")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("LMS Team")
                                .email("lms@mzc.com")))
                .servers(List.of(
                        new Server().url("/").description("Default Server")
                ));
    }
}
