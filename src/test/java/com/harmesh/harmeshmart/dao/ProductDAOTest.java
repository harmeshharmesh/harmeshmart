package com.harmesh.harmeshmart.dao;

import com.harmesh.harmeshmart.dao.impl.ProductDAOImpl;
import com.harmesh.harmeshmart.model.Product;
import com.harmesh.harmeshmart.util.DBUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 DAO Test executing against embedded H2 instance.
 * Conforms to Section 9 Testing & Validation Requirements.
 */
public class ProductDAOTest {

    private static ProductDAO productDAO;

    @BeforeAll
    public static void setUp() {
        DBUtil.initialize();
        DBUtil.executeSqlScript("schema.sql");
        DBUtil.executeSqlScript("seed.sql");
        productDAO = new ProductDAOImpl();
    }

    @Test
    @DisplayName("Should find product by primary key ID")
    public void testFindById() throws SQLException {
        Optional<Product> prod = productDAO.findById(1L);
        assertTrue(prod.isPresent(), "Product with ID 1 should exist in seeded dataset");
        assertEquals("UltraPhone 15 Pro 5G (256GB - Titanium Gray)", prod.get().getName());
        assertEquals("Electronics & Gadgets", prod.get().getCategory());
        assertTrue(prod.get().getPrice().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("Should search products by keyword successfully")
    public void testSearchProductsByKeyword() throws SQLException {
        List<Product> results = productDAO.search("Headphones", null, null, null, "price_asc");
        assertNotNull(results);
        assertFalse(results.isEmpty(), "Search for 'Headphones' should return matches");
        assertTrue(results.get(0).getName().toLowerCase().contains("headphone"));
    }

    @Test
    @DisplayName("Should update stock quantity for product")
    public void testUpdateStock() throws SQLException {
        productDAO.updateStock(2L, 45);
        Optional<Product> updated = productDAO.findById(2L);
        assertTrue(updated.isPresent());
        assertEquals(45, updated.get().getStockQty().intValue());
    }
}
