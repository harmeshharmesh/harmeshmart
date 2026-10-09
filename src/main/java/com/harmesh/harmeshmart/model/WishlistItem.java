package com.harmesh.harmeshmart.model;

import java.sql.Timestamp;

/**
 * Saved / Wishlist item entity.
 */
public class WishlistItem {
    private Long id;
    private Long userId;
    private Long productId;
    private Timestamp createdAt;

    // Joined product details
    private Product product;

    public WishlistItem() {
    }

    public WishlistItem(Long userId, Long productId) {
        this.userId = userId;
        this.productId = productId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
