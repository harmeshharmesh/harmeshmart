package com.harmesh.harmeshmart.controller;

import com.harmesh.harmeshmart.dto.ApiResponse;
import com.harmesh.harmeshmart.model.User;
import com.harmesh.harmeshmart.model.WishlistItem;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Controller for managing buyer wishlist and save-for-later items.
 */
@WebServlet(name = "WishlistServlet", urlPatterns = {"/wishlist", "/wishlist/*", "/api/v1/wishlist/*"})
public class WishlistServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User currentUser = getCurrentUser(req);
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login?redirect=/wishlist");
            return;
        }

        try {
            List<WishlistItem> wishlist = wishlistService.getWishlist(currentUser.getId());
            req.setAttribute("wishlist", wishlist);
            forwardView(req, resp, "wishlist");
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Error loading wishlist: " + e.getMessage());
            forwardView(req, resp, "wishlist");
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
                        ApiResponse.error("UNAUTHORIZED", "Please log in to manage wishlist."));
            } else {
                resp.sendRedirect(req.getContextPath() + "/login?redirect=/wishlist");
            }
            return;
        }

        String action = req.getPathInfo();
        if (action == null) action = "";

        try {
            if (action.contains("/add") || "add".equalsIgnoreCase(req.getParameter("action"))) {
                Long productId = Long.parseLong(req.getParameter("productId"));
                wishlistService.addToWishlist(currentUser.getId(), productId);

                if (isApi || "XMLHttpRequest".equals(req.getHeader("X-Requested-With"))) {
                    sendJsonResponse(resp, HttpServletResponse.SC_OK,
                            ApiResponse.success(wishlistService.getWishlistCount(currentUser.getId())));
                    return;
                }
                resp.sendRedirect(req.getContextPath() + "/wishlist?success=added");

            } else if (action.contains("/remove") || "remove".equalsIgnoreCase(req.getParameter("action"))) {
                Long productId = Long.parseLong(req.getParameter("productId"));
                wishlistService.removeFromWishlist(currentUser.getId(), productId);

                if (isApi || "XMLHttpRequest".equals(req.getHeader("X-Requested-With"))) {
                    sendJsonResponse(resp, HttpServletResponse.SC_OK,
                            ApiResponse.success(wishlistService.getWishlistCount(currentUser.getId())));
                    return;
                }
                resp.sendRedirect(req.getContextPath() + "/wishlist?success=removed");

            } else if (action.contains("/move-to-cart") || "move-to-cart".equalsIgnoreCase(req.getParameter("action"))) {
                Long productId = Long.parseLong(req.getParameter("productId"));
                wishlistService.moveToCart(currentUser.getId(), productId);

                resp.sendRedirect(req.getContextPath() + "/cart?success=moved_from_wishlist");

            } else {
                resp.sendRedirect(req.getContextPath() + "/wishlist");
            }
        } catch (Exception e) {
            if (isApi || "XMLHttpRequest".equals(req.getHeader("X-Requested-With"))) {
                sendJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST, ApiResponse.error("WISHLIST_ERROR", e.getMessage()));
            } else {
                req.setAttribute("errorMessage", e.getMessage());
                doGet(req, resp);
            }
        }
    }
}
