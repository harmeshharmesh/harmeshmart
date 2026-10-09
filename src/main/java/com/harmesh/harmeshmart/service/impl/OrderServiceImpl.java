package com.harmesh.harmeshmart.service.impl;

import com.harmesh.harmeshmart.dao.*;
import com.harmesh.harmeshmart.dto.CartSummaryDTO;
import com.harmesh.harmeshmart.exception.AppException;
import com.harmesh.harmeshmart.exception.ValidationException;
import com.harmesh.harmeshmart.model.*;
import com.harmesh.harmeshmart.service.CartService;
import com.harmesh.harmeshmart.service.OrderService;
import com.harmesh.harmeshmart.service.payment.PaymentStrategy;
import com.harmesh.harmeshmart.service.payment.PaymentStrategyFactory;
import com.harmesh.harmeshmart.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of OrderService orchestrating cart checkout, payment strategy, and order tracking workflow.
 */
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final OrderDAO orderDAO;
    private final CartDAO cartDAO;
    private final AddressDAO addressDAO;
    private final ProductDAO productDAO;
    private final CartService cartService;

    public OrderServiceImpl() {
        this(DAOFactory.getOrderDAO(), DAOFactory.getCartDAO(), DAOFactory.getAddressDAO(), DAOFactory.getProductDAO(), new CartServiceImpl());
    }

    public OrderServiceImpl(OrderDAO orderDAO, CartDAO cartDAO, AddressDAO addressDAO, ProductDAO productDAO, CartService cartService) {
        this.orderDAO = orderDAO;
        this.cartDAO = cartDAO;
        this.addressDAO = addressDAO;
        this.productDAO = productDAO;
        this.cartService = cartService;
    }

    @Override
    public Order placeOrder(Long buyerId, Long addressId, String manualAddress, String paymentMethod, String paymentDetails, String promoCode) throws AppException {
        if (buyerId == null || buyerId <= 0) {
            throw new ValidationException("buyerId", "Invalid buyer session.");
        }

        // Determine shipping address
        String finalAddress = manualAddress != null ? manualAddress.trim() : "";
        if (finalAddress.isEmpty() && addressId != null && addressId > 0) {
            try {
                Optional<Address> addrOpt = addressDAO.findById(addressId);
                if (addrOpt.isPresent() && addrOpt.get().getUserId().equals(buyerId)) {
                    finalAddress = addrOpt.get().getFormattedAddress();
                }
            } catch (SQLException e) {
                logger.error("Error retrieving address: {}", e.getMessage(), e);
            }
        }

        if (finalAddress.isEmpty()) {
            throw new ValidationException("shippingAddress", "Please provide a valid delivery address.");
        }

        CartSummaryDTO cartSummary = cartService.getCartSummary(buyerId, promoCode);
        if (cartSummary.getItems().isEmpty()) {
            throw new ValidationException("cart", "Your shopping cart is empty.");
        }

        // Validate stock availability for all items
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartSummaryDTO.CartItemDetailDTO item : cartSummary.getItems()) {
            try {
                Optional<Product> prodOpt = productDAO.findById(item.getProductId());
                if (prodOpt.isEmpty() || !prodOpt.get().isActive()) {
                    throw new AppException("ITEM_UNAVAILABLE", "Product " + item.getProductName() + " is no longer available.");
                }
                if (prodOpt.get().getStockQty() < item.getQuantity()) {
                    throw new AppException("INSUFFICIENT_STOCK", "Insufficient stock for " + item.getProductName() + ". Available: " + prodOpt.get().getStockQty());
                }

                OrderItem oi = new OrderItem();
                oi.setProductId(item.getProductId());
                oi.setQuantity(item.getQuantity());
                oi.setUnitPrice(item.getUnitPrice());
                orderItems.add(oi);
            } catch (SQLException e) {
                logger.error("Error checking product stock: {}", e.getMessage(), e);
                throw new AppException("DATABASE_ERROR", "Error validating cart products.");
            }
        }

        // Execute payment strategy
        String cleanMethod = (paymentMethod != null && !paymentMethod.trim().isEmpty()) ? paymentMethod.trim().toUpperCase() : "COD";
        PaymentStrategy strategy = PaymentStrategyFactory.getStrategy(cleanMethod);
        String paymentResult = strategy.processPayment(null, cartSummary.getGrandTotal(), paymentDetails);
        logger.info("Payment simulation result: {}", paymentResult);

        // Build Order entity
        Order order = new Order();
        order.setBuyerId(buyerId);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(cartSummary.getGrandTotal());
        order.setShippingAddress(finalAddress);
        order.setPaymentMethod(strategy.getMethodName());
        order.setPaymentStatus("COD".equalsIgnoreCase(strategy.getMethodName()) ? "PENDING" : "PAID");
        order.setPromoCode(cartSummary.getPromoCode());
        order.setDiscountAmount(cartSummary.getDiscount());

        try {
            Order createdOrder = orderDAO.createOrder(order, orderItems);
            // Clear cart upon successful order creation
            cartDAO.clearCart(buyerId);
            return createdOrder;
        } catch (SQLException e) {
            logger.error("Failed to commit order placement: {}", e.getMessage(), e);
            throw new AppException("DATABASE_ERROR", "Order placement failed. Your card or payment has not been charged.");
        }
    }

    @Override
    public Optional<Order> getOrderById(Long orderId, Long requestingUserId, Role requestingRole) throws AppException {
        try {
            Optional<Order> orderOpt = orderDAO.findById(orderId);
            if (orderOpt.isEmpty()) {
                return Optional.empty();
            }

            Order order = orderOpt.get();
            // Verify access permissions (admin or buyer owner or seller who owns at least one item)
            if (requestingRole == Role.ADMIN) {
                return orderOpt;
            }
            if (order.getBuyerId().equals(requestingUserId)) {
                return orderOpt;
            }
            if (requestingRole == Role.SELLER) {
                for (OrderItem it : order.getItems()) {
                    if (requestingUserId.equals(it.getSellerId())) {
                        return orderOpt;
                    }
                }
            }

            throw new AppException("UNAUTHORIZED", "Access Denied: You do not have permission to view this order.");
        } catch (SQLException e) {
            logger.error("Error retrieving order ID {}: {}", orderId, e.getMessage(), e);
            throw new AppException("DATABASE_ERROR", "Failed to retrieve order.");
        }
    }

    @Override
    public List<Order> getOrdersForBuyer(Long buyerId) throws AppException {
        try {
            return orderDAO.findByBuyerId(buyerId);
        } catch (SQLException e) {
            logger.error("Error retrieving orders for buyer {}: {}", buyerId, e.getMessage(), e);
            throw new AppException("DATABASE_ERROR", "Failed to retrieve order history.");
        }
    }

    @Override
    public List<Order> getOrdersForSeller(Long sellerId) throws AppException {
        try {
            return orderDAO.findBySellerId(sellerId);
        } catch (SQLException e) {
            logger.error("Error retrieving orders for seller {}: {}", sellerId, e.getMessage(), e);
            throw new AppException("DATABASE_ERROR", "Failed to retrieve incoming orders.");
        }
    }

    @Override
    public List<Order> getAllOrders() throws AppException {
        try {
            return orderDAO.findAll();
        } catch (SQLException e) {
            logger.error("Error retrieving all orders for admin: {}", e.getMessage(), e);
            throw new AppException("DATABASE_ERROR", "Failed to retrieve orders.");
        }
    }

    @Override
    public void updateOrderStatus(Long orderId, OrderStatus status) throws AppException {
        if (orderId == null || status == null) {
            throw new ValidationException("orderId", "Order ID and status are required.");
        }
        try {
            orderDAO.updateStatus(orderId, status);
        } catch (SQLException e) {
            logger.error("Error updating order ID {} status to {}: {}", orderId, status, e.getMessage(), e);
            throw new AppException("DATABASE_ERROR", "Failed to update order status.");
        }
    }

    @Override
    public int getTotalOrderCount() throws AppException {
        try {
            return orderDAO.countTotalOrders();
        } catch (SQLException e) {
            logger.error("Error counting orders: {}", e.getMessage(), e);
            return 0;
        }
    }

    @Override
    public BigDecimal getPlatformRevenue() throws AppException {
        try {
            return orderDAO.calculatePlatformRevenue();
        } catch (SQLException e) {
            logger.error("Error calculating platform revenue: {}", e.getMessage(), e);
            return BigDecimal.ZERO;
        }
    }

    @Override
    public BigDecimal getSellerRevenue(Long sellerId) throws AppException {
        try {
            return orderDAO.calculateSellerRevenue(sellerId);
        } catch (SQLException e) {
            logger.error("Error calculating seller revenue for {}: {}", sellerId, e.getMessage(), e);
            return BigDecimal.ZERO;
        }
    }
}
