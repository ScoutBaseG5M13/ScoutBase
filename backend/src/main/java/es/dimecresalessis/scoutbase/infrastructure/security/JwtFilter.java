package es.dimecresalessis.scoutbase.infrastructure.security;

import es.dimecresalessis.scoutbase.application.user.FindUserByUsernameUseCase;
import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.infrastructure.security.handler.SecurityHandlers;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

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
    private final FindUserByUsernameUseCase findUserByUsernameUseCase;
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

        try {
            if (authHeader != null && authHeader.startsWith(bearer)) {
                String token = authHeader.substring(bearer.length());
                String username = jwtService.extractUsername(token);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    User user = findUserByUsernameUseCase.execute(username);
                    validateToken(token, user);
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            Collections.emptyList()
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request, response);
        } catch (SignatureException ex) {
            securityHandlers.commence(request, response, new BadCredentialsException("Your session token is invalid"));
        } catch (ExpiredJwtException ex) {
            securityHandlers.commence(request, response, new BadCredentialsException("Your session token has expired"));
        } catch (UsernameNotFoundException ex) {
            securityHandlers.commence(request, response, new BadCredentialsException("The user associated with this token does not exist"));
        } catch (Exception ex) {
            securityHandlers.commence(request, response, new BadCredentialsException("An error occurred while processing your request"));
        }
    }

    private void validateToken(String token, User user) throws SignatureException, ExpiredJwtException {
        if (!jwtService.isTokenValid(token, user)) {
            throw new SignatureException(null);
        }

        if (jwtService.isTokenExpired(token)) {
            throw new ExpiredJwtException(null, null, null);
        }
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