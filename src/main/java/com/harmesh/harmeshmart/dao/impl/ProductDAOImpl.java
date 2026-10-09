package com.harmesh.harmeshmart.dao.impl;

import com.harmesh.harmeshmart.dao.ProductDAO;
import com.harmesh.harmeshmart.model.Product;
import com.harmesh.harmeshmart.util.DBUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC implementation of ProductDAO.
 * Conforms to PreparedStatement and try-with-resources rules.
 */
public class ProductDAOImpl implements ProductDAO {

    private Product mapRow(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setId(rs.getLong("id"));
        p.setSellerId(rs.getLong("seller_id"));
        p.setCategoryId(rs.getObject("category_id") != null ? rs.getLong("category_id") : null);
        p.setCategory(rs.getString("category"));
        p.setName(rs.getString("name"));
        p.setDescription(rs.getString("description"));
        p.setPrice(rs.getBigDecimal("price"));
        p.setOriginalPrice(rs.getBigDecimal("original_price"));
        p.setStockQty(rs.getInt("stock_qty"));
        p.setImageUrl(rs.getString("image_url"));
        p.setActive(rs.getBoolean("is_active"));
        p.setRating(rs.getBigDecimal("rating"));
        p.setReviewCount(rs.getInt("review_count"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        try {
            p.setSellerStoreName(rs.getString("seller_store_name"));
        } catch (SQLException ignored) {
        }
        return p;
    }

    @Override
    public Optional<Product> findById(Long id) throws SQLException {
        String sql = "SELECT p.*, s.store_name as seller_store_name FROM products p " +
                "LEFT JOIN sellers s ON p.seller_id = s.user_id WHERE p.id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Product> findAll(boolean activeOnly) throws SQLException {
        String sql = "SELECT p.*, s.store_name as seller_store_name FROM products p " +
                "LEFT JOIN sellers s ON p.seller_id = s.user_id " +
                (activeOnly ? "WHERE p.is_active = TRUE " : "") +
                "ORDER BY p.id ASC";
        List<Product> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    @Override
    public List<Product> findByCategory(String category) throws SQLException {
        String sql = "SELECT p.*, s.store_name as seller_store_name FROM products p " +
                "LEFT JOIN sellers s ON p.seller_id = s.user_id " +
                "WHERE p.is_active = TRUE AND LOWER(p.category) = LOWER(?) ORDER BY p.id ASC";
        List<Product> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, category);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<Product> findBySeller(Long sellerId) throws SQLException {
        String sql = "SELECT p.*, s.store_name as seller_store_name FROM products p " +
                "LEFT JOIN sellers s ON p.seller_id = s.user_id " +
                "WHERE p.seller_id = ? ORDER BY p.id DESC";
        List<Product> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, sellerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<Product> search(String keyword, String category, BigDecimal minPrice, BigDecimal maxPrice, String sortBy) throws SQLException {
        StringBuilder sql = new StringBuilder(
                "SELECT p.*, s.store_name as seller_store_name FROM products p " +
                "LEFT JOIN sellers s ON p.seller_id = s.user_id WHERE p.is_active = TRUE ");
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (LOWER(p.name) LIKE ? OR LOWER(p.description) LIKE ? OR LOWER(p.category) LIKE ?) ");
            String term = "%" + keyword.trim().toLowerCase() + "%";
            params.add(term);
            params.add(term);
            params.add(term);
        }

        if (category != null && !category.trim().isEmpty() && !category.equalsIgnoreCase("all")) {
            sql.append("AND (LOWER(p.category) = LOWER(?) OR LOWER(p.category) LIKE ?) ");
            params.add(category.trim());
            params.add("%" + category.trim().toLowerCase() + "%");
        }

        if (minPrice != null) {
            sql.append("AND p.price >= ? ");
            params.add(minPrice);
        }

        if (maxPrice != null) {
            sql.append("AND p.price <= ? ");
            params.add(maxPrice);
        }

        if ("price_asc".equalsIgnoreCase(sortBy)) {
            sql.append("ORDER BY p.price ASC");
        } else if ("price_desc".equalsIgnoreCase(sortBy)) {
            sql.append("ORDER BY p.price DESC");
        } else if ("rating".equalsIgnoreCase(sortBy)) {
            sql.append("ORDER BY p.rating DESC, p.review_count DESC");
        } else if ("newest".equalsIgnoreCase(sortBy)) {
            sql.append("ORDER BY p.created_at DESC");
        } else {
            sql.append("ORDER BY p.id ASC");
        }

        List<Product> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    @Override
    public Product save(Product p) throws SQLException {
        String sql = "INSERT INTO products (seller_id, category_id, category, name, description, price, original_price, " +
                "stock_qty, image_url, is_active, rating, review_count, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, p.getSellerId());
            if (p.getCategoryId() != null) ps.setLong(2, p.getCategoryId());
            else ps.setNull(2, Types.BIGINT);
            ps.setString(3, p.getCategory());
            ps.setString(4, p.getName());
            ps.setString(5, p.getDescription());
            ps.setBigDecimal(6, p.getPrice());
            ps.setBigDecimal(7, p.getOriginalPrice() != null ? p.getOriginalPrice() : p.getPrice());
            ps.setInt(8, p.getStockQty());
            ps.setString(9, p.getImageUrl());
            ps.setBoolean(10, p.isActive());
            ps.setBigDecimal(11, p.getRating() != null ? p.getRating() : BigDecimal.ZERO);
            ps.setInt(12, p.getReviewCount() != null ? p.getReviewCount() : 0);
            Timestamp now = new Timestamp(System.currentTimeMillis());
            ps.setTimestamp(13, now);

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    p.setId(rs.getLong(1));
                    p.setCreatedAt(now);
                }
            }
        }
        return p;
    }

    @Override
    public void update(Product p) throws SQLException {
        String sql = "UPDATE products SET category_id = ?, category = ?, name = ?, description = ?, price = ?, " +
                "original_price = ?, stock_qty = ?, image_url = ?, is_active = ? WHERE id = ? AND seller_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (p.getCategoryId() != null) ps.setLong(1, p.getCategoryId());
            else ps.setNull(1, Types.BIGINT);
            ps.setString(2, p.getCategory());
            ps.setString(3, p.getName());
            ps.setString(4, p.getDescription());
            ps.setBigDecimal(5, p.getPrice());
            ps.setBigDecimal(6, p.getOriginalPrice());
            ps.setInt(7, p.getStockQty());
            ps.setString(8, p.getImageUrl());
            ps.setBoolean(9, p.isActive());
            ps.setLong(10, p.getId());
            ps.setLong(11, p.getSellerId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM products WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public void updateStock(Long id, int newStock) throws SQLException {
        String sql = "UPDATE products SET stock_qty = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newStock);
            ps.setLong(2, id);
            ps.executeUpdate();
        }
    }

    @Override
    public void updateRatingAndReviewCount(Long productId) throws SQLException {
        String sql = "UPDATE products SET " +
                "rating = COALESCE((SELECT CAST(AVG(CAST(rating AS DECIMAL(10,2))) AS DECIMAL(3,2)) FROM reviews WHERE product_id = ?), 0.00), " +
                "review_count = (SELECT COUNT(*) FROM reviews WHERE product_id = ?) " +
                "WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, productId);
            ps.setLong(2, productId);
            ps.setLong(3, productId);
            ps.executeUpdate();
        }
    }

    @Override
    public int countTotalProducts() throws SQLException {
        String sql = "SELECT COUNT(*) FROM products WHERE is_active = TRUE";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
}
