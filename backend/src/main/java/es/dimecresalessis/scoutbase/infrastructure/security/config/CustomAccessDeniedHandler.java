package es.dimecresalessis.scoutbase.infrastructure.security.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

/**
 * Custom handler for authorization failures (HTTP 403 Forbidden).
 * <p>
 * It is triggered by Spring Security when an authenticated user attempts to access a protected
 * resource without the necessary authorities.
 * </p>
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    /**
     * Intercepts an authorization failure and writes a standardized JSON error message
     * directly to the HTTP response.
     * <p>
     * Unlike the {@code GlobalExceptionHandler}, this handler operates at the
     * Security Filter Chain level, before the request ever reaches a Controller.
     * </p>
     *
     * @param request The {@link HttpServletRequest} that caused the failure.
     * @param response The {@link HttpServletResponse} to be populated.
     * @param accessDeniedException The specific exception representing the failure.
     * @throws IOException If an input or output exception occurs while writing the response.
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");
        response.getWriter().write("{\"message\": \"Access Denied: You do not have the required permissions.\"}");
    }
}