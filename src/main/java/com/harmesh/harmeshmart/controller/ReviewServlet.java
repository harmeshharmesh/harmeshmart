package com.harmesh.harmeshmart.controller;

import com.harmesh.harmeshmart.model.User;
import com.harmesh.harmeshmart.service.ReviewService;
import com.harmesh.harmeshmart.service.impl.ReviewServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Controller for handling product reviews and customer star ratings.
 */
@WebServlet(name = "ReviewServlet", urlPatterns = {"/reviews/add"})
public class ReviewServlet extends BaseServlet {

    private final ReviewService reviewService = new ReviewServiceImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User currentUser = getCurrentUser(req);
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        try {
            Long productId = Long.parseLong(req.getParameter("productId"));
            int rating = Integer.parseInt(req.getParameter("rating"));
            String comment = req.getParameter("comment");

            reviewService.addReview(currentUser.getId(), productId, rating, comment);
            resp.sendRedirect(req.getContextPath() + "/product?id=" + productId + "&review=success#reviews");
        } catch (Exception e) {
            String prodId = req.getParameter("productId");
            resp.sendRedirect(req.getContextPath() + "/product?id=" + prodId + "&reviewError=" + java.net.URLEncoder.encode(e.getMessage(), "UTF-8") + "#reviews");
        }
    }
}
