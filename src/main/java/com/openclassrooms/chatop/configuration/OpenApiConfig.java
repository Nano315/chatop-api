package com.openclassrooms.chatop.configuration;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

/**
 * Swagger documentation: every route requires the JWT by default ("Authorize" button in Swagger UI).
 */
@Configuration
@OpenAPIDefinition(
		info = @Info(
				title = "ChâTop API",
				version = "1.0.0",
				description = "REST API of the ChâTop rental portal. Get a token with /api/auth/login "
						+ "or /api/auth/register, then click Authorize and paste it."),
		security = @SecurityRequirement(name = OpenApiConfig.BEARER_AUTH))
@SecurityScheme(name = OpenApiConfig.BEARER_AUTH, type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
public class OpenApiConfig {

	public static final String BEARER_AUTH = "bearerAuth";

}
