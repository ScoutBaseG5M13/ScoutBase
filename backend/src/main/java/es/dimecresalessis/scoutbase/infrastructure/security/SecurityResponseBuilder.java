package es.dimecresalessis.scoutbase.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.dimecresalessis.scoutbase.infrastructure.web.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Infrastructure utility for constructing standardized security error responses.
 * <p>
 * It is responsible for manually serializing {@link ApiResponse} objects into the {@link HttpServletResponse}
 * when a security failure occurs within the Filter Chain (before reaching a Controller).
 * </p>
 */
@Component
public class SecurityResponseBuilder {

    /**
     * Dedicated JSON mapper configured for security-level response serialization.
     */
    private final ObjectMapper mapper;

    /**
     * Constructs the builder and configures the internal {@link ObjectMapper}.
     * <p>
     * Includes the {@link JavaTimeModule} and disables timestamp serialization
     * to ensure that {@link LocalDateTime} fields are formatted as ISO-8601 strings.
     * </p>
     */
    public SecurityResponseBuilder() {
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    /**
     * Writes a standardized JSON response to the client.
     * <p>
     * It retrieves the {@code sessionId} from the request attributes (set by
     * {@code SessionFilter}) to maintain traceability even in failed
     * authentication attempts.
     * </p>
     *
     * @param request The current {@link HttpServletRequest} to extract attributes from.
     * @param response The {@link HttpServletResponse} where the JSON will be written.
     * @param status The HTTP status code (e.g., 401, 403).
     * @param message The localized error message to include in the response body.
     * @throws IOException If an error occurs during JSON serialization or writing to the response stream.
     */
    public void buildResponse(HttpServletRequest request, HttpServletResponse response, int status, String message) throws IOException {
        String sessionId = (String) request.getAttribute("sessionId");

        ApiResponse<Void> apiResponse = new ApiResponse<>(
                false, message, null, sessionId, LocalDateTime.now()
        );
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write(mapper.writeValueAsString(apiResponse));
    }
}