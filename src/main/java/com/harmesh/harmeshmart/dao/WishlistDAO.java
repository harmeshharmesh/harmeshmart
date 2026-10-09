package com.harmesh.harmeshmart.dao;

import com.harmesh.harmeshmart.model.WishlistItem;

import java.sql.SQLException;
import java.util.List;

/**
 * Data Access Object for Wishlist / Save for Later.
 */
public interface WishlistDAO {

    List<WishlistItem> findByUserId(Long userId) throws SQLException;

    boolean exists(Long userId, Long productId) throws SQLException;

    void add(Long userId, Long productId) throws SQLException;

    void remove(Long userId, Long productId) throws SQLException;

    int getWishlistCount(Long userId) throws SQLException;
}
