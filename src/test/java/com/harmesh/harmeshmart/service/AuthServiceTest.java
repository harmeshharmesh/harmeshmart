package com.harmesh.harmeshmart.service;

import com.harmesh.harmeshmart.dao.SellerDAO;
import com.harmesh.harmeshmart.dao.UserDAO;
import com.harmesh.harmeshmart.exception.AppException;
import com.harmesh.harmeshmart.exception.ValidationException;
import com.harmesh.harmeshmart.model.Role;
import com.harmesh.harmeshmart.model.User;
import com.harmesh.harmeshmart.service.impl.AuthServiceImpl;
import com.harmesh.harmeshmart.util.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * JUnit 5 + Mockito Service test verifying business validation logic.
 * Conforms to Section 9 Testing & Validation Requirements.
 */
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserDAO userDAO;

    @Mock
    private SellerDAO sellerDAO;

    private AuthService authService;

    @BeforeEach
    public void init() {
        authService = new AuthServiceImpl(userDAO, sellerDAO);
    }

    @Test
    @DisplayName("Should throw ValidationException when logging in with invalid email format")
    public void testLoginInvalidEmailFormat() {
        assertThrows(ValidationException.class, () -> {
            authService.login("not-an-email", "secretPassword");
        });
        verifyNoInteractions(userDAO);
    }

    @Test
    @DisplayName("Should successfully authenticate valid user credentials")
    public void testLoginSuccess() throws Exception {
        String rawPass = "Buyer@12345";
        String hashed = PasswordUtil.hashPassword(rawPass);
        User mockUser = new User();
        mockUser.setId(10L);
        mockUser.setEmail("buyer@harmeshmart.com");
        mockUser.setPasswordHash(hashed);
        mockUser.setRole(Role.BUYER);

        when(userDAO.findByEmail("buyer@harmeshmart.com")).thenReturn(Optional.of(mockUser));

        User loggedIn = authService.login("buyer@harmeshmart.com", rawPass);
        assertNotNull(loggedIn);
        assertEquals(10L, loggedIn.getId());
        assertEquals("buyer@harmeshmart.com", loggedIn.getEmail());
    }

    @Test
    @DisplayName("Should prevent registration with duplicate email")
    public void testRegisterDuplicateEmail() throws SQLException {
        User existing = new User();
        existing.setEmail("existing@harmeshmart.com");

        when(userDAO.findByEmail("existing@harmeshmart.com")).thenReturn(Optional.of(existing));

        assertThrows(AppException.class, () -> {
            authService.register("Existing User", "existing@harmeshmart.com", "Password@123", "+91 9999999999", Role.BUYER);
        });

        verify(userDAO, never()).save(any());
    }
}
