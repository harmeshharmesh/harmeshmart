package com.harmesh.harmeshmart.dao;

import com.harmesh.harmeshmart.model.Product;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for Products.
 */
public interface ProductDAO {

    Optional<Product> findById(Long id) throws SQLException;

    List<Product> findAll(boolean activeOnly) throws SQLException;

    List<Product> findByCategory(String category) throws SQLException;

    List<Product> findBySeller(Long sellerId) throws SQLException;

    List<Product> search(String keyword, String category, BigDecimal minPrice, BigDecimal maxPrice, String sortBy) throws SQLException;

    Product save(Product product) throws SQLException;

    void update(Product product) throws SQLException;

    void delete(Long id) throws SQLException;

    void updateStock(Long id, int newStock) throws SQLException;

    void updateRatingAndReviewCount(Long productId) throws SQLException;

    int countTotalProducts() throws SQLException;
}
