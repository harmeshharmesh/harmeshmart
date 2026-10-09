package com.harmesh.harmeshmart.dao;

import com.harmesh.harmeshmart.model.Review;

import java.sql.SQLException;
import java.util.List;

/**
 * Data Access Object for Product Reviews and Ratings.
 */
public interface ReviewDAO {

    List<Review> findByProductId(Long productId) throws SQLException;

    Review save(Review review) throws SQLException;

    boolean hasUserPurchasedProduct(Long userId, Long productId) throws SQLException;
}
