package es.dimecresalessis.scoutbase.infrastructure.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.UUID;

/**
 * Early-stage HTTP filter for session identification and tracing.
 * <p>
 * It ensures that every incoming request is associated with a unique session identifier, either by
 * capturing an existing {@code X-Session-ID} header or generating a new one.
 * </p>
 * <p>
 * Unlike an Interceptor, this filter runs at the Servlet container level, ensuring
 * the identifier is available even if the request is rejected by security filters
 * before reaching a Controller.
 * </p>
 */
@Component
public class SessionFilter extends OncePerRequestFilter {

    /**
     * Intercepts the request to inject a session identifier into the request attributes.
     *
     * By setting the `sessionId` as a request attribute, it becomes accessible
     * to all subsequent filters, controllers, and error handlers in the chain.
     *
     * @param request The incoming [HttpServletRequest].
     * @param response The outgoing [HttpServletResponse].
     * @param filterChain The chain of filters to execute.
     * @throws ServletException If a filter-related error occurs.
     * @throws IOException If an I/O error occurs during processing.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String sessionId = request.getHeader("X-Session-ID");
        if (sessionId == null) {
            sessionId = UUID.randomUUID().toString();
        }

        // Stores the ID as a request attribute for availability throughout the chain
        request.setAttribute("sessionId", sessionId);

        filterChain.doFilter(request, response);
    }
}