package com.mzc.lms.enrollment.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${server.servlet.context-path:/api/v1}")
    private String contextPath;

    @Bean
    public OpenAPI enrollmentServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Enrollment Service API")
                        .description("LMS 수강 신청 서비스 API - 수강 신청, 확정, 철회, 완료 관리")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("MZC LMS Team")
                                .email("lms@mzc.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8086" + contextPath)
                                .description("Local Development Server"),
                        new Server()
                                .url("https://api.lms.mzc.com" + contextPath)
                                .description("Production Server")
                ));
    }
}
