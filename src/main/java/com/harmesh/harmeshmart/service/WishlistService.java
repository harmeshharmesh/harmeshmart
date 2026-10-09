package com.harmesh.harmeshmart.service;

import com.harmesh.harmeshmart.exception.AppException;
import com.harmesh.harmeshmart.model.WishlistItem;

import java.util.List;

/**
 * Service for buyer wishlist / save for later.
 */
public interface WishlistService {

    List<WishlistItem> getWishlist(Long userId) throws AppException;

    void addToWishlist(Long userId, Long productId) throws AppException;

    void removeFromWishlist(Long userId, Long productId) throws AppException;

    void moveToCart(Long userId, Long productId) throws AppException;

    int getWishlistCount(Long userId) throws AppException;
}
