package es.dimecresalessis.scoutbase.infrastructure.web.dto;

import java.time.LocalDateTime;

public class ResponseHandler<T> {
    private final T data;

    public ResponseHandler(T data) {
        this.data = data;
    }

    public ApiResponse<T> ok() {
        return new ApiResponse<>(true, "Success", data, LocalDateTime.now());
    }
}