package com.mzc.backend.lms.common.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
	@Value("${server.port}")
	private String port;
	
	@Bean
	public OpenAPI reservationPricingOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("LMS_PROJECT")
						.description("LMS_PROJECT_API")
						.version("v1.0.0")
						.contact(new Contact()
								.name("Team 1")
								.email("ddingsha9@teambind.co.kr")))
				.servers(List.of(
						new Server()
								.url("http://localhost:" + port)
								.description("Local Development Server"),
						new Server()
								.url("https://api.example.com")
								.description("Production Server")
				));
	}
}
