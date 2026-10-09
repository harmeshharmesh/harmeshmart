package com.harmesh.harmeshmart.model;

/**
 * Order status lifecycle workflow:
 * PENDING -> CONFIRMED -> SHIPPED -> DELIVERED (or CANCELLED)
 */
public enum OrderStatus {
    PENDING,
    CONFIRMED,
    SHIPPED,
    DELIVERED,
    CANCELLED;

    public static OrderStatus fromString(String statusStr) {
        if (statusStr == null) return PENDING;
        try {
            return OrderStatus.valueOf(statusStr.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return PENDING;
        }
    }
}
