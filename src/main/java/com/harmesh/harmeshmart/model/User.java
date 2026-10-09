package com.harmesh.harmeshmart.model;

import java.sql.Timestamp;

/**
 * User entity representing buyers, sellers, and administrators.
 */
public class User {
    private Long id;
    private String name;
    private String email;
    private String passwordHash;
    private Role role;
    private String phone;
    private Timestamp createdAt;

    public User() {
    }

    public User(Long id, String name, String email, String passwordHash, Role role, String phone, Timestamp createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.phone = phone;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isBuyer() {
        return role == Role.BUYER;
    }

    public boolean isSeller() {
        return role == Role.SELLER;
    }

    public boolean isAdmin() {
        return role == Role.ADMIN;
    }
}
