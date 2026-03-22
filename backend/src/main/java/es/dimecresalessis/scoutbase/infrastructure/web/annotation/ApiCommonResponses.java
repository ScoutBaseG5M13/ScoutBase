package es.dimecresalessis.scoutbase.infrastructure.web.annotation;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Composite annotation for standardized OpenAPI documentation responses.
 * <p>
 * It centralizes the most common HTTP status codes returned by the API,
 * ensuring that Swagger documentation remains consistent across all
 * controllers while reducing code duplication.
 * </p>
 * <b>Included Responses:</b>
 * <ul>
 * <li><b>200:</b> Success - The request was processed correctly.</li>
 * <li><b>404:</b> Resource not found - The requested entity does not exist.</li>
 * <li><b>500:</b> Internal server error - An unexpected failure occurred.</li>
 * </ul>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "404", description = "Resource not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
})
public @interface ApiCommonResponses {
}