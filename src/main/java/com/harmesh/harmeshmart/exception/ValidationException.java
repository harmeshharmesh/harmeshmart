package com.harmesh.harmeshmart.exception;

/**
 * Validation exception for field and input errors.
 */
public class ValidationException extends AppException {
    private final String field;

    public ValidationException(String message) {
        super("VALIDATION_ERROR", message);
        this.field = null;
    }

    public ValidationException(String field, String message) {
        super("VALIDATION_ERROR", message);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
