package com.harmesh.harmeshmart.dao.impl;

import com.harmesh.harmeshmart.dao.OrderDAO;
import com.harmesh.harmeshmart.model.Order;
import com.harmesh.harmeshmart.model.OrderItem;
import com.harmesh.harmeshmart.model.OrderStatus;
import com.harmesh.harmeshmart.util.DBUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC implementation of OrderDAO with transaction safety.
 */
public class OrderDAOImpl implements OrderDAO {

    private Order mapOrderRow(ResultSet rs) throws SQLException {
        Order o = new Order();
        o.setId(rs.getLong("id"));
        o.setBuyerId(rs.getLong("buyer_id"));
        o.setStatus(OrderStatus.fromString(rs.getString("status")));
        o.setTotalAmount(rs.getBigDecimal("total_amount"));
        o.setShippingAddress(rs.getString("shipping_address"));
        o.setPaymentMethod(rs.getString("payment_method"));
        o.setPaymentStatus(rs.getString("payment_status"));
        o.setPromoCode(rs.getString("promo_code"));
        o.setDiscountAmount(rs.getBigDecimal("discount_amount"));
        o.setCreatedAt(rs.getTimestamp("created_at"));
        try {
            o.setBuyerName(rs.getString("buyer_name"));
            o.setBuyerEmail(rs.getString("buyer_email"));
        } catch (SQLException ignored) {
        }
        return o;
    }

    private OrderItem mapItemRow(ResultSet rs) throws SQLException {
        OrderItem item = new OrderItem();
        item.setId(rs.getLong("id"));
        item.setOrderId(rs.getLong("order_id"));
        item.setProductId(rs.getLong("product_id"));
        item.setQuantity(rs.getInt("quantity"));
        item.setUnitPrice(rs.getBigDecimal("unit_price"));
        item.setCreatedAt(rs.getTimestamp("created_at"));
        try {
            item.setProductName(rs.getString("product_name"));
            item.setProductImageUrl(rs.getString("image_url"));
            item.setSellerId(rs.getLong("seller_id"));
            item.setSellerStoreName(rs.getString("seller_store_name"));
        } catch (SQLException ignored) {
        }
        return item;
    }

