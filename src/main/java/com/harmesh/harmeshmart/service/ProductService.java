package com.harmesh.harmeshmart.service;

import com.harmesh.harmeshmart.exception.AppException;
import com.harmesh.harmeshmart.model.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Service for catalog browsing, multi-attribute search, and seller product management.
 */
public interface ProductService {

    Optional<Product> getProductById(Long id) throws AppException;

    List<Product> getAllProducts(boolean activeOnly) throws AppException;

    List<Product> getProductsByCategory(String category) throws AppException;

    List<Product> getProductsBySeller(Long sellerId) throws AppException;

    List<Product> searchProducts(String keyword, String category, BigDecimal minPrice, BigDecimal maxPrice, String sortBy) throws AppException;

    Product createProduct(Product product) throws AppException;

    void updateProduct(Product product) throws AppException;

    void deleteProduct(Long id, Long sellerId) throws AppException;

    void updateStock(Long id, int newStock) throws AppException;

    void toggleActiveStatus(Long id, boolean active) throws AppException;

    int getTotalProductCount() throws AppException;
}
