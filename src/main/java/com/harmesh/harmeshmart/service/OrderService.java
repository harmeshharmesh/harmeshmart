package com.harmesh.harmeshmart.service;

import com.harmesh.harmeshmart.exception.AppException;
import com.harmesh.harmeshmart.model.Order;
import com.harmesh.harmeshmart.model.OrderStatus;
import com.harmesh.harmeshmart.model.Role;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Service for placing orders, payment simulation, and order status workflow.
 */
public interface OrderService {

    Order placeOrder(Long buyerId, Long addressId, String manualAddress, String paymentMethod, String paymentDetails, String promoCode) throws AppException;

    Optional<Order> getOrderById(Long orderId, Long requestingUserId, Role requestingRole) throws AppException;

    List<Order> getOrdersForBuyer(Long buyerId) throws AppException;

    List<Order> getOrdersForSeller(Long sellerId) throws AppException;

    List<Order> getAllOrders() throws AppException;

    void updateOrderStatus(Long orderId, OrderStatus status) throws AppException;

    int getTotalOrderCount() throws AppException;

    BigDecimal getPlatformRevenue() throws AppException;

    BigDecimal getSellerRevenue(Long sellerId) throws AppException;
}
