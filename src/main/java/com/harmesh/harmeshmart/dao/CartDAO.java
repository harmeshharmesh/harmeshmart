package com.harmesh.harmeshmart.dao;

import com.harmesh.harmeshmart.model.CartItem;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for Shopping Cart.
 */
public interface CartDAO {

    List<CartItem> findByUserId(Long userId) throws SQLException;

    Optional<CartItem> findByUserAndProduct(Long userId, Long productId) throws SQLException;

    CartItem save(CartItem item) throws SQLException;

    void updateQuantity(Long cartItemId, int quantity) throws SQLException;

    void delete(Long cartItemId, Long userId) throws SQLException;

    void clearCart(Long userId) throws SQLException;

    int getCartCount(Long userId) throws SQLException;
}
