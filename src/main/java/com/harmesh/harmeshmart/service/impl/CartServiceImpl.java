package com.harmesh.harmeshmart.service.impl;

import com.harmesh.harmeshmart.dao.CartDAO;
import com.harmesh.harmeshmart.dao.DAOFactory;
import com.harmesh.harmeshmart.dao.ProductDAO;
import com.harmesh.harmeshmart.dto.CartSummaryDTO;
import com.harmesh.harmeshmart.exception.AppException;
import com.harmesh.harmeshmart.exception.ValidationException;
import com.harmesh.harmeshmart.model.CartItem;
import com.harmesh.harmeshmart.model.Product;
import com.harmesh.harmeshmart.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of CartService with Amazon-style shipping and coupon logic.
 */
public class CartServiceImpl implements CartService {

    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);
    private final CartDAO cartDAO;
    private final ProductDAO productDAO;

    public CartServiceImpl() {
        this(DAOFactory.getCartDAO(), DAOFactory.getProductDAO());
    }

    public CartServiceImpl(CartDAO cartDAO, ProductDAO productDAO) {
        this.cartDAO = cartDAO;
        this.productDAO = productDAO;
    }

    @Override
    public List<CartItem> getCartItems(Long userId) throws AppException {
        try {
            return cartDAO.findByUserId(userId);
        } catch (SQLException e) {
            logger.error("Error retrieving cart for user {}: {}", userId, e.getMessage(), e);
            throw new AppException("DATABASE_ERROR", "Failed to retrieve cart items.");
        }
    }

    @Override
    public CartSummaryDTO getCartSummary(Long userId, String promoCode) throws AppException {
        List<CartItem> items = getCartItems(userId);
        CartSummaryDTO summary = new CartSummaryDTO();

        int totalCount = 0;
        BigDecimal subtotal = BigDecimal.ZERO;

        for (CartItem it : items) {
            totalCount += it.getQuantity();
            BigDecimal itemTotal = it.getItemTotal();
            subtotal = subtotal.add(itemTotal);

            CartSummaryDTO.CartItemDetailDTO detail = new CartSummaryDTO.CartItemDetailDTO();
            detail.setCartItemId(it.getId());
            detail.setProductId(it.getProductId());
            detail.setQuantity(it.getQuantity());
            detail.setItemTotal(itemTotal);
            if (it.getProduct() != null) {
                detail.setProductName(it.getProduct().getName());
                detail.setImageUrl(it.getProduct().getImageUrl());
                detail.setUnitPrice(it.getProduct().getPrice());
                detail.setStockQty(it.getProduct().getStockQty());
            }
            summary.getItems().add(detail);
        }

        summary.setTotalItems(totalCount);
        summary.setSubtotal(subtotal);

        // Free shipping if subtotal >= 999 or cart is empty
        BigDecimal shipping = BigDecimal.ZERO;
        if (subtotal.compareTo(BigDecimal.ZERO) > 0 && subtotal.compareTo(new BigDecimal("999.00")) < 0) {
            shipping = new BigDecimal("99.00");
        }
        summary.setShipping(shipping);

        // Promo code discount: SAVE10 gives 10% discount up to ₹500
        BigDecimal discount = BigDecimal.ZERO;
        if (promoCode != null && "SAVE10".equalsIgnoreCase(promoCode.trim())) {
            summary.setPromoCode("SAVE10");
            discount = subtotal.multiply(new BigDecimal("0.10")).setScale(2, java.math.RoundingMode.HALF_UP);
            if (discount.compareTo(new BigDecimal("500.00")) > 0) {
                discount = new BigDecimal("500.00");
            }
        }
        summary.setDiscount(discount);

        BigDecimal grandTotal = subtotal.add(shipping).subtract(discount);
        if (grandTotal.compareTo(BigDecimal.ZERO) < 0) {
            grandTotal = BigDecimal.ZERO;
        }
        summary.setGrandTotal(grandTotal);

        return summary;
    }

    @Override
    public void addToCart(Long userId, Long productId, int quantity) throws AppException {
        if (quantity <= 0) quantity = 1;
        try {
            Optional<Product> prodOpt = productDAO.findById(productId);
            if (prodOpt.isEmpty() || !prodOpt.get().isActive()) {
                throw new AppException("NOT_FOUND", "Product is unavailable.");
            }

            Product product = prodOpt.get();
            if (product.getStockQty() <= 0) {
                throw new AppException("OUT_OF_STOCK", "Product is currently out of stock.");
            }

            Optional<CartItem> existing = cartDAO.findByUserAndProduct(userId, productId);
            if (existing.isPresent()) {
                CartItem item = existing.get();
                int newQty = Math.min(item.getQuantity() + quantity, product.getStockQty());
                cartDAO.updateQuantity(item.getId(), newQty);
            } else {
                CartItem item = new CartItem(userId, productId, Math.min(quantity, product.getStockQty()));
                cartDAO.save(item);
            }
        } catch (SQLException e) {
            logger.error("Error adding to cart for user {}: {}", userId, e.getMessage(), e);
            throw new AppException("DATABASE_ERROR", "Failed to add product to cart.");
        }
    }

    @Override
    public void updateQuantity(Long cartItemId, Long userId, int quantity) throws AppException {
        try {
            if (quantity <= 0) {
                cartDAO.delete(cartItemId, userId);
            } else {
                cartDAO.updateQuantity(cartItemId, quantity);
            }
        } catch (SQLException e) {
            logger.error("Error updating cart item {} quantity: {}", cartItemId, e.getMessage(), e);
            throw new AppException("DATABASE_ERROR", "Failed to update item quantity.");
        }
    }

    @Override
    public void removeFromCart(Long cartItemId, Long userId) throws AppException {
        try {
            cartDAO.delete(cartItemId, userId);
        } catch (SQLException e) {
            logger.error("Error removing cart item {}: {}", cartItemId, e.getMessage(), e);
            throw new AppException("DATABASE_ERROR", "Failed to remove item from cart.");
        }
    }

    @Override
    public void clearCart(Long userId) throws AppException {
        try {
            cartDAO.clearCart(userId);
        } catch (SQLException e) {
            logger.error("Error clearing cart for user {}: {}", userId, e.getMessage(), e);
            throw new AppException("DATABASE_ERROR", "Failed to clear cart.");
        }
    }

    @Override
    public int getCartCount(Long userId) throws AppException {
        try {
            return cartDAO.getCartCount(userId);
        } catch (SQLException e) {
            logger.error("Error counting cart items for user {}: {}", userId, e.getMessage(), e);
            return 0;
        }
    }
}
