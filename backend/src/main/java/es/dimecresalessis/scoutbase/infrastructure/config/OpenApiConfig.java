package es.dimecresalessis.scoutbase.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.annotation.PostConstruct;
import org.springdoc.core.utils.SpringDocUtils;
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
public class OpenApiConfig {

        /**
         * This stops Swagger from creating "ApiResponseObject", "ApiResponseBoolean", etc.
         * It forces it to use the base ApiResponse schema and just swap the 'data' field.
         */
        @PostConstruct
        public void init() {
                SpringDocUtils.getConfig().replaceWithClass(
                        es.dimecresalessis.scoutbase.infrastructure.web.dto.ApiResponse.class,
                        es.dimecresalessis.scoutbase.infrastructure.web.dto.ApiResponse.class
                );
        }
}