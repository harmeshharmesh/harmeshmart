package com.harmesh.harmeshmart.controller;

import com.harmesh.harmeshmart.model.Category;
import com.harmesh.harmeshmart.model.Order;
import com.harmesh.harmeshmart.model.Product;
import com.harmesh.harmeshmart.model.Seller;
import com.harmesh.harmeshmart.model.User;
import com.harmesh.harmeshmart.service.AdminService;
import com.harmesh.harmeshmart.service.CategoryService;
import com.harmesh.harmeshmart.service.OrderService;
import com.harmesh.harmeshmart.service.ProductService;
import com.harmesh.harmeshmart.service.impl.AdminServiceImpl;
import com.harmesh.harmeshmart.service.impl.CategoryServiceImpl;
import com.harmesh.harmeshmart.service.impl.OrderServiceImpl;
import com.harmesh.harmeshmart.service.impl.ProductServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Controller for Platform Administrator Dashboard, Moderation, and Category Management.
 */
@WebServlet(name = "AdminServlet", urlPatterns = {"/admin", "/admin/*"})
public class AdminServlet extends BaseServlet {

    private final AdminService adminService = new AdminServiceImpl();
    private final ProductService productService = new ProductServiceImpl();
    private final OrderService orderService = new OrderServiceImpl();
    private final CategoryService categoryService = new CategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User currentUser = getCurrentUser(req);
        if (currentUser == null || !currentUser.isAdmin()) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: Administrator role required.");
            return;
        }

        try {
            Map<String, Object> stats = adminService.getPlatformStats();
            List<User> users = adminService.getAllUsers();
            List<Seller> sellers = adminService.getAllSellers();
            List<Product> products = productService.getAllProducts(false);
            List<Order> orders = orderService.getAllOrders();
            List<Category> categories = categoryService.getAllCategories();

            req.setAttribute("stats", stats);
            req.setAttribute("usersList", users);
            req.setAttribute("sellersList", sellers);
            req.setAttribute("productsList", products);
            req.setAttribute("ordersList", orders);
            req.setAttribute("categoriesList", categories);

            forwardView(req, resp, "admin-dashboard");
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Error loading admin console: " + e.getMessage());
            forwardView(req, resp, "admin-dashboard");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User currentUser = getCurrentUser(req);
        if (currentUser == null || !currentUser.isAdmin()) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
            return;
        }

        String pathInfo = req.getPathInfo();
        if (pathInfo == null) pathInfo = "";

        try {
            if (pathInfo.contains("/sellers/verify")) {
                Long sellerId = Long.parseLong(req.getParameter("sellerId"));
                boolean verified = "true".equalsIgnoreCase(req.getParameter("verified"));
                adminService.setSellerVerified(sellerId, verified);
                resp.sendRedirect(req.getContextPath() + "/admin/dashboard?success=seller_updated");

            } else if (pathInfo.contains("/categories/save")) {
                String name = req.getParameter("name");
                String desc = req.getParameter("description");
                String icon = req.getParameter("icon");
                Category c = new Category();
                c.setName(name);
                c.setDescription(desc);
                c.setIcon(icon != null && !icon.trim().isEmpty() ? icon : "bi-tag");
                categoryService.createCategory(c);
                resp.sendRedirect(req.getContextPath() + "/admin/dashboard?success=category_added");

            } else if (pathInfo.contains("/products/delete")) {
                Long productId = Long.parseLong(req.getParameter("productId"));
                adminService.deleteProduct(productId);
                resp.sendRedirect(req.getContextPath() + "/admin/dashboard?success=product_moderated");

            } else {
                resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
            }
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Admin action failed: " + e.getMessage());
            doGet(req, resp);
        }
    }
}
