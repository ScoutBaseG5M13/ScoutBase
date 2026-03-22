package es.dimecresalessis.scoutbase.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * Infrastructure configuration for the OpenAPI 3.0 (Swagger) documentation.
 * <p>
 * Defines global metadata for the ScoutBase API, including
 * the versioning, description, and the security schemes required for
 * authorized requests.
 * </p>
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "ScoutBase API",
                version = "v0.2",
                description = "API documentation for ScoutBase"
        )
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {}