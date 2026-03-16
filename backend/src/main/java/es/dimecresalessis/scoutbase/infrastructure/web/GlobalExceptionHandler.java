package es.dimecresalessis.scoutbase.infrastructure.web;

import es.dimecresalessis.scoutbase.infrastructure.web.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.access.AccessDeniedException;
import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<String>> handleException(RuntimeException ex) {
        logException(ex);
        return buildErrorResponse(ex.getMessage(), ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleException(Exception ex) {
        logException(ex);
        return buildErrorResponse(ex.getMessage(), ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<String>> handleAccessDenied(AccessDeniedException ex) {
        logException(ex);
        return buildErrorResponse(ex.getMessage(), ex, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<String>> handleException(AuthenticationException ex) {
        logException(ex);
        return buildErrorResponse(ex.getMessage(), ex, HttpStatus.UNAUTHORIZED);
    }

    private void logException(Exception ex) {
        logger.error("Exception thrown: {}", ex.getMessage());
    }

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