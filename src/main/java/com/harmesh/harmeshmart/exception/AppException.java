package com.harmesh.harmeshmart.exception;

/**
 * Base custom application checked exception.
 */
public class AppException extends Exception {
    private final String errorCode;

    public AppException(String message) {
        super(message);
        this.errorCode = "APP_ERROR";
    }

    public AppException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public AppException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
