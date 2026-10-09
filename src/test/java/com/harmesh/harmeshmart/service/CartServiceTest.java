package com.harmesh.harmeshmart.service;

import com.harmesh.harmeshmart.dao.CartDAO;
import com.harmesh.harmeshmart.dao.ProductDAO;
import com.harmesh.harmeshmart.dto.CartSummaryDTO;
import com.harmesh.harmeshmart.model.CartItem;
import com.harmesh.harmeshmart.model.Product;
import com.harmesh.harmeshmart.service.impl.CartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * JUnit 5 test for shopping cart business calculations and coupon codes.
 */
@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private CartDAO cartDAO;

    @Mock
    private ProductDAO productDAO;

    private CartService cartService;

    @BeforeEach
    public void init() {
        cartService = new CartServiceImpl(cartDAO, productDAO);
    }

    @Test
    @DisplayName("Should apply 10% coupon discount when promo code is SAVE10")
    public void testPromoCodeDiscountCalculation() throws Exception {
        Long userId = 5L;

        Product p = new Product();
        p.setId(1L);
        p.setName("UltraPhone 15 Pro 5G");
        p.setPrice(new BigDecimal("5000.00"));
        p.setStockQty(10);

        CartItem item = new CartItem(userId, 1L, 1);
        item.setId(101L);
        item.setProduct(p);

        when(cartDAO.findByUserId(userId)).thenReturn(Collections.singletonList(item));

        CartSummaryDTO summary = cartService.getCartSummary(userId, "SAVE10");

        assertEquals(1, summary.getTotalItems());
        assertEquals(new BigDecimal("5000.00"), summary.getSubtotal());
        assertEquals(new BigDecimal("500.00"), summary.getDiscount()); // Max capped discount
        assertEquals(BigDecimal.ZERO, summary.getShipping()); // Free delivery over ₹999
        assertEquals(new BigDecimal("4500.00"), summary.getGrandTotal());
    }
}
