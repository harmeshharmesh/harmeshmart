package com.harmesh.harmeshmart.controller;

import com.harmesh.harmeshmart.model.Product;
import com.harmesh.harmeshmart.model.Review;
import com.harmesh.harmeshmart.model.User;
import com.harmesh.harmeshmart.service.ProductService;
import com.harmesh.harmeshmart.service.ReviewService;
import com.harmesh.harmeshmart.service.impl.ProductServiceImpl;
import com.harmesh.harmeshmart.service.impl.ReviewServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Controller for product detail view, reviews, and related listings.
 */
@WebServlet(name = "ProductServlet", urlPatterns = {"/product", "/product/*"})
public class ProductServlet extends BaseServlet {

    private final ProductService productService = new ProductServiceImpl();
    private final ReviewService reviewService = new ReviewServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        String idParam = req.getParameter("id");

        Long productId = null;
        if (idParam != null && !idParam.trim().isEmpty()) {
            try {
                productId = Long.parseLong(idParam.trim());
            } catch (NumberFormatException ignored) {
            }
        } else if (pathInfo != null && pathInfo.length() > 1) {
            try {
                productId = Long.parseLong(pathInfo.substring(1));
            } catch (NumberFormatException ignored) {
            }
        }

        if (productId == null) {
            resp.sendRedirect(req.getContextPath() + "/shop");
            return;
        }

        try {
            Optional<Product> prodOpt = productService.getProductById(productId);
            if (prodOpt.isEmpty()) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found.");
                return;
            }

            Product product = prodOpt.get();
            List<Review> reviews = reviewService.getReviewsForProduct(productId);
            List<Product> relatedProducts = productService.getProductsByCategory(product.getCategory());

            User currentUser = getCurrentUser(req);
            boolean inWishlist = false;
            if (currentUser != null) {
                try {
                    inWishlist = wishlistService.getWishlist(currentUser.getId()).stream()
                            .anyMatch(w -> w.getProductId().equals(product.getId()));
                } catch (Exception ignored) {
                }
            }

            req.setAttribute("product", product);
            req.setAttribute("reviews", reviews);
            req.setAttribute("relatedProducts", relatedProducts);
            req.setAttribute("inWishlist", inWishlist);

            forwardView(req, resp, "product-detail");
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error loading product: " + e.getMessage());
        }
    }
}
