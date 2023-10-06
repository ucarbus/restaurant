package com.wolt.restaurant.infrastructure.interceptor;

import com.wolt.restaurant.application.exception.ValidationException;
import com.wolt.restaurant.application.model.response.Response;
import com.wolt.restaurant.domain.model.enumtype.ResponseStatusType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@Slf4j
public class RestControllerExceptionHandler {

    @ExceptionHandler(value = ValidationException.class)
    public ResponseEntity<Response> handleValidationException(ValidationException ex) {
        Response response = createResponse(ex.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request) {
        log.error("Malformed JSON request: ", ex);
        String error = "Malformed JSON request";
        Response response = createResponse(error);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    private Response createResponse(String errorMessage) {
        Response response = new Response();
        response.setErrorMessage(errorMessage);
        response.setStatus(ResponseStatusType.FAILURE.getValue());
        return response;
    }
}