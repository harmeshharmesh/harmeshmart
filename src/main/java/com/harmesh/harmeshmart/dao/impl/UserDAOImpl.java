package com.harmesh.harmeshmart.dao.impl;

import com.harmesh.harmeshmart.dao.UserDAO;
import com.harmesh.harmeshmart.model.Role;
import com.harmesh.harmeshmart.model.User;
import com.harmesh.harmeshmart.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC Implementation of UserDAO using PreparedStatement and try-with-resources.
 */
public class UserDAOImpl implements UserDAO {

    private User mapRow(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setRole(Role.fromString(rs.getString("role")));
        user.setPhone(rs.getString("phone"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        return user;
    }

    @Override
    public Optional<User> findById(Long id) throws SQLException {
        String sql = "SELECT id, name, email, password_hash, role, phone, created_at FROM users WHERE id = ?";
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
    public Optional<User> findByEmail(String email) throws SQLException {
        String sql = "SELECT id, name, email, password_hash, role, phone, created_at FROM users WHERE LOWER(email) = LOWER(?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email != null ? email.trim() : "");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public User save(User user) throws SQLException {
        String sql = "INSERT INTO users (name, email, password_hash, role, phone, created_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPasswordHash());
            ps.setString(4, user.getRole().name());
            ps.setString(5, user.getPhone());
            Timestamp now = new Timestamp(System.currentTimeMillis());
            ps.setTimestamp(6, now);

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    user.setId(rs.getLong(1));
                    user.setCreatedAt(now);
                }
            }
        }
        return user;
    }

    @Override
    public void update(User user) throws SQLException {
        String sql = "UPDATE users SET name = ?, email = ?, password_hash = ?, role = ?, phone = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPasswordHash());
            ps.setString(4, user.getRole().name());
            ps.setString(5, user.getPhone());
            ps.setLong(6, user.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public List<User> findAll() throws SQLException {
        String sql = "SELECT id, name, email, password_hash, role, phone, created_at FROM users ORDER BY id ASC";
        List<User> list = new ArrayList<>();
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
    public int countTotalUsers() throws SQLException {
        String sql = "SELECT COUNT(*) FROM users";
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
