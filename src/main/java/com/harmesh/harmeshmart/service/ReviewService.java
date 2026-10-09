package com.harmesh.harmeshmart.service;

import com.harmesh.harmeshmart.exception.AppException;
import com.harmesh.harmeshmart.model.Review;

import java.util.List;

/**
 * Service for customer product reviews and star ratings.
 */
public interface ReviewService {

    List<Review> getReviewsForProduct(Long productId) throws AppException;

    Review addReview(Long userId, Long productId, int rating, String comment) throws AppException;
}
