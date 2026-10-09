package com.harmesh.harmeshmart.service.impl;

import com.harmesh.harmeshmart.dao.DAOFactory;
import com.harmesh.harmeshmart.dao.SellerDAO;
import com.harmesh.harmeshmart.dao.UserDAO;
import com.harmesh.harmeshmart.exception.AppException;
import com.harmesh.harmeshmart.exception.ValidationException;
import com.harmesh.harmeshmart.model.Role;
import com.harmesh.harmeshmart.model.Seller;
import com.harmesh.harmeshmart.model.User;
import com.harmesh.harmeshmart.service.AuthService;
import com.harmesh.harmeshmart.util.PasswordUtil;
import com.harmesh.harmeshmart.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Implementation of AuthService adhering to SOLID principles.
 */
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final UserDAO userDAO;
    private final SellerDAO sellerDAO;

    public AuthServiceImpl() {
        this(DAOFactory.getUserDAO(), DAOFactory.getSellerDAO());
    }

    public AuthServiceImpl(UserDAO userDAO, SellerDAO sellerDAO) {
        this.userDAO = userDAO;
        this.sellerDAO = sellerDAO;
    }

    @Override
    public User login(String email, String password) throws AppException {
        ValidationUtil.validateEmail(email);
        ValidationUtil.validateRequired(password, "password");

        try {
            Optional<User> userOpt = userDAO.findByEmail(email.trim());
            if (userOpt.isEmpty()) {
                throw new AppException("INVALID_CREDENTIALS", "Invalid email or password.");
            }

            User user = userOpt.get();
            if (!PasswordUtil.checkPassword(password, user.getPasswordHash())) {
                throw new AppException("INVALID_CREDENTIALS", "Invalid email or password.");
            }

            return user;
        } catch (SQLException e) {
            logger.error("Database error during user login: {}", e.getMessage(), e);
            throw new AppException("DATABASE_ERROR", "Internal system error during login. Please try again.");
        }
    }

    @Override
    public User register(String name, String email, String password, String phone, Role role) throws AppException {
        ValidationUtil.validateRequired(name, "name");
        ValidationUtil.validateEmail(email);
        ValidationUtil.validatePassword(password);

        try {
            Optional<User> existing = userDAO.findByEmail(email.trim());
            if (existing.isPresent()) {
                throw new AppException("CONFLICT", "An account with this email address already exists.");
            }

            String hashed = PasswordUtil.hashPassword(password);
            User user = new User();
            user.setName(name.trim());
            user.setEmail(email.trim().toLowerCase());
            user.setPasswordHash(hashed);
            user.setRole(role != null ? role : Role.BUYER);
            user.setPhone(phone != null ? phone.trim() : "");

            return userDAO.save(user);
        } catch (SQLException e) {
            logger.error("Database error during registration: {}", e.getMessage(), e);
            throw new AppException("DATABASE_ERROR", "Could not complete registration.");
        }
    }

    @Override
    public Seller becomeSeller(Long userId, String storeName, String storeDescription, String phone) throws AppException {
        ValidationUtil.validateRequired(storeName, "storeName");

        try {
            Optional<User> userOpt = userDAO.findById(userId);
            if (userOpt.isEmpty()) {
                throw new AppException("NOT_FOUND", "User account not found.");
            }

            User user = userOpt.get();

            Optional<Seller> existingSeller = sellerDAO.findByUserId(userId);
            if (existingSeller.isPresent()) {
                return existingSeller.get();
            }

            Seller seller = new Seller();
            seller.setUserId(userId);
            seller.setStoreName(storeName.trim());
            seller.setStoreDescription(storeDescription != null ? storeDescription.trim() : "");
            seller.setContactEmail(user.getEmail());
            seller.setPhone(phone != null && !phone.trim().isEmpty() ? phone.trim() : user.getPhone());
            seller.setVerified(true);
            seller.setRating(new BigDecimal("5.00"));

            Seller created = sellerDAO.save(seller);

            // Elevate user role to SELLER if currently BUYER
            if (user.getRole() == Role.BUYER) {
                user.setRole(Role.SELLER);
                userDAO.update(user);
            }

            return created;
        } catch (SQLException e) {
            logger.error("Database error during seller onboarding: {}", e.getMessage(), e);
            throw new AppException("DATABASE_ERROR", "Could not complete seller registration.");
        }
    }

    @Override
    public Optional<User> getUserById(Long id) throws AppException {
        try {
            return userDAO.findById(id);
        } catch (SQLException e) {
            logger.error("Database error finding user by ID: {}", e.getMessage(), e);
            throw new AppException("DATABASE_ERROR", "Could not fetch user details.");
        }
    }

    @Override
    public Optional<Seller> getSellerByUserId(Long userId) throws AppException {
        try {
            return sellerDAO.findByUserId(userId);
        } catch (SQLException e) {
            logger.error("Database error finding seller by user ID: {}", e.getMessage(), e);
            throw new AppException("DATABASE_ERROR", "Could not fetch seller details.");
        }
    }
}
