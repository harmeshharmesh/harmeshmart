package com.harmesh.harmeshmart.exception;

/**
 * Thrown when a requested resource does not exist.
 */
public class NotFoundException extends AppException {
    public NotFoundException(String message) {
        super("NOT_FOUND", message);
    }
}