    private List<OrderItem> getOrderItems(Connection conn, Long orderId) throws SQLException {
        String sql = "SELECT oi.*, p.name as product_name, p.image_url, p.seller_id, s.store_name as seller_store_name " +
                "FROM order_items oi " +
                "JOIN products p ON oi.product_id = p.id " +
                "LEFT JOIN sellers s ON p.seller_id = s.user_id " +
                "WHERE oi.order_id = ? ORDER BY oi.id ASC";
        List<OrderItem> items = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(mapItemRow(rs));
                }
            }
        }
        return items;
    }

    @Override
    public Order createOrder(Order order, List<OrderItem> items) throws SQLException {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            String orderSql = "INSERT INTO orders (buyer_id, status, total_amount, shipping_address, payment_method, " +
                    "payment_status, promo_code, discount_amount, created_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            Timestamp now = new Timestamp(System.currentTimeMillis());

            try (PreparedStatement psOrder = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {
                psOrder.setLong(1, order.getBuyerId());
                psOrder.setString(2, order.getStatus().name());
                psOrder.setBigDecimal(3, order.getTotalAmount());
                psOrder.setString(4, order.getShippingAddress());
                psOrder.setString(5, order.getPaymentMethod());
                psOrder.setString(6, order.getPaymentStatus());
                psOrder.setString(7, order.getPromoCode());
                psOrder.setBigDecimal(8, order.getDiscountAmount());
                psOrder.setTimestamp(9, now);

                psOrder.executeUpdate();
                try (ResultSet rs = psOrder.getGeneratedKeys()) {
                    if (rs.next()) {
                        order.setId(rs.getLong(1));
                        order.setCreatedAt(now);
                    }
                }
            }

            String itemSql = "INSERT INTO order_items (order_id, product_id, quantity, unit_price, created_at) " +
                    "VALUES (?, ?, ?, ?, ?)";
            String updateStockSql = "UPDATE products SET stock_qty = stock_qty - ? WHERE id = ? AND stock_qty >= ?";

            try (PreparedStatement psItem = conn.prepareStatement(itemSql, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement psStock = conn.prepareStatement(updateStockSql)) {

                for (OrderItem it : items) {
                    it.setOrderId(order.getId());
                    psItem.setLong(1, order.getId());
                    psItem.setLong(2, it.getProductId());
                    psItem.setInt(3, it.getQuantity());
                    psItem.setBigDecimal(4, it.getUnitPrice());
                    psItem.setTimestamp(5, now);
                    psItem.executeUpdate();

                    try (ResultSet rsKey = psItem.getGeneratedKeys()) {
                        if (rsKey.next()) {
                            it.setId(rsKey.getLong(1));
                            it.setCreatedAt(now);
                        }
                    }

                    // Decrement stock safely
                    psStock.setInt(1, it.getQuantity());
                    psStock.setLong(2, it.getProductId());
                    psStock.setInt(3, it.getQuantity());
                    psStock.executeUpdate();
                }
            }

            conn.commit();
            order.setItems(items);
            return order;

        } catch (SQLException ex) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ignored) {
                }
            }
            throw ex;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    @Override
    public Optional<Order> findById(Long id) throws SQLException {
        String sql = "SELECT o.*, u.name as buyer_name, u.email as buyer_email FROM orders o " +
                "JOIN users u ON o.buyer_id = u.id WHERE o.id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Order order = mapOrderRow(rs);
                    order.setItems(getOrderItems(conn, order.getId()));
                    return Optional.of(order);
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Order> findByBuyerId(Long buyerId) throws SQLException {
        String sql = "SELECT o.*, u.name as buyer_name, u.email as buyer_email FROM orders o " +
                "JOIN users u ON o.buyer_id = u.id WHERE o.buyer_id = ? ORDER BY o.id DESC";
        List<Order> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, buyerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order o = mapOrderRow(rs);
                    o.setItems(getOrderItems(conn, o.getId()));
                    list.add(o);
                }
            }
        }
        return list;
    }

    @Override
    public List<Order> findBySellerId(Long sellerId) throws SQLException {
        String sql = "SELECT DISTINCT o.*, u.name as buyer_name, u.email as buyer_email FROM orders o " +
                "JOIN users u ON o.buyer_id = u.id " +
                "JOIN order_items oi ON o.id = oi.order_id " +
                "JOIN products p ON oi.product_id = p.id " +
                "WHERE p.seller_id = ? ORDER BY o.id DESC";
        List<Order> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, sellerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order o = mapOrderRow(rs);
                    o.setItems(getOrderItems(conn, o.getId()));
                    list.add(o);
                }
            }
        }
        return list;
    }

    @Override
    public List<Order> findAll() throws SQLException {
        String sql = "SELECT o.*, u.name as buyer_name, u.email as buyer_email FROM orders o " +
                "JOIN users u ON o.buyer_id = u.id ORDER BY o.id DESC";
        List<Order> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Order o = mapOrderRow(rs);
                o.setItems(getOrderItems(conn, o.getId()));
                list.add(o);
            }
        }
        return list;
    }

    @Override
    public void updateStatus(Long orderId, OrderStatus status) throws SQLException {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setLong(2, orderId);
            ps.executeUpdate();
        }
    }

    @Override
    public int countTotalOrders() throws SQLException {
        String sql = "SELECT COUNT(*) FROM orders";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    @Override
    public BigDecimal calculatePlatformRevenue() throws SQLException {
        String sql = "SELECT COALESCE(SUM(total_amount), 0.00) FROM orders WHERE status != 'CANCELLED'";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getBigDecimal(1);
            }
        }
        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal calculateSellerRevenue(Long sellerId) throws SQLException {
        String sql = "SELECT COALESCE(SUM(oi.quantity * oi.unit_price), 0.00) " +
                "FROM order_items oi " +
                "JOIN orders o ON oi.order_id = o.id " +
                "JOIN products p ON oi.product_id = p.id " +
                "WHERE p.seller_id = ? AND o.status != 'CANCELLED'";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, sellerId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal(1);
                }
            }
        }
        return BigDecimal.ZERO;
    }
}
