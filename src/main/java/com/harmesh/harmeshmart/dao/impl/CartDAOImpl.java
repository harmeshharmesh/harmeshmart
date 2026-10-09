package com.harmesh.harmeshmart.dao.impl;

import com.harmesh.harmeshmart.dao.CartDAO;
import com.harmesh.harmeshmart.model.CartItem;
import com.harmesh.harmeshmart.model.Product;
import com.harmesh.harmeshmart.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC implementation of CartDAO.
 */
public class CartDAOImpl implements CartDAO {

    private CartItem mapRow(ResultSet rs) throws SQLException {
        CartItem item = new CartItem();
        item.setId(rs.getLong("id"));
        item.setUserId(rs.getLong("user_id"));
        item.setProductId(rs.getLong("product_id"));
        item.setQuantity(rs.getInt("quantity"));
        item.setCreatedAt(rs.getTimestamp("created_at"));

        Product p = new Product();
        p.setId(rs.getLong("product_id"));
        p.setName(rs.getString("product_name"));
        p.setPrice(rs.getBigDecimal("price"));
        p.setOriginalPrice(rs.getBigDecimal("original_price"));
        p.setImageUrl(rs.getString("image_url"));
        p.setStockQty(rs.getInt("stock_qty"));
        p.setCategory(rs.getString("category"));
        item.setProduct(p);

        return item;
    }

    @Override
    public List<CartItem> findByUserId(Long userId) throws SQLException {
        String sql = "SELECT c.*, p.name as product_name, p.price, p.original_price, p.image_url, p.stock_qty, p.category " +
                "FROM cart_items c JOIN products p ON c.product_id = p.id " +
                "WHERE c.user_id = ? ORDER BY c.id DESC";
        List<CartItem> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    @Override
    public Optional<CartItem> findByUserAndProduct(Long userId, Long productId) throws SQLException {
        String sql = "SELECT c.*, p.name as product_name, p.price, p.original_price, p.image_url, p.stock_qty, p.category " +
                "FROM cart_items c JOIN products p ON c.product_id = p.id " +
                "WHERE c.user_id = ? AND c.product_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setLong(2, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public CartItem save(CartItem item) throws SQLException {
        String sql = "INSERT INTO cart_items (user_id, product_id, quantity, created_at) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, item.getUserId());
            ps.setLong(2, item.getProductId());
            ps.setInt(3, item.getQuantity());
            Timestamp now = new Timestamp(System.currentTimeMillis());
            ps.setTimestamp(4, now);

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    item.setId(rs.getLong(1));
                    item.setCreatedAt(now);
                }
            }
        }
        return item;
    }

    @Override
    public void updateQuantity(Long cartItemId, int quantity) throws SQLException {
        String sql = "UPDATE cart_items SET quantity = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setLong(2, cartItemId);
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Long cartItemId, Long userId) throws SQLException {
        String sql = "DELETE FROM cart_items WHERE id = ? AND user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, cartItemId);
            ps.setLong(2, userId);
            ps.executeUpdate();
        }
    }

    @Override
    public void clearCart(Long userId) throws SQLException {
        String sql = "DELETE FROM cart_items WHERE user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.executeUpdate();
        }
    }

    @Override
    public int getCartCount(Long userId) throws SQLException {
        String sql = "SELECT COALESCE(SUM(quantity), 0) FROM cart_items WHERE user_id = ?";
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
