package com.harmesh.harmeshmart.service;

import com.harmesh.harmeshmart.dto.CartSummaryDTO;
import com.harmesh.harmeshmart.exception.AppException;
import com.harmesh.harmeshmart.model.CartItem;

import java.util.List;

/**
 * Service for shopping cart operations, live subtotal calculations, and promo code discounts.
 */
public interface CartService {

    List<CartItem> getCartItems(Long userId) throws AppException;

    CartSummaryDTO getCartSummary(Long userId, String promoCode) throws AppException;

    void addToCart(Long userId, Long productId, int quantity) throws AppException;

    void updateQuantity(Long cartItemId, Long userId, int quantity) throws AppException;

    void removeFromCart(Long cartItemId, Long userId) throws AppException;

    void clearCart(Long userId) throws AppException;

    int getCartCount(Long userId) throws AppException;
}
