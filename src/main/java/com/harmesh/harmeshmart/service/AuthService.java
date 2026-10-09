package com.harmesh.harmeshmart.service;

import com.harmesh.harmeshmart.exception.AppException;
import com.harmesh.harmeshmart.exception.ValidationException;
import com.harmesh.harmeshmart.model.Role;
import com.harmesh.harmeshmart.model.Seller;
import com.harmesh.harmeshmart.model.User;

import java.util.Optional;

/**
 * Service handling authentication, registration, and RBAC onboarding.
 */
public interface AuthService {

    /**
     * Authenticate user credentials.
     *
     * @param email User email
     * @param password Cleartext candidate password
     * @return Authenticated User object
     * @throws AppException If invalid credentials or account issue
     */
    User login(String email, String password) throws AppException;

    /**
     * Register a new user account.
     *
     * @param name Full name
     * @param email Email address
     * @param password Raw password
     * @param phone Phone number
     * @param role User role (BUYER or SELLER)
     * @return Newly registered User
     * @throws AppException On validation or duplicate email conflict
     */
    User register(String name, String email, String password, String phone, Role role) throws AppException;

    /**
     * Upgrade an existing user to Seller status.
     *
     * @param userId User primary key
     * @param storeName Store business name
     * @param storeDescription Merchant description
     * @param phone Merchant phone
     * @return Created Seller profile
     * @throws AppException On validation error or existing seller profile
     */
    Seller becomeSeller(Long userId, String storeName, String storeDescription, String phone) throws AppException;

    /**
     * Find user by primary key ID.
     *
     * @param id User ID
     * @return Optional user
     * @throws AppException On retrieval error
     */
    Optional<User> getUserById(Long id) throws AppException;

    /**
     * Find seller profile by user ID.
     *
     * @param userId User ID
     * @return Optional seller profile
     * @throws AppException On retrieval error
     */
    Optional<Seller> getSellerByUserId(Long userId) throws AppException;
}
