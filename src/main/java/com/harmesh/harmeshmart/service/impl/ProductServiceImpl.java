package com.harmesh.harmeshmart.service.impl;

import com.harmesh.harmeshmart.dao.DAOFactory;
import com.harmesh.harmeshmart.dao.ProductDAO;
import com.harmesh.harmeshmart.exception.AppException;
import com.harmesh.harmeshmart.exception.ValidationException;
import com.harmesh.harmeshmart.model.Product;
import com.harmesh.harmeshmart.service.ProductService;
import com.harmesh.harmeshmart.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of ProductService with input validation and business logic.
 */
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductDAO productDAO;

    public ProductServiceImpl() {
        this(DAOFactory.getProductDAO());
    }

    public ProductServiceImpl(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Override
    public Optional<Product> getProductById(Long id) throws AppException {
        if (id == null || id <= 0) {
            throw new ValidationException("id", "Invalid product ID.");
        }
        try {
            return productDAO.findById(id);
        } catch (SQLException e) {
            logger.error("Error retrieving product ID {}: {}", id, e.getMessage(), e);
            throw new AppException("DATABASE_ERROR", "Failed to retrieve product details.");
        }
    }

    @Override
    public List<Product> getAllProducts(boolean activeOnly) throws AppException {
        try {
            return productDAO.findAll(activeOnly);
        } catch (SQLException e) {
            logger.error("Error retrieving products: {}", e.getMessage(), e);
            throw new AppException("DATABASE_ERROR", "Failed to retrieve products list.");
        }
    }

    @Override
    public List<Product> getProductsByCategory(String category) throws AppException {
        try {
            return productDAO.findByCategory(category);
        } catch (SQLException e) {
            logger.error("Error retrieving products by category {}: {}", category, e.getMessage(), e);
            throw new AppException("DATABASE_ERROR", "Failed to retrieve category products.");
        }
    }

    @Override
    public List<Product> getProductsBySeller(Long sellerId) throws AppException {
        if (sellerId == null || sellerId <= 0) {
            throw new ValidationException("sellerId", "Invalid seller ID.");
        }
        try {
            return productDAO.findBySeller(sellerId);
        } catch (SQLException e) {
            logger.error("Error retrieving products for seller {}: {}", sellerId, e.getMessage(), e);
            throw new AppException("DATABASE_ERROR", "Failed to retrieve seller products.");
        }
    }

    @Override
    public List<Product> searchProducts(String keyword, String category, BigDecimal minPrice, BigDecimal maxPrice, String sortBy) throws AppException {
        try {
            return productDAO.search(keyword, category, minPrice, maxPrice, sortBy);
        } catch (SQLException e) {
            logger.error("Error searching products: {}", e.getMessage(), e);
            throw new AppException("DATABASE_ERROR", "Product search failed.");
        }
    }

    @Override
    public Product createProduct(Product product) throws AppException {
        ValidationUtil.validateRequired(product.getName(), "name");
        ValidationUtil.validateRequired(product.getCategory(), "category");
        ValidationUtil.validatePositivePrice(product.getPrice(), "price");
        ValidationUtil.validateNonNegativeInt(product.getStockQty(), "stockQty");

        if (product.getImageUrl() == null || product.getImageUrl().trim().isEmpty()) {
            product.setImageUrl("https://images.unsplash.com/photo-1526170375885-4d8ecf77b99f?w=800&auto=format&fit=crop&q=80");
        }

        try {
            return productDAO.save(product);
        } catch (SQLException e) {
            logger.error("Error saving product {}: {}", product.getName(), e.getMessage(), e);
            throw new AppException("DATABASE_ERROR", "Failed to save product.");
        }
    }

    @Override
    public void updateProduct(Product product) throws AppException {
        ValidationUtil.validateRequired(product.getName(), "name");
        ValidationUtil.validateRequired(product.getCategory(), "category");
        ValidationUtil.validatePositivePrice(product.getPrice(), "price");
        ValidationUtil.validateNonNegativeInt(product.getStockQty(), "stockQty");

        try {
            productDAO.update(product);
        } catch (SQLException e) {
            logger.error("Error updating product ID {}: {}", product.getId(), e.getMessage(), e);
            throw new AppException("DATABASE_ERROR", "Failed to update product.");
        }
    }

    @Override
    public void deleteProduct(Long id, Long sellerId) throws AppException {
        try {
            Optional<Product> p = productDAO.findById(id);
            if (p.isPresent() && (sellerId == null || p.get().getSellerId().equals(sellerId))) {
                productDAO.delete(id);
            } else {
                throw new AppException("UNAUTHORIZED", "Not authorized to delete this product.");
            }
        } catch (SQLException e) {
            logger.error("Error deleting product ID {}: {}", id, e.getMessage(), e);
            throw new AppException("DATABASE_ERROR", "Failed to delete product.");
        }
    }

    @Override
    public void updateStock(Long id, int newStock) throws AppException {
        ValidationUtil.validateNonNegativeInt(newStock, "stockQty");
        try {
            productDAO.updateStock(id, newStock);
        } catch (SQLException e) {
            logger.error("Error updating stock for product ID {}: {}", id, e.getMessage(), e);
            throw new AppException("DATABASE_ERROR", "Failed to update stock quantity.");
        }
    }

    @Override
    public void toggleActiveStatus(Long id, boolean active) throws AppException {
        try {
            Optional<Product> p = productDAO.findById(id);
            if (p.isPresent()) {
                Product prod = p.get();
                prod.setActive(active);
                productDAO.update(prod);
            }
        } catch (SQLException e) {
            logger.error("Error toggling status for product ID {}: {}", id, e.getMessage(), e);
            throw new AppException("DATABASE_ERROR", "Failed to toggle product status.");
        }
    }

    @Override
    public int getTotalProductCount() throws AppException {
        try {
            return productDAO.countTotalProducts();
        } catch (SQLException e) {
            logger.error("Error counting products: {}", e.getMessage(), e);
            return 0;
        }
    }
}
