package com.harmesh.harmeshmart.dao.impl;

import com.harmesh.harmeshmart.dao.AddressDAO;
import com.harmesh.harmeshmart.model.Address;
import com.harmesh.harmeshmart.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC implementation of AddressDAO.
 */
public class AddressDAOImpl implements AddressDAO {

    private Address mapRow(ResultSet rs) throws SQLException {
        Address a = new Address();
        a.setId(rs.getLong("id"));
        a.setUserId(rs.getLong("user_id"));
        a.setFullName(rs.getString("full_name"));
        a.setPhone(rs.getString("phone"));
        a.setStreet(rs.getString("street"));
        a.setCity(rs.getString("city"));
        a.setState(rs.getString("state"));
        a.setPostalCode(rs.getString("postal_code"));
        a.setDefault(rs.getBoolean("is_default"));
        a.setCreatedAt(rs.getTimestamp("created_at"));
        return a;
    }

    @Override
    public Optional<Address> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM addresses WHERE id = ?";
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
    public List<Address> findByUserId(Long userId) throws SQLException {
        String sql = "SELECT * FROM addresses WHERE user_id = ? ORDER BY is_default DESC, id DESC";
        List<Address> list = new ArrayList<>();
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
    public Optional<Address> findDefaultByUserId(Long userId) throws SQLException {
        String sql = "SELECT * FROM addresses WHERE user_id = ? AND is_default = TRUE";
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
    public Address save(Address a) throws SQLException {
        if (a.isDefault()) {
            clearDefault(a.getUserId());
        }
        String sql = "INSERT INTO addresses (user_id, full_name, phone, street, city, state, postal_code, is_default, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, a.getUserId());
            ps.setString(2, a.getFullName());
            ps.setString(3, a.getPhone());
            ps.setString(4, a.getStreet());
            ps.setString(5, a.getCity());
            ps.setString(6, a.getState());
            ps.setString(7, a.getPostalCode());
            ps.setBoolean(8, a.isDefault());
            Timestamp now = new Timestamp(System.currentTimeMillis());
            ps.setTimestamp(9, now);

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    a.setId(rs.getLong(1));
                    a.setCreatedAt(now);
                }
            }
        }
        return a;
    }

    @Override
    public void update(Address a) throws SQLException {
        if (a.isDefault()) {
            clearDefault(a.getUserId());
        }
        String sql = "UPDATE addresses SET full_name = ?, phone = ?, street = ?, city = ?, state = ?, postal_code = ?, is_default = ? WHERE id = ? AND user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, a.getFullName());
            ps.setString(2, a.getPhone());
            ps.setString(3, a.getStreet());
            ps.setString(4, a.getCity());
            ps.setString(5, a.getState());
            ps.setString(6, a.getPostalCode());
            ps.setBoolean(7, a.isDefault());
            ps.setLong(8, a.getId());
            ps.setLong(9, a.getUserId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Long id, Long userId) throws SQLException {
        String sql = "DELETE FROM addresses WHERE id = ? AND user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.setLong(2, userId);
            ps.executeUpdate();
        }
    }

    @Override
    public void clearDefault(Long userId) throws SQLException {
        String sql = "UPDATE addresses SET is_default = FALSE WHERE user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.executeUpdate();
        }
    }
}
