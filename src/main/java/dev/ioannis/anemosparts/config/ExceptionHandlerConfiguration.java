package dev.ioannis.anemosparts.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.PayloadTooLargeException;

import javax.naming.ServiceUnavailableException;

@RestControllerAdvice
public class ExceptionHandlerConfiguration {
    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(HttpClientErrorException.BadRequest ex) {
        return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(HttpClientErrorException.NotFound ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleServiceUnavailable(ServiceUnavailableException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(PayloadTooLargeException.class)
    public ResponseEntity<ErrorResponse> handlePayloadTooLarge(PayloadTooLargeException ex) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(HttpClientErrorException.Conflict.class)
    public ResponseEntity<ErrorResponse> handleHttpClientError(HttpClientErrorException.Conflict ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(ex.getMessage()));
    }

    // Last resort / general catch-all
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralErrors(Exception ex) {
        System.err.println("ERROR caused by: " + ex.getCause());
        System.err.println(ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(ex.getMessage()));
    }
}