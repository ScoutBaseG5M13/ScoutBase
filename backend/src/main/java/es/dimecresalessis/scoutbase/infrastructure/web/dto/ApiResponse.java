package es.dimecresalessis.scoutbase.infrastructure.web.dto;

import java.time.LocalDateTime;

/**
 * Universal wrapper for all REST API responses.
 * <p>
 * It provides a consistent JSON structure for both successful data payloads and error
 * messages, facilitating easier integration for frontend and other consumers.
 * </p>
 *
 * @param <T> The type of the data payload being returned.
 * @param success Indicates if the operation was processed without business errors.
 * @param message A descriptive string explaining the result (e.g., "Player created").
 * @param data The actual payload of the response (can be null for errors or void actions).
 * @param sessionId A unique trace identifier for the request, sourced from {@code SessionFilter}.
 * @param timestamp The exact date and time the response was generated.
 */
public record ApiResponse<T>(
        boolean success,
        String message,
        T data,
        String sessionId,
        LocalDateTime timestamp
) {
    /**
     * Compact constructor for the ApiResponse record.
     * <p>
     * Ensures that every response is timestamped and linked to a session
     * for observability and debugging purposes.
     * </p>
     */
    public ApiResponse {
        // Validation or default logic could be placed here
    }
}