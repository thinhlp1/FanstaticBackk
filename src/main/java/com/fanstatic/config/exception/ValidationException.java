package com.fanstatic.config.exception;

import java.util.List;

import org.springframework.validation.FieldError;

public class ValidationException extends RuntimeException {
    private final List<FieldError> errors;

    public ValidationException(List<FieldError> errors) {
        // super("Validation failed");
        this.errors = errors;
    }

    public List<FieldError> getErrors() {
        return errors;
    }
}
