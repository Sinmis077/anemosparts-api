package dev.ioannis.anemosparts.config;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.naming.AuthenticationException;
import javax.security.auth.login.AccountNotFoundException;
import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class ExceptionConfiguration {
    private static final Logger log = LoggerFactory.getLogger(ExceptionConfiguration.class);

    public record ErrorResponse(String message) {}

    // 400 - Bad Request - Failed validation
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> illegalArgumentExceptionHandler(IllegalArgumentException e) {
        log.warn("Bad request: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
    }

    // 400 - Bad Request - Jakarta validation failed
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(ex -> ex.getField() + ": " + ex.getDefaultMessage())
                .orElse("Validation failed");

        log.warn("Validation failed: {}", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(message));
    }

    // 401 - Unauthorized - User is not logged in
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> authenticationExceptionHandler(AuthenticationException e) {
        log.warn("Authentication failed: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(e.getMessage()));
    }

    // 403 - Forbidden - User is logged in but does not have the necessary permissions
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> accessDeniedExceptionHandler(AccessDeniedException e) {
        log.warn("Access denied: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(e.getMessage()));
    }

    // 404 - Not found - Client request could not be fulfilled due to missing an entity in the database
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> entityNotFoundExceptionHandler(EntityNotFoundException e) {
        log.warn("Entity not found: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ErrorResponse> handleEntityExists(EntityExistsException e) {
        log.warn("Entity already exists: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(e.getMessage()));
    }

    // 409 - Conflict - Violation of business or database rules
    @ExceptionHandler({IllegalStateException.class, DataIntegrityViolationException.class})
    public ResponseEntity<ErrorResponse> conflictExceptionHandler(Exception e) {
        String message = e instanceof DataIntegrityViolationException
                ? "Database rule violation"
                : e.getMessage();

        log.warn("Conflict: {}", message);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(message));
    }

    // 413 - Payload Too Large - Given content is too large (images)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> maxUploadSizeExceptionHandler(MaxUploadSizeExceededException e) {
        log.warn("Max file size exceeded: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException e) {
        log.debug("Failed attempt to login because of incorrect credentials: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("Invalid credentials"));
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAccountDoesntExist(AccountNotFoundException e) {
        log.debug("Failed attempt to login because of a missing account: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("Invalid credentials"));
    }

    // Last resort / general catch-all
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralErrors(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("An unexpected error occurred"));
    }
}