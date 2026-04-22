package es.dimecresalessis.scoutbase.infrastructure.web.dto;

/**
 * Factory for initiating a fluent API response construction.
 * <p>
 * It acts as the entry point for creating standardized {@link ApiResponse} objects,
 * abstracting away the boilerplate of record instantiation.
 * </p>
 */
public class ResponseFactory {

    /**
     * Starts the creation of a response by wrapping the provided data.
     * <p>
     * This method returns a {@link ResponseHandler}, allowing the chain of further calls to
     * define the message and status before generating the final {@link ApiResponse}.
     * </p>
     *
     * @param <T> The type of the data payload.
     * @param data The payload to be included in the response.
     * @return A {@link ResponseHandler} instance for fluent configuration.
     */
    public static <T> ResponseHandler<T> handleResponse(T data) {
        return new ResponseHandler<>(data);
    }
}