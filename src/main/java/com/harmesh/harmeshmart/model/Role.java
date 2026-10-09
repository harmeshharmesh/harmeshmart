package com.harmesh.harmeshmart.model;

/**
 * User roles in HarmeshMart multi-vendor platform.
 */
public enum Role {
    BUYER,
    SELLER,
    ADMIN;

    public static Role fromString(String roleStr) {
        if (roleStr == null) return BUYER;
        try {
            return Role.valueOf(roleStr.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return BUYER;
        }
    }
}
