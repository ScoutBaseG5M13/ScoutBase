package es.dimecresalessis.scoutbase.infrastructure.web.annotation;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
})
public @interface ApiCommonResponses {
}
