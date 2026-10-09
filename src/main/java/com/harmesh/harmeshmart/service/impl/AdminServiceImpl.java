package com.harmesh.harmeshmart.service.impl;

import com.harmesh.harmeshmart.dao.*;
import com.harmesh.harmeshmart.exception.AppException;
import com.harmesh.harmeshmart.model.Seller;
import com.harmesh.harmeshmart.model.User;
import com.harmesh.harmeshmart.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implementation of AdminService.
 */
public class AdminServiceImpl implements AdminService {

    private static final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);
    private final UserDAO userDAO;
    private final SellerDAO sellerDAO;
    private final ProductDAO productDAO;
    private final OrderDAO orderDAO;

    public AdminServiceImpl() {
        this(DAOFactory.getUserDAO(), DAOFactory.getSellerDAO(), DAOFactory.getProductDAO(), DAOFactory.getOrderDAO());
    }

    public AdminServiceImpl(UserDAO userDAO, SellerDAO sellerDAO, ProductDAO productDAO, OrderDAO orderDAO) {
        this.userDAO = userDAO;
        this.sellerDAO = sellerDAO;
        this.productDAO = productDAO;
        this.orderDAO = orderDAO;
    }

    @Override
    public Map<String, Object> getPlatformStats() throws AppException {
        Map<String, Object> stats = new HashMap<>();
        try {
            int users = userDAO.countTotalUsers();
            int sellers = sellerDAO.countTotalSellers();
            int products = productDAO.countTotalProducts();
            int orders = orderDAO.countTotalOrders();
            BigDecimal revenue = orderDAO.calculatePlatformRevenue();

            stats.put("totalUsers", users);
            stats.put("totalSellers", sellers);
            stats.put("totalProducts", products);
            stats.put("totalOrders", orders);
            stats.put("platformRevenue", revenue);
        } catch (SQLException e) {
            logger.error("Error gathering admin platform stats: {}", e.getMessage(), e);
            throw new AppException("DATABASE_ERROR", "Failed to compile platform statistics.");
        }
        return stats;
    }

    @Override
    public List<User> getAllUsers() throws AppException {
        try {
            return userDAO.findAll();
        } catch (SQLException e) {
            logger.error("Error retrieving all users: {}", e.getMessage(), e);
            throw new AppException("DATABASE_ERROR", "Failed to load users list.");
        }
    }

    @Override
    public List<Seller> getAllSellers() throws AppException {
        try {
            return sellerDAO.findAll();
        } catch (SQLException e) {
            logger.error("Error retrieving all sellers: {}", e.getMessage(), e);
            throw new AppException("DATABASE_ERROR", "Failed to load sellers list.");
        }
    }

    @Override
    public void setSellerVerified(Long sellerId, boolean verified) throws AppException {
        try {
            Optional<Seller> s = sellerDAO.findById(sellerId);
            if (s.isPresent()) {
                Seller seller = s.get();
                seller.setVerified(verified);
                sellerDAO.update(seller);
            }
        } catch (SQLException e) {
            logger.error("Error updating seller verification: {}", e.getMessage(), e);
            throw new AppException("DATABASE_ERROR", "Failed to update seller verification.");
        }
    }

    @Override
    public void deleteProduct(Long productId) throws AppException {
        try {
            productDAO.delete(productId);
        } catch (SQLException e) {
            logger.error("Error deleting product {}: {}", productId, e.getMessage(), e);
            throw new AppException("DATABASE_ERROR", "Failed to delete product.");
        }
    }
}
