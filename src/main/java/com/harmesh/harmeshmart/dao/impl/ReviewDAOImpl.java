package com.harmesh.harmeshmart.dao.impl;

import com.harmesh.harmeshmart.dao.ReviewDAO;
import com.harmesh.harmeshmart.model.Review;
import com.harmesh.harmeshmart.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of ReviewDAO.
 */
public class ReviewDAOImpl implements ReviewDAO {

    @Override
    public List<Review> findByProductId(Long productId) throws SQLException {
        String sql = "SELECT r.*, u.name as user_name FROM reviews r " +
                "JOIN users u ON r.user_id = u.id " +
                "WHERE r.product_id = ? ORDER BY r.created_at DESC";
        List<Review> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Review r = new Review();
                    r.setId(rs.getLong("id"));
                    r.setProductId(rs.getLong("product_id"));
                    r.setUserId(rs.getLong("user_id"));
                    r.setRating(rs.getInt("rating"));
                    r.setComment(rs.getString("comment"));
                    r.setCreatedAt(rs.getTimestamp("created_at"));
                    r.setUserName(rs.getString("user_name"));
                    list.add(r);
                }
            }
        }
        return list;
    }

    @Override
    public Review save(Review review) throws SQLException {
        String sql = "INSERT INTO reviews (product_id, user_id, rating, comment, created_at) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, review.getProductId());
            ps.setLong(2, review.getUserId());
            ps.setInt(3, review.getRating());
            ps.setString(4, review.getComment());
            Timestamp now = new Timestamp(System.currentTimeMillis());
            ps.setTimestamp(5, now);

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    review.setId(rs.getLong(1));
                    review.setCreatedAt(now);
                }
            }
        }
        return review;
    }

    @Override
    public boolean hasUserPurchasedProduct(Long userId, Long productId) throws SQLException {
        String sql = "SELECT 1 FROM orders o " +
                "JOIN order_items oi ON o.id = oi.order_id " +
                "WHERE o.buyer_id = ? AND oi.product_id = ? LIMIT 1";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setLong(2, productId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
