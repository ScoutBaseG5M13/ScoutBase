package es.dimecresalessis.scoutbase.infrastructure.web;

import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.player.exception.PlayerException;
import es.dimecresalessis.scoutbase.domain.stat.exception.StatException;
import es.dimecresalessis.scoutbase.domain.user.exception.UserException;
import es.dimecresalessis.scoutbase.infrastructure.web.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

/**
 * Global interceptor for unhandled exceptions across the REST API.
 * <p>
 * It acts as a specialized adapter that translates internal system failures (Domain or Application exceptions)
 * into a standardized {@link ApiResponse} format for external clients.
 * </p>
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles custom Scoutbase Player failures.
     * @param ex The caught {@link PlayerException}.
     * @return A response with serialized error details depending on the error.
     */
    @ExceptionHandler(PlayerException.class)
    public ResponseEntity<ApiResponse<String>> handlePlayerException(PlayerException ex) {
        logException(ex);
        if (ex.getErrorEnum().equals(ErrorEnum.PLAYER_NOT_FOUND)) {
            return buildErrorResponse(ex.getMessage(), ex, HttpStatus.NOT_FOUND);
        }
        return buildErrorResponse(ex.getMessage(), ex, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles custom Scoutbase User failures.
     * @param ex The caught {@link UserException}.
     * @return A response with serialized error details depending on the error.
     */
    @ExceptionHandler(UserException.class)
    public ResponseEntity<ApiResponse<String>> handleUserException(UserException ex) {
        logException(ex);
        if (ex.getErrorEnum().equals(ErrorEnum.USER_NOT_FOUND)) {
            return buildErrorResponse(ex.getMessage(), ex, HttpStatus.NOT_FOUND);
        }
        return buildErrorResponse(ex.getMessage(), ex, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles custom Scoutbase Stat failures.
     * @param ex The caught {@link StatException}.
     * @return A response with serialized error details depending on the error.
     */
    @ExceptionHandler(StatException.class)
    public ResponseEntity<ApiResponse<String>> handleStatException(StatException ex) {
        logException(ex);
        return buildErrorResponse(ex.getMessage(), ex, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles custom MethodArgumentNotValidException failures.
     * @param ex The caught {@link MethodArgumentNotValidException}.
     * @return A response with serialized error details depending on the error.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .findFirst()
                .orElse("Validation failed");

        return buildErrorResponse(errorMessage, ex, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles general runtime logic failures.
     * @param ex The caught {@link RuntimeException}.
     * @return A {@code 400 Bad Request} response with serialized error details.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<String>> handleException(RuntimeException ex) {
        logException(ex);
        return buildErrorResponse(ex.getMessage(), ex, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles BadCredentialsException failures.
     * @param ex The caught {@link BadCredentialsException}.
     * @return A {@code 401 Unauthorized} response with serialized error details.
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<String>> handleBadCredentialsException(BadCredentialsException ex) {
        logException(ex);
        String userMessage = "Your session has expired or is invalid. Please, log in again";
        return buildErrorResponse(userMessage, ex, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Catch-all handler for checked exceptions not covered by specific methods.
     * @param ex The caught {@link Exception}.
     * @return A {@code 500 Internal Server Error} response.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleException(Exception ex) {
        logException(ex);
        return buildErrorResponse(ex.getMessage(), ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Intercepts authorization failures when a user has insufficient permissions.
     * @param ex The {@link AccessDeniedException} thrown by the security context.
     * @return A {@code 403 Forbidden} response.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<String>> handleAccessDenied(AccessDeniedException ex) {
        logException(ex);
        return buildErrorResponse(ex.getMessage(), ex, HttpStatus.FORBIDDEN);
    }

    /**
     * Handles authentication-specific failures, such as invalid or expired credentials.
     * @param ex The {@link AuthenticationException} thrown during the filter chain.
     * @return A {@code 401 Unauthorized} response.
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<String>> handleException(AuthenticationException ex) {
        logException(ex);
        return buildErrorResponse(ex.getMessage(), ex, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        logException(ex);
        return buildErrorResponse(ex.getMessage(), ex, HttpStatus.NOT_FOUND);
    }

    /**
     * Internal utility to log exception messages via the configured logger.
     * @param ex The exception to log.
     */
    private void logException(Exception ex) {
        logger.error("Exception thrown: {}", ex.getMessage());
    }

    /**
     * Transforms an exception into a unified API response structure.
     * <p>
     * Includes the {@code sessionId} from the {@link MDC} (Mapped Diagnostic Context)
     * to facilitate log correlation and tracing in the infrastructure layer.
     * </p>
     * * @param message The descriptive error message.
     * @param ex The exception being processed.
     * @param status The target HTTP status.
     * @return A {@link ResponseEntity} containing the standardized {@link ApiResponse}.
     */
    private ResponseEntity<ApiResponse<String>> buildErrorResponse(String message, Exception ex, HttpStatus status) {
        ApiResponse<String> response = new ApiResponse<>(
                false,
                message,
                ex.getClass().getSimpleName(),
                MDC.get("sessionId"),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, status);
    }
}