package com.harmesh.harmeshmart.dao;

import com.harmesh.harmeshmart.model.Order;
import com.harmesh.harmeshmart.model.OrderItem;
import com.harmesh.harmeshmart.model.OrderStatus;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for Orders and Order Items.
 */
public interface OrderDAO {

    Order createOrder(Order order, List<OrderItem> items) throws SQLException;

    Optional<Order> findById(Long id) throws SQLException;

    List<Order> findByBuyerId(Long buyerId) throws SQLException;

    List<Order> findBySellerId(Long sellerId) throws SQLException;

    List<Order> findAll() throws SQLException;

    void updateStatus(Long orderId, OrderStatus status) throws SQLException;

    int countTotalOrders() throws SQLException;

    BigDecimal calculatePlatformRevenue() throws SQLException;

    BigDecimal calculateSellerRevenue(Long sellerId) throws SQLException;
}
