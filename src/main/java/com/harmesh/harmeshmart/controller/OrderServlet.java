package com.harmesh.harmeshmart.controller;

import com.harmesh.harmeshmart.model.Order;
import com.harmesh.harmeshmart.model.User;
import com.harmesh.harmeshmart.service.OrderService;
import com.harmesh.harmeshmart.service.impl.OrderServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Controller for buyer order history, tracking, and downloadable/printable invoice.
 */
@WebServlet(name = "OrderServlet", urlPatterns = {"/orders", "/orders/*", "/order", "/order/*", "/order-success"})
public class OrderServlet extends BaseServlet {

    private final OrderService orderService = new OrderServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User currentUser = getCurrentUser(req);
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login?redirect=/orders");
            return;
        }

        String servletPath = req.getServletPath();
        String pathInfo = req.getPathInfo();
        String idParam = req.getParameter("id");
        String uri = req.getRequestURI();

        // 1. Tax Invoice Route
        if ((pathInfo != null && pathInfo.contains("/invoice")) || "invoice".equalsIgnoreCase(req.getParameter("view")) || uri.contains("/invoice")) {
            if (idParam != null && !idParam.trim().isEmpty()) {
                try {
                    Long orderId = Long.parseLong(idParam.trim());
                    Optional<Order> orderOpt = orderService.getOrderById(orderId, currentUser.getId(), currentUser.getRole());
                    if (orderOpt.isPresent()) {
                        req.setAttribute("order", orderOpt.get());
                        forwardView(req, resp, "order-invoice");
                        return;
                    }
                } catch (Exception ignored) {
                }
            }
        }

        // 2. Order Confirmation / Success Route
        if ((pathInfo != null && (pathInfo.contains("/confirmation") || pathInfo.contains("/success"))) 
                || "/order-success".equals(servletPath) 
                || uri.contains("/confirmation") 
                || "confirmation".equalsIgnoreCase(req.getParameter("view"))) {
            if (idParam != null && !idParam.trim().isEmpty()) {
                try {
                    Long orderId = Long.parseLong(idParam.trim());
                    Optional<Order> orderOpt = orderService.getOrderById(orderId, currentUser.getId(), currentUser.getRole());
                    if (orderOpt.isPresent()) {
                        req.setAttribute("order", orderOpt.get());
                        forwardView(req, resp, "order-success");
                        return;
                    }
                } catch (Exception ignored) {
                }
            }
        }

        // 3. Order Detail Route
        if (idParam != null && !idParam.trim().isEmpty()) {
            try {
                Long orderId = Long.parseLong(idParam.trim());
                Optional<Order> orderOpt = orderService.getOrderById(orderId, currentUser.getId(), currentUser.getRole());
                if (orderOpt.isPresent()) {
                    req.setAttribute("order", orderOpt.get());
                    forwardView(req, resp, "order-success");
                    return;
                }
            } catch (Exception e) {
                req.setAttribute("errorMessage", e.getMessage());
            }
        }

        try {
            List<Order> orders = orderService.getOrdersForBuyer(currentUser.getId());
            req.setAttribute("orders", orders);
            forwardView(req, resp, "orders");
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Error loading orders: " + e.getMessage());
            forwardView(req, resp, "orders");
        }
    }
}
