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
@WebServlet(name = "ProductServlet", urlPatterns = {"/product", "/product/*", "/products", "/products/*"})
public class ProductServlet extends BaseServlet {

    private final ProductService productService = new ProductServiceImpl();
    private final ReviewService reviewService = new ReviewServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        String idParam = req.getParameter("id");

        Long productId = null;
        String slugOrKeyword = null;

        if (idParam != null && !idParam.trim().isEmpty()) {
            try {
                productId = Long.parseLong(idParam.trim());
            } catch (NumberFormatException ignored) {
                slugOrKeyword = idParam.trim();
            }
        } else if (pathInfo != null && pathInfo.length() > 1) {
            String pathVal = pathInfo.substring(1).trim();
            try {
                productId = Long.parseLong(pathVal);
            } catch (NumberFormatException ignored) {
                slugOrKeyword = pathVal.replace("-", " ");
            }
        }

        try {
            Optional<Product> prodOpt = Optional.empty();
            if (productId != null) {
                prodOpt = productService.getProductById(productId);
            } else if (slugOrKeyword != null && !slugOrKeyword.isEmpty()) {
                List<Product> matches = productService.searchProducts(slugOrKeyword, null, null, null, null);
                if (!matches.isEmpty()) {
                    prodOpt = Optional.of(matches.get(0));
                    productId = prodOpt.get().getId();
                }
            } else {
                resp.sendRedirect(req.getContextPath() + "/shop");
                return;
            }

            if (prodOpt.isEmpty()) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found.");
                return;
            }

            Product product = prodOpt.get();
            productId = product.getId();
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
