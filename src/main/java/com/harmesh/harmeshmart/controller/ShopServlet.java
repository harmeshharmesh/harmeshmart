package com.harmesh.harmeshmart.controller;

import com.harmesh.harmeshmart.model.Product;
import com.harmesh.harmeshmart.service.ProductService;
import com.harmesh.harmeshmart.service.impl.ProductServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * Controller for marketplace browsing, catalog exploration, and multi-facet search.
 */
@WebServlet(name = "ShopServlet", urlPatterns = {"", "/shop", "/index.jsp"})
public class ShopServlet extends BaseServlet {

    private final ProductService productService = new ProductServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String keyword = req.getParameter("keyword");
        String category = req.getParameter("category");
        String minPriceStr = req.getParameter("minPrice");
        String maxPriceStr = req.getParameter("maxPrice");
        String sortBy = req.getParameter("sort");

        BigDecimal minPrice = null;
        BigDecimal maxPrice = null;

        if (minPriceStr != null && !minPriceStr.trim().isEmpty()) {
            try {
                minPrice = new BigDecimal(minPriceStr.trim());
            } catch (NumberFormatException ignored) {
            }
        }

        if (maxPriceStr != null && !maxPriceStr.trim().isEmpty()) {
            try {
                maxPrice = new BigDecimal(maxPriceStr.trim());
            } catch (NumberFormatException ignored) {
            }
        }

        try {
            List<Product> products = productService.searchProducts(keyword, category, minPrice, maxPrice, sortBy);
            req.setAttribute("products", products);
            req.setAttribute("searchKeyword", keyword != null ? keyword : "");
            req.setAttribute("selectedCategory", category != null ? category : "all");
            req.setAttribute("selectedSort", sortBy != null ? sortBy : "featured");
            req.setAttribute("selectedMinPrice", minPriceStr != null ? minPriceStr : "");
            req.setAttribute("selectedMaxPrice", maxPriceStr != null ? maxPriceStr : "");

            // Flag whether this is a search result or landing page
            boolean isFiltered = (keyword != null && !keyword.trim().isEmpty()) ||
                    (category != null && !category.trim().isEmpty() && !"all".equalsIgnoreCase(category)) ||
                    minPrice != null || maxPrice != null;
            req.setAttribute("isFiltered", isFiltered);

            forwardView(req, resp, "shop");
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Unable to load products: " + e.getMessage());
            forwardView(req, resp, "shop");
        }
    }
}
