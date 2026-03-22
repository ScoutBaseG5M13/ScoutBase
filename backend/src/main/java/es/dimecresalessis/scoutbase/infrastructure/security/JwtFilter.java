package es.dimecresalessis.scoutbase.infrastructure.security;

import es.dimecresalessis.scoutbase.infrastructure.security.handler.SecurityHandlers;
import es.dimecresalessis.scoutbase.infrastructure.security.service.CustomUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import io.jsonwebtoken.security.SignatureException;

/**
 * Filter responsible for JWT authentication.
 * Intercepts every incoming HTTP request to validate the Bearer token in the Authorization header.
 * <p>If a valid token is present and the security context is empty, it authenticates
 * the user and populates the {@link SecurityContextHolder}.</p>
 */
@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final SecurityHandlers securityHandlers;

    /**
     * Performs the core filtering logic for JWT validation.
     * @param request The incoming {@link HttpServletRequest}.
     * @param response The outgoing {@link HttpServletResponse}.
     * @param filterChain The chain of filters to be executed.
     * @throws IOException If an input or output error occurs.
     * @throws ServletException If the request cannot be handled.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        String authHeader = request.getHeader("Authorization");
        String bearer = "Bearer ";

        if (authHeader != null && authHeader.startsWith(bearer)) {
            String token = authHeader.substring(bearer.length());
            try {
                String username = jwtService.extractUsername(token);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    if (jwtService.isTokenValid(token, userDetails)) {
                        UsernamePasswordAuthenticationToken auth =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails, null, userDetails.getAuthorities()
                                );
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                }
            } catch (SignatureException | ExpiredJwtException e) {
                String userMessage = "Your session has expired or is invalid. Please, log in again";
                securityHandlers.commence(request, response, new BadCredentialsException(userMessage, e));
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Determines which requests should skip this filter.
     * Specifically used to allow access to Swagger UI and API documentation without a token.
     * @param request The current {@link HttpServletRequest}.
     * @return {@code true} if the request should bypass the filter, {@code false} otherwise.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.equals("/swagger-ui.html");
    }
}