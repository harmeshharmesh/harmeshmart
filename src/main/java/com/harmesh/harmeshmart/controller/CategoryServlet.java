package com.harmesh.harmeshmart.controller;

import com.harmesh.harmeshmart.model.Category;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * Controller mapping /category/{slug} routes to catalog listings.
 */
@WebServlet(name = "CategoryServlet", urlPatterns = {"/category/*"})
public class CategoryServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && pathInfo.length() > 1) {
            String slug = pathInfo.substring(1);
            try {
                Optional<Category> catOpt = categoryService.getCategoryBySlug(slug);
                if (catOpt.isPresent()) {
                    resp.sendRedirect(req.getContextPath() + "/shop?category=" + java.net.URLEncoder.encode(catOpt.get().getName(), "UTF-8"));
                    return;
                }
            } catch (Exception ignored) {
            }
        }
        resp.sendRedirect(req.getContextPath() + "/shop");
    }
}
