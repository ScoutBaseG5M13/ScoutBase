package es.dimecresalessis.scoutbase.infrastructure.web;

import es.dimecresalessis.scoutbase.infrastructure.web.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> handleException(RuntimeException ex) {
        logger.error("{}: {}", ex.getClass().getSimpleName() , ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception ex) {
        logger.error("Exception thrown: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ApiResponse<Void>> buildErrorResponse(String message, HttpStatus status) {
        ApiResponse<Void> response = new ApiResponse<>(
                false,
                message,
                null,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, status);
    }
}