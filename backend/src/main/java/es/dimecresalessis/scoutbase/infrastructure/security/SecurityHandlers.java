package es.dimecresalessis.scoutbase.infrastructure.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SecurityHandlers implements AuthenticationEntryPoint, AccessDeniedHandler {

    private final SecurityResponseBuilder responseBuilder;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        responseBuilder.buildResponse(request, response, HttpServletResponse.SC_UNAUTHORIZED, "Autenticación requerida.");
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        responseBuilder.buildResponse(request, response, HttpServletResponse.SC_FORBIDDEN, "Acceso denegado.");
    }
}