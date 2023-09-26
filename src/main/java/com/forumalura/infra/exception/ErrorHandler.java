package com.forumalura.infra.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity handlerError404() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handlerError400(MethodArgumentNotValidException ex) {
        var errors = ex.getFieldErrors();

        return ResponseEntity.badRequest().body(errors.stream().map(DataErrorValidation::new).toList());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity handlerErrorBusinessRule(ValidationException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity handlerErrorNotFound(NotFoundException ex) {
        return ResponseEntity.notFound().eTag(ex.getMessage()).build();
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity handlerErrorBadCredentials() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bad Credentials!");
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity handlerErrorAuthentication() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failure!");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity handlerErrorAccessDenied() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied!");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handlerError500(Exception ex) {
        return ResponseEntity.internalServerError().body("Error: " +ex.getLocalizedMessage());
    }

    private record DataErrorValidation(String field, String message) {
        public DataErrorValidation(FieldError error) {
            this(error.getField(), error.getDefaultMessage());
        }
    }
}
