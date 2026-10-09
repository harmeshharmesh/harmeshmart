package com.harmesh.harmeshmart.dao.impl;

import com.harmesh.harmeshmart.dao.WishlistDAO;
import com.harmesh.harmeshmart.model.Product;
import com.harmesh.harmeshmart.model.WishlistItem;
import com.harmesh.harmeshmart.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of WishlistDAO.
 */
public class WishlistDAOImpl implements WishlistDAO {

    @Override
    public List<WishlistItem> findByUserId(Long userId) throws SQLException {
        String sql = "SELECT w.*, p.name as product_name, p.price, p.original_price, p.image_url, p.stock_qty, p.category, p.rating " +
                "FROM wishlist_items w JOIN products p ON w.product_id = p.id " +
                "WHERE w.user_id = ? ORDER BY w.id DESC";
        List<WishlistItem> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    WishlistItem item = new WishlistItem();
                    item.setId(rs.getLong("id"));
                    item.setUserId(rs.getLong("user_id"));
                    item.setProductId(rs.getLong("product_id"));
                    item.setCreatedAt(rs.getTimestamp("created_at"));

                    Product p = new Product();
                    p.setId(rs.getLong("product_id"));
                    p.setName(rs.getString("product_name"));
                    p.setPrice(rs.getBigDecimal("price"));
                    p.setOriginalPrice(rs.getBigDecimal("original_price"));
                    p.setImageUrl(rs.getString("image_url"));
                    p.setStockQty(rs.getInt("stock_qty"));
                    p.setCategory(rs.getString("category"));
                    p.setRating(rs.getBigDecimal("rating"));
                    item.setProduct(p);

                    list.add(item);
                }
            }
        }
        return list;
    }

    @Override
    public boolean exists(Long userId, Long productId) throws SQLException {
        String sql = "SELECT 1 FROM wishlist_items WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setLong(2, productId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public void add(Long userId, Long productId) throws SQLException {
        if (exists(userId, productId)) return;
        String sql = "INSERT INTO wishlist_items (user_id, product_id, created_at) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setLong(2, productId);
            ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            ps.executeUpdate();
        }
    }

    @Override
    public void remove(Long userId, Long productId) throws SQLException {
        String sql = "DELETE FROM wishlist_items WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setLong(2, productId);
            ps.executeUpdate();
        }
    }

    @Override
    public int getWishlistCount(Long userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM wishlist_items WHERE user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }
}
