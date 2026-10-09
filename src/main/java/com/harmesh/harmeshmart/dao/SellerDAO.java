package com.harmesh.harmeshmart.dao;

import com.harmesh.harmeshmart.model.Seller;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for Seller profiles.
 */
public interface SellerDAO {

    Optional<Seller> findById(Long id) throws SQLException;

    Optional<Seller> findByUserId(Long userId) throws SQLException;

    Seller save(Seller seller) throws SQLException;

    void update(Seller seller) throws SQLException;

    List<Seller> findAll() throws SQLException;

    int countTotalSellers() throws SQLException;
}
