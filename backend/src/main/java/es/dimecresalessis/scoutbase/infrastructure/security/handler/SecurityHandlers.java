package es.dimecresalessis.scoutbase.infrastructure.security.handler;

import es.dimecresalessis.scoutbase.infrastructure.security.SecurityResponseBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

/**
 * Unified security handler for managing authentication and authorization failures.
 * <p>
 * It acts as a bridge between the Spring Security filter chain and the application's
 * standardized response format by delegating to a {@link SecurityResponseBuilder}.
 * </p>
 */
@Component
@RequiredArgsConstructor
public class SecurityHandlers implements AuthenticationEntryPoint, AccessDeniedHandler {

    /**
     * Infrastructure utility used to construct and write the standardized JSON
     * error response to the {@link HttpServletResponse}.
     */
    private final SecurityResponseBuilder responseBuilder;

    /**
     * Triggered when an unauthenticated user attempts to access a protected resource.
     * <p>
     * Responds with a {@code 401 Unauthorized} status, signaling that the
     * request lacks valid authentication credentials.
     * </p>
     *
     * @param request The current {@link HttpServletRequest}.
     * @param response The {@link HttpServletResponse} to be populated.
     * @param authException The specific exception that triggered the entry point.
     * @throws IOException If an input or output exception occurs while writing the response.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        responseBuilder.buildResponse(request, response, HttpServletResponse.SC_UNAUTHORIZED, "Autenticación requerida.");
    }

    /**
     * Triggered when an authenticated user lacks the required permissions/roles.
     * <p>
     * Responds with a {@code 403 Forbidden} status, signaling that the user's
     * identity is known, but they are restricted from the specific resource.
     * </p>
     *
     * @param request               The current {@link HttpServletRequest}.
     * @param response              The {@link HttpServletResponse} to be populated.
     * @param accessDeniedException The specific exception representing the authorization failure.
     * @throws IOException If an input or output exception occurs while writing the response.
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        responseBuilder.buildResponse(request, response, HttpServletResponse.SC_FORBIDDEN, "Acceso denegado.");
    }
}