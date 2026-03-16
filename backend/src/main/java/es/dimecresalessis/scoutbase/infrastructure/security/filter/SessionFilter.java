package es.dimecresalessis.scoutbase.infrastructure.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.UUID;

@Component
public class SessionFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String sessionId = request.getHeader("X-Session-ID");
        if (sessionId == null) {
            sessionId = UUID.randomUUID().toString();
        }
        // Lo ponemos como atributo para que esté disponible en toda la cadena
        request.setAttribute("sessionId", sessionId);

        filterChain.doFilter(request, response);
    }
}