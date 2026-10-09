package com.harmesh.harmeshmart.service.impl;

import com.harmesh.harmeshmart.dao.CartDAO;
import com.harmesh.harmeshmart.dao.DAOFactory;
import com.harmesh.harmeshmart.dao.WishlistDAO;
import com.harmesh.harmeshmart.exception.AppException;
import com.harmesh.harmeshmart.model.CartItem;
import com.harmesh.harmeshmart.model.WishlistItem;
import com.harmesh.harmeshmart.service.WishlistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of WishlistService.
 */
public class WishlistServiceImpl implements WishlistService {

    private static final Logger logger = LoggerFactory.getLogger(WishlistServiceImpl.class);
    private final WishlistDAO wishlistDAO;
    private final CartDAO cartDAO;

    public WishlistServiceImpl() {
        this(DAOFactory.getWishlistDAO(), DAOFactory.getCartDAO());
    }

    public WishlistServiceImpl(WishlistDAO wishlistDAO, CartDAO cartDAO) {
        this.wishlistDAO = wishlistDAO;
        this.cartDAO = cartDAO;
    }

    @Override
    public List<WishlistItem> getWishlist(Long userId) throws AppException {
        try {
            return wishlistDAO.findByUserId(userId);
        } catch (SQLException e) {
            logger.error("Error retrieving wishlist for user {}: {}", userId, e.getMessage(), e);
            throw new AppException("DATABASE_ERROR", "Failed to retrieve wishlist.");
        }
    }

    @Override
    public void addToWishlist(Long userId, Long productId) throws AppException {
        try {
            wishlistDAO.add(userId, productId);
        } catch (SQLException e) {
            logger.error("Error adding to wishlist for user {}: {}", userId, e.getMessage(), e);
            throw new AppException("DATABASE_ERROR", "Failed to add to wishlist.");
        }
    }

    @Override
    public void removeFromWishlist(Long userId, Long productId) throws AppException {
        try {
            wishlistDAO.remove(userId, productId);
        } catch (SQLException e) {
            logger.error("Error removing from wishlist for user {}: {}", userId, e.getMessage(), e);
            throw new AppException("DATABASE_ERROR", "Failed to remove from wishlist.");
        }
    }

    @Override
    public void moveToCart(Long userId, Long productId) throws AppException {
        try {
            Optional<CartItem> existing = cartDAO.findByUserAndProduct(userId, productId);
            if (existing.isPresent()) {
                cartDAO.updateQuantity(existing.get().getId(), existing.get().getQuantity() + 1);
            } else {
                cartDAO.save(new CartItem(userId, productId, 1));
            }
            wishlistDAO.remove(userId, productId);
        } catch (SQLException e) {
            logger.error("Error moving wishlist item to cart for user {}: {}", userId, e.getMessage(), e);
            throw new AppException("DATABASE_ERROR", "Failed to move item to cart.");
        }
    }

    @Override
    public int getWishlistCount(Long userId) throws AppException {
        try {
            return wishlistDAO.getWishlistCount(userId);
        } catch (SQLException e) {
            logger.error("Error counting wishlist for user {}: {}", userId, e.getMessage(), e);
            return 0;
        }
    }
}
