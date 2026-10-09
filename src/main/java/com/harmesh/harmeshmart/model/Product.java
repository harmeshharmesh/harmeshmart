package com.harmesh.harmeshmart.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;

/**
 * Product catalog entity.
 */
public class Product {
    private Long id;
    private Long sellerId;
    private Long categoryId;
    private String category;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Integer stockQty;
    private String imageUrl;
    private boolean isActive;
    private BigDecimal rating;
    private Integer reviewCount;
    private Timestamp createdAt;

    // Transient attributes for display
    private String sellerStoreName;
    private Integer discountPercentage;

    public Product() {
        this.isActive = true;
        this.stockQty = 0;
        this.rating = BigDecimal.ZERO;
        this.reviewCount = 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Integer getStockQty() {
        return stockQty != null ? stockQty : 0;
    }

    public void setStockQty(Integer stockQty) {
        this.stockQty = stockQty;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public BigDecimal getRating() {
        return rating != null ? rating : BigDecimal.ZERO;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public Integer getReviewCount() {
        return reviewCount != null ? reviewCount : 0;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getSellerStoreName() {
        return sellerStoreName;
    }

    public void setSellerStoreName(String sellerStoreName) {
        this.sellerStoreName = sellerStoreName;
    }

    public int getDiscountPercentage() {
        if (originalPrice != null && price != null && originalPrice.compareTo(price) > 0) {
            BigDecimal diff = originalPrice.subtract(price);
            return diff.multiply(BigDecimal.valueOf(100)).divide(originalPrice, 0, RoundingMode.HALF_UP).intValue();
        }
        return 0;
    }

    public boolean isInStock() {
        return stockQty != null && stockQty > 0;
    }

    public boolean isLowStock() {
        return stockQty != null && stockQty > 0 && stockQty <= 5;
    }
}
