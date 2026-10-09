package com.harmesh.harmeshmart.service.impl;

import com.harmesh.harmeshmart.dao.DAOFactory;
import com.harmesh.harmeshmart.dao.ProductDAO;
import com.harmesh.harmeshmart.dao.ReviewDAO;
import com.harmesh.harmeshmart.exception.AppException;
import com.harmesh.harmeshmart.exception.ValidationException;
import com.harmesh.harmeshmart.model.Review;
import com.harmesh.harmeshmart.service.ReviewService;
import com.harmesh.harmeshmart.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

/**
 * Implementation of ReviewService.
 */
public class ReviewServiceImpl implements ReviewService {

    private static final Logger logger = LoggerFactory.getLogger(ReviewServiceImpl.class);
    private final ReviewDAO reviewDAO;
    private final ProductDAO productDAO;

    public ReviewServiceImpl() {
        this(DAOFactory.getReviewDAO(), DAOFactory.getProductDAO());
    }

    public ReviewServiceImpl(ReviewDAO reviewDAO, ProductDAO productDAO) {
        this.reviewDAO = reviewDAO;
        this.productDAO = productDAO;
    }

    @Override
    public List<Review> getReviewsForProduct(Long productId) throws AppException {
        try {
            return reviewDAO.findByProductId(productId);
        } catch (SQLException e) {
            logger.error("Error retrieving reviews for product {}: {}", productId, e.getMessage(), e);
            throw new AppException("DATABASE_ERROR", "Failed to retrieve product reviews.");
        }
    }

    @Override
    public Review addReview(Long userId, Long productId, int rating, String comment) throws AppException {
        ValidationUtil.validateRating(rating);
        ValidationUtil.validateRequired(comment, "comment");

        try {
            Review r = new Review(productId, userId, rating, comment.trim());
            Review saved = reviewDAO.save(r);

            // Recompute product average rating & review count
            productDAO.updateRatingAndReviewCount(productId);
            return saved;
        } catch (SQLException e) {
            logger.error("Error saving review for product {}: {}", productId, e.getMessage(), e);
            throw new AppException("DATABASE_ERROR", "Failed to submit review.");
        }
    }
}
