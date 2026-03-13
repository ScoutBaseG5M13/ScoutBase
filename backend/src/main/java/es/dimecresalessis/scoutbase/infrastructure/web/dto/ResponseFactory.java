package es.dimecresalessis.scoutbase.infrastructure.web.dto;

public class ResponseFactory {

    public static <T> ResponseHandler<T> handleResponse(T data) {
        return new ResponseHandler<>(data);
    }
}
