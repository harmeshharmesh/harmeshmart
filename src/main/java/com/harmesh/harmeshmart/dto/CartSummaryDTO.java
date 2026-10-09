package com.harmesh.harmeshmart.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Cart summary DTO returned to client for AJAX live updates.
 */
public class CartSummaryDTO {
    private int totalItems;
    private BigDecimal subtotal;
    private BigDecimal shipping;
    private BigDecimal discount;
    private BigDecimal grandTotal;
    private String promoCode;
    private List<CartItemDetailDTO> items = new ArrayList<>();

    public CartSummaryDTO() {
        this.subtotal = BigDecimal.ZERO;
        this.shipping = BigDecimal.ZERO;
        this.discount = BigDecimal.ZERO;
        this.grandTotal = BigDecimal.ZERO;
    }

    public static class CartItemDetailDTO {
        private Long cartItemId;
        private Long productId;
        private String productName;
        private String imageUrl;
        private BigDecimal unitPrice;
        private int quantity;
        private BigDecimal itemTotal;
        private int stockQty;

        public Long getCartItemId() { return cartItemId; }
        public void setCartItemId(Long cartItemId) { this.cartItemId = cartItemId; }
        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }
        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
        public BigDecimal getUnitPrice() { return unitPrice; }
        public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        public BigDecimal getItemTotal() { return itemTotal; }
        public void setItemTotal(BigDecimal itemTotal) { this.itemTotal = itemTotal; }
        public int getStockQty() { return stockQty; }
        public void setStockQty(int stockQty) { this.stockQty = stockQty; }
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getShipping() {
        return shipping;
    }

    public void setShipping(BigDecimal shipping) {
        this.shipping = shipping;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(BigDecimal grandTotal) {
        this.grandTotal = grandTotal;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public List<CartItemDetailDTO> getItems() {
        return items;
    }

    public void setItems(List<CartItemDetailDTO> items) {
        this.items = items;
    }
}
