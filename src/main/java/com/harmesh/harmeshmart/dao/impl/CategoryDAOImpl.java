package com.harmesh.harmeshmart.dao.impl;

import com.harmesh.harmeshmart.dao.CategoryDAO;
import com.harmesh.harmeshmart.model.Category;
import com.harmesh.harmeshmart.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC implementation of CategoryDAO.
 */
public class CategoryDAOImpl implements CategoryDAO {

    private Category mapRow(ResultSet rs) throws SQLException {
        Category cat = new Category();
        cat.setId(rs.getLong("id"));
        cat.setName(rs.getString("name"));
        cat.setSlug(rs.getString("slug"));
        cat.setDescription(rs.getString("description"));
        cat.setIcon(rs.getString("icon"));
        cat.setCreatedAt(rs.getTimestamp("created_at"));
        try {
            cat.setProductCount(rs.getInt("product_count"));
        } catch (SQLException ignored) {
        }
        return cat;
    }

    @Override
    public Optional<Category> findById(Long id) throws SQLException {
        String sql = "SELECT c.*, (SELECT COUNT(*) FROM products p WHERE p.category_id = c.id AND p.is_active = TRUE) as product_count " +
                "FROM categories c WHERE c.id = ?";
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
    public Optional<Category> findBySlug(String slug) throws SQLException {
        String sql = "SELECT c.*, (SELECT COUNT(*) FROM products p WHERE p.category_id = c.id AND p.is_active = TRUE) as product_count " +
                "FROM categories c WHERE c.slug = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, slug);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Category> findAll() throws SQLException {
        String sql = "SELECT c.*, (SELECT COUNT(*) FROM products p WHERE p.category_id = c.id AND p.is_active = TRUE) as product_count " +
                "FROM categories c ORDER BY c.id ASC";
        List<Category> list = new ArrayList<>();
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
    public Category save(Category category) throws SQLException {
        String sql = "INSERT INTO categories (name, slug, description, icon, created_at) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, category.getName());
            ps.setString(2, category.getSlug());
            ps.setString(3, category.getDescription());
            ps.setString(4, category.getIcon());
            Timestamp now = new Timestamp(System.currentTimeMillis());
            ps.setTimestamp(5, now);

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    category.setId(rs.getLong(1));
                    category.setCreatedAt(now);
                }
            }
        }
        return category;
    }

    @Override
    public void update(Category category) throws SQLException {
        String sql = "UPDATE categories SET name = ?, slug = ?, description = ?, icon = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, category.getName());
            ps.setString(2, category.getSlug());
            ps.setString(3, category.getDescription());
            ps.setString(4, category.getIcon());
            ps.setLong(5, category.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM categories WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}
