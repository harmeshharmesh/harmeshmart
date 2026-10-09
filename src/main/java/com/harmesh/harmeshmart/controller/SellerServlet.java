package com.harmesh.harmeshmart.controller;

import com.harmesh.harmeshmart.model.*;
import com.harmesh.harmeshmart.service.OrderService;
import com.harmesh.harmeshmart.service.ProductService;
import com.harmesh.harmeshmart.service.impl.OrderServiceImpl;
import com.harmesh.harmeshmart.service.impl.ProductServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * Controller for Merchant / Seller Dashboard, Product CRUD operations, and Order Fulfillment.
 */
@WebServlet(name = "SellerServlet", urlPatterns = {"/seller", "/seller/*"})
public class SellerServlet extends BaseServlet {

    private final ProductService productService = new ProductServiceImpl();
    private final OrderService orderService = new OrderServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User currentUser = getCurrentUser(req);
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login?redirect=/seller/dashboard");
            return;
        }

        try {
            List<Product> sellerProducts = productService.getProductsBySeller(currentUser.getId());
            List<Order> incomingOrders = orderService.getOrdersForSeller(currentUser.getId());
            BigDecimal sellerRevenue = orderService.getSellerRevenue(currentUser.getId());

            req.setAttribute("sellerProducts", sellerProducts);
            req.setAttribute("incomingOrders", incomingOrders);
            req.setAttribute("sellerRevenue", sellerRevenue);
            req.setAttribute("totalListings", sellerProducts.size());
            req.setAttribute("totalIncomingOrders", incomingOrders.size());

            forwardView(req, resp, "seller-dashboard");
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Error loading seller dashboard: " + e.getMessage());
            forwardView(req, resp, "seller-dashboard");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User currentUser = getCurrentUser(req);
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String pathInfo = req.getPathInfo();
        if (pathInfo == null) pathInfo = "";

        try {
            if (pathInfo.contains("/products/save")) {
                handleProductSave(req, currentUser.getId());
                resp.sendRedirect(req.getContextPath() + "/seller/dashboard?success=product_saved");
            } else if (pathInfo.contains("/products/stock")) {
                Long productId = Long.parseLong(req.getParameter("productId"));
                int newStock = Integer.parseInt(req.getParameter("stockQty"));
                productService.updateStock(productId, newStock);
                resp.sendRedirect(req.getContextPath() + "/seller/dashboard?success=stock_updated");
            } else if (pathInfo.contains("/products/delete")) {
                Long productId = Long.parseLong(req.getParameter("productId"));
                productService.deleteProduct(productId, currentUser.getId());
                resp.sendRedirect(req.getContextPath() + "/seller/dashboard?success=product_deleted");
            } else if (pathInfo.contains("/orders/status")) {
                Long orderId = Long.parseLong(req.getParameter("orderId"));
                OrderStatus status = OrderStatus.fromString(req.getParameter("status"));
                orderService.updateOrderStatus(orderId, status);
                resp.sendRedirect(req.getContextPath() + "/seller/dashboard?success=order_updated");
            } else {
                resp.sendRedirect(req.getContextPath() + "/seller/dashboard");
            }
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Seller operation failed: " + e.getMessage());
            doGet(req, resp);
        }
    }

    private void handleProductSave(HttpServletRequest req, Long sellerId) throws Exception {
        String idStr = req.getParameter("productId");
        String name = req.getParameter("name");
        String description = req.getParameter("description");
        String category = req.getParameter("category");
        String priceStr = req.getParameter("price");
        String origPriceStr = req.getParameter("originalPrice");
        String stockStr = req.getParameter("stockQty");
        String imageUrl = req.getParameter("imageUrl");

        BigDecimal price = new BigDecimal(priceStr.trim());
        BigDecimal origPrice = (origPriceStr != null && !origPriceStr.trim().isEmpty())
                ? new BigDecimal(origPriceStr.trim()) : price;
        int stock = Integer.parseInt(stockStr.trim());

        if (idStr != null && !idStr.trim().isEmpty()) {
            Long productId = Long.parseLong(idStr.trim());
            Product product = new Product();
            product.setId(productId);
            product.setSellerId(sellerId);
            product.setName(name);
            product.setDescription(description);
            product.setCategory(category);
            product.setPrice(price);
            product.setOriginalPrice(origPrice);
            product.setStockQty(stock);
            product.setImageUrl(imageUrl);
            product.setActive(true);
            productService.updateProduct(product);
        } else {
            Product product = new Product();
            product.setSellerId(sellerId);
            product.setName(name);
            product.setDescription(description);
            product.setCategory(category);
            product.setPrice(price);
            product.setOriginalPrice(origPrice);
            product.setStockQty(stock);
            product.setImageUrl(imageUrl);
            product.setActive(true);
            productService.createProduct(product);
        }
    }
}
