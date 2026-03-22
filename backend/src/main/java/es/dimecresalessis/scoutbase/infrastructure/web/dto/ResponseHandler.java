package es.dimecresalessis.scoutbase.infrastructure.web.dto;

import org.slf4j.MDC;
import java.time.LocalDateTime;

/**
 * Fluent builder for constructing standardized API responses.
 * <p>
 * It provides a convenient way to wrap data into an {@link ApiResponse} while automatically
 * injecting technical metadata such as the {@code sessionId} from the Mapped Diagnostic Context (MDC).
 * </p>
 */
public class ResponseHandler<T> {

    /**
     * The data payload to be included in the final response.
     */
    private final T data;

    /**
     * Initializes the handler with the response data.
     * @param data The payload returned by the application/domain layer.
     */
    public ResponseHandler(T data) {
        this.data = data;
    }

    /**
     * Finalizes the response as a successful (HTTP 200-range) operation.
     * <p>
     * This method retrieves the {@code sessionId} directly from the {@link MDC},
     * which should have been populated earlier in the request lifecycle by the
     * {@code SessionFilter} or a {@code HandlerInterceptor}.
     * </p>
     *
     * @return A fully populated {@link ApiResponse} marked as successful.
     */
    public ApiResponse<T> ok() {
        return new ApiResponse<>(
                true,
                "Success",
                data,
                MDC.get("sessionId"),
                LocalDateTime.now()
        );
    }

    /**
     * Finalizes the response as a successful creation (HTTP 201).
     *
     * @return A fully populated {@link ApiResponse} marked as successful.
     */
    public ApiResponse<T> created() {
        return new ApiResponse<>(
                true,
                "Created Successfully",
                data,
                MDC.get("sessionId"),
                LocalDateTime.now()
        );
    }
}