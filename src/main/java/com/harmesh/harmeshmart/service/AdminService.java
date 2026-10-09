package com.harmesh.harmeshmart.service;

import com.harmesh.harmeshmart.exception.AppException;
import com.harmesh.harmeshmart.model.Seller;
import com.harmesh.harmeshmart.model.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Service for platform-wide administrative functions, seller approvals, and system metrics.
 */
public interface AdminService {

    Map<String, Object> getPlatformStats() throws AppException;

    List<User> getAllUsers() throws AppException;

    List<Seller> getAllSellers() throws AppException;

    void setSellerVerified(Long sellerId, boolean verified) throws AppException;

    void deleteProduct(Long productId) throws AppException;
}
