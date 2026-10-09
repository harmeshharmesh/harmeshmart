package com.harmesh.harmeshmart.exception;

/**
 * Thrown when an unauthenticated or unauthorized action is attempted.
 */
public class UnauthorizedException extends AppException {
    public UnauthorizedException(String message) {
        super("UNAUTHORIZED", message);
    }
}
