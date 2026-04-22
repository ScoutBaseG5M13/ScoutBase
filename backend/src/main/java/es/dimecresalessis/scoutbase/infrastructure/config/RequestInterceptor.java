package es.dimecresalessis.scoutbase.infrastructure.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

/**
 * HTTP Request Interceptor responsible for managing diagnostic context.
 */
@Component
public class RequestInterceptor implements HandlerInterceptor {

    /**
     * Pre-processes every incoming HTTP request before it reaches the Controller.
     * <p>
     * It looks for an {@code X-Session-ID} header. If missing, it generates a
     * new {@link UUID}. This ID is then stored in the {@link MDC} under the key
     * {@code "sessionId"}, making it available to all loggers during the
     * execution of the current thread.
     * </p>
     *
     * @param request The current {@link HttpServletRequest}.
     * @param response The current {@link HttpServletResponse}.
     * @param handler The chosen handler to execute, for type and/or instance evaluation.
     * @return {@code true} to continue the execution chain.
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String sessionId = request.getHeader("X-Session-ID");
        if (sessionId == null) {
            sessionId = UUID.randomUUID().toString();
        }
        MDC.put("sessionId", sessionId);
        return true;
    }

    /**
     * Cleans up the diagnostic context after the request is fully processed.
     * <p>
     * This ensures that the {@code sessionId} does not leak into other threads
     * within the thread pool, preventing log pollution.
     * </p>
     *
     * @param request The current {@link HttpServletRequest}.
     * @param response The current {@link HttpServletResponse}.
     * @param handler The handler that was executed.
     * @param ex Any exception thrown on handler execution, if any.
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        MDC.clear();
    }
}