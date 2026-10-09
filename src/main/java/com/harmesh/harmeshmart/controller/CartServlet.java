package com.harmesh.harmeshmart.controller;

import com.harmesh.harmeshmart.dto.ApiResponse;
import com.harmesh.harmeshmart.dto.CartSummaryDTO;
import com.harmesh.harmeshmart.model.User;
import com.harmesh.harmeshmart.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Controller managing shopping cart operations, live AJAX quantity updates, and promo codes.
 */
@WebServlet(name = "CartServlet", urlPatterns = {"/cart", "/cart/*", "/api/v1/cart", "/api/v1/cart/*"})
public class CartServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        User currentUser = getCurrentUser(req);

        if (currentUser == null) {
            if (uri.contains("/api/v1/")) {
                sendJsonResponse(resp, HttpServletResponse.SC_UNAUTHORIZED,
                        ApiResponse.error("UNAUTHORIZED", "Please log in to access your cart."));
            } else {
                resp.sendRedirect(req.getContextPath() + "/login?redirect=/cart");
            }
            return;
        }

        String promoCode = req.getParameter("promoCode");
        try {
            CartSummaryDTO summary = cartService.getCartSummary(currentUser.getId(), promoCode);

            if (uri.contains("/api/v1/")) {
                sendJsonResponse(resp, HttpServletResponse.SC_OK, ApiResponse.success(summary));
            } else {
                req.setAttribute("cartSummary", summary);
                forwardView(req, resp, "cart");
            }
        } catch (Exception e) {
            if (uri.contains("/api/v1/")) {
                sendJsonResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        ApiResponse.error("SERVER_ERROR", e.getMessage()));
            } else {
                req.setAttribute("errorMessage", "Error loading cart: " + e.getMessage());
                forwardView(req, resp, "cart");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User currentUser = getCurrentUser(req);
        String uri = req.getRequestURI();
        boolean isApi = uri.contains("/api/v1/");

        if (currentUser == null) {
            if (isApi) {
                sendJsonResponse(resp, HttpServletResponse.SC_UNAUTHORIZED,
                        ApiResponse.error("UNAUTHORIZED", "Please log in to manage your cart."));
            } else {
                resp.sendRedirect(req.getContextPath() + "/login?redirect=/cart");
            }
            return;
        }

        String action = req.getPathInfo();
        if (action == null) action = "";

        try {
            if (action.contains("/add") || "add".equalsIgnoreCase(req.getParameter("action"))) {
                Long productId = Long.parseLong(req.getParameter("productId"));
                int qty = 1;
                if (req.getParameter("quantity") != null) {
                    qty = Integer.parseInt(req.getParameter("quantity"));
                }
                cartService.addToCart(currentUser.getId(), productId, qty);

                if ("true".equalsIgnoreCase(req.getParameter("buyNow"))) {
                    resp.sendRedirect(req.getContextPath() + "/checkout");
                    return;
                }

                if (isApi || "XMLHttpRequest".equals(req.getHeader("X-Requested-With"))) {
                    CartSummaryDTO summary = cartService.getCartSummary(currentUser.getId(), null);
                    sendJsonResponse(resp, HttpServletResponse.SC_OK, ApiResponse.success(summary));
                    return;
                }
                resp.sendRedirect(req.getContextPath() + "/cart?success=added");

            } else if (action.contains("/update") || "update".equalsIgnoreCase(req.getParameter("action"))) {
                Long cartItemId = Long.parseLong(req.getParameter("cartItemId"));
                int qty = Integer.parseInt(req.getParameter("quantity"));
                cartService.updateQuantity(cartItemId, currentUser.getId(), qty);

                String promoCode = req.getParameter("promoCode");
                CartSummaryDTO summary = cartService.getCartSummary(currentUser.getId(), promoCode);

                if (isApi || "XMLHttpRequest".equals(req.getHeader("X-Requested-With"))) {
                    sendJsonResponse(resp, HttpServletResponse.SC_OK, ApiResponse.success(summary));
                    return;
                }
                resp.sendRedirect(req.getContextPath() + "/cart");

            } else if (action.contains("/remove") || "remove".equalsIgnoreCase(req.getParameter("action"))) {
                Long cartItemId = Long.parseLong(req.getParameter("cartItemId"));
                cartService.removeFromCart(cartItemId, currentUser.getId());

                if (isApi || "XMLHttpRequest".equals(req.getHeader("X-Requested-With"))) {
                    CartSummaryDTO summary = cartService.getCartSummary(currentUser.getId(), null);
                    sendJsonResponse(resp, HttpServletResponse.SC_OK, ApiResponse.success(summary));
                    return;
                }
                resp.sendRedirect(req.getContextPath() + "/cart?success=removed");

            } else {
                resp.sendRedirect(req.getContextPath() + "/cart");
            }
        } catch (Exception e) {
            if (isApi || "XMLHttpRequest".equals(req.getHeader("X-Requested-With"))) {
                sendJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                        ApiResponse.error("CART_ERROR", e.getMessage()));
            } else {
                req.setAttribute("errorMessage", e.getMessage());
                forwardView(req, resp, "cart");
            }
        }
    }
}
