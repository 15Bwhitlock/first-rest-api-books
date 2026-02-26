package com.brayden.firstrestapibooks.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice // tells Spring this class handles exceptions globally
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<?> handleException(ApiException ex) {
        return new ResponseEntity<>(ErrorResponse
                .builder()
                .error(ex.getMessage())
                .build(),
                ex.getHttpStatus());
    }
}
