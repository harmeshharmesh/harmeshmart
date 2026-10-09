package com.harmesh.harmeshmart.dao.impl;

import com.harmesh.harmeshmart.dao.SellerDAO;
import com.harmesh.harmeshmart.model.Seller;
import com.harmesh.harmeshmart.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC implementation of SellerDAO.
 */
public class SellerDAOImpl implements SellerDAO {

    private Seller mapRow(ResultSet rs) throws SQLException {
        Seller s = new Seller();
        s.setId(rs.getLong("id"));
        s.setUserId(rs.getLong("user_id"));
        s.setStoreName(rs.getString("store_name"));
        s.setStoreDescription(rs.getString("store_description"));
        s.setContactEmail(rs.getString("contact_email"));
        s.setPhone(rs.getString("phone"));
        s.setVerified(rs.getBoolean("verified"));
        s.setRating(rs.getBigDecimal("rating"));
        s.setCreatedAt(rs.getTimestamp("created_at"));
        try {
            s.setOwnerName(rs.getString("owner_name"));
        } catch (SQLException ignored) {
        }
        return s;
    }

    @Override
    public Optional<Seller> findById(Long id) throws SQLException {
        String sql = "SELECT s.*, u.name as owner_name FROM sellers s JOIN users u ON s.user_id = u.id WHERE s.id = ?";
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
    public Optional<Seller> findByUserId(Long userId) throws SQLException {
        String sql = "SELECT s.*, u.name as owner_name FROM sellers s JOIN users u ON s.user_id = u.id WHERE s.user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Seller save(Seller seller) throws SQLException {
        String sql = "INSERT INTO sellers (user_id, store_name, store_description, contact_email, phone, verified, rating, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, seller.getUserId());
            ps.setString(2, seller.getStoreName());
            ps.setString(3, seller.getStoreDescription());
            ps.setString(4, seller.getContactEmail());
            ps.setString(5, seller.getPhone());
            ps.setBoolean(6, seller.isVerified());
            ps.setBigDecimal(7, seller.getRating() != null ? seller.getRating() : new java.math.BigDecimal("5.00"));
            Timestamp now = new Timestamp(System.currentTimeMillis());
            ps.setTimestamp(8, now);

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    seller.setId(rs.getLong(1));
                    seller.setCreatedAt(now);
                }
            }
        }
        return seller;
    }

    @Override
    public void update(Seller seller) throws SQLException {
        String sql = "UPDATE sellers SET store_name = ?, store_description = ?, contact_email = ?, phone = ?, verified = ?, rating = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, seller.getStoreName());
            ps.setString(2, seller.getStoreDescription());
            ps.setString(3, seller.getContactEmail());
            ps.setString(4, seller.getPhone());
            ps.setBoolean(5, seller.isVerified());
            ps.setBigDecimal(6, seller.getRating());
            ps.setLong(7, seller.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public List<Seller> findAll() throws SQLException {
        String sql = "SELECT s.*, u.name as owner_name FROM sellers s JOIN users u ON s.user_id = u.id ORDER BY s.id ASC";
        List<Seller> list = new ArrayList<>();
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
    public int countTotalSellers() throws SQLException {
        String sql = "SELECT COUNT(*) FROM sellers";
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
