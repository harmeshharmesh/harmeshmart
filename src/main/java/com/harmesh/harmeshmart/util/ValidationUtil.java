package com.harmesh.harmeshmart.util;

import com.harmesh.harmeshmart.exception.ValidationException;

import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * Validation utilities conforming to Section 13 point 5:
 * "Validation occurs at the top of the service method. Return HTTP 400 with a field-level error before any DAO call."
 */
public final class ValidationUtil {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    private ValidationUtil() {
    }

    public static void validateRequired(String value, String fieldName) throws ValidationException {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(fieldName, fieldName + " is required and cannot be empty.");
        }
    }

    public static void validateEmail(String email) throws ValidationException {
        validateRequired(email, "email");
        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new ValidationException("email", "Invalid email format: " + email);
        }
    }

    public static void validatePassword(String password) throws ValidationException {
        validateRequired(password, "password");
        if (password.length() < 6) {
            throw new ValidationException("password", "Password must be at least 6 characters long.");
        }
    }

    public static void validatePositivePrice(BigDecimal price, String fieldName) throws ValidationException {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException(fieldName, fieldName + " must be a positive value greater than zero.");
        }
    }

    public static void validateNonNegativeInt(Integer val, String fieldName) throws ValidationException {
        if (val == null || val < 0) {
            throw new ValidationException(fieldName, fieldName + " must be zero or a positive integer.");
        }
    }

    public static void validateRating(Integer rating) throws ValidationException {
        if (rating == null || rating < 1 || rating > 5) {
            throw new ValidationException("rating", "Rating must be between 1 and 5 stars.");
        }
    }
}
