package com.harmesh.harmeshmart.controller;

import com.harmesh.harmeshmart.dto.ApiResponse;
import com.harmesh.harmeshmart.model.Category;
import com.harmesh.harmeshmart.model.User;
import com.harmesh.harmeshmart.service.CartService;
import com.harmesh.harmeshmart.service.CategoryService;
import com.harmesh.harmeshmart.service.WishlistService;
import com.harmesh.harmeshmart.service.impl.CartServiceImpl;
import com.harmesh.harmeshmart.service.impl.CategoryServiceImpl;
import com.harmesh.harmeshmart.service.impl.WishlistServiceImpl;
import com.harmesh.harmeshmart.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Base abstract servlet providing common header attribute injection, view forwarding, and JSON output.
 */
public abstract class BaseServlet extends HttpServlet {

    protected final CategoryService categoryService = new CategoryServiceImpl();
    protected final CartService cartService = new CartServiceImpl();
    protected final WishlistService wishlistService = new WishlistServiceImpl();

    /**
     * Populate standard header data: categories, cart count, wishlist count, active user.
     */
    protected void populateCommonAttributes(HttpServletRequest req) {
        try {
            List<Category> categories = categoryService.getAllCategories();
            req.setAttribute("categories", categories);

            HttpSession session = req.getSession(false);
            User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;
            req.setAttribute("currentUser", currentUser);

            int cartCount = 0;
            int wishlistCount = 0;
            if (currentUser != null) {
                cartCount = cartService.getCartCount(currentUser.getId());
                wishlistCount = wishlistService.getWishlistCount(currentUser.getId());
            }

            req.setAttribute("cartCount", cartCount);
            req.setAttribute("wishlistCount", wishlistCount);
        } catch (Exception ignored) {
        }
    }

    /**
     * Forward to a JSP view inside /WEB-INF/views/.
     */
    protected void forwardView(HttpServletRequest req, HttpServletResponse resp, String viewName)
            throws ServletException, IOException {
        populateCommonAttributes(req);
        String jspPath = "/WEB-INF/views/" + viewName + ".jsp";
        req.getRequestDispatcher(jspPath).forward(req, resp);
    }

    /**
     * Send JSON response using standard envelope.
     */
    protected void sendJsonResponse(HttpServletResponse resp, int status, ApiResponse<?> body) throws IOException {
        JsonUtil.sendJsonResponse(resp, status, body);
    }

    /**
     * Get currently logged-in user or null.
     */
    protected User getCurrentUser(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return (session != null) ? (User) session.getAttribute("currentUser") : null;
    }
}
