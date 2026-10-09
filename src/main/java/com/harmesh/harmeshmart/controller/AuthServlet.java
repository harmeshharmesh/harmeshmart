package com.harmesh.harmeshmart.controller;

import com.harmesh.harmeshmart.model.Role;
import com.harmesh.harmeshmart.model.Seller;
import com.harmesh.harmeshmart.model.User;
import com.harmesh.harmeshmart.service.AuthService;
import com.harmesh.harmeshmart.service.impl.AuthServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Controller for user authentication, registration, session regeneration, and seller onboarding.
 * Conforms to Section 4 Mandatory Engineering Rule 3: Session ID regeneration on login.
 */
@WebServlet(name = "AuthServlet", urlPatterns = {"/login", "/register", "/logout", "/become-seller"})
public class AuthServlet extends BaseServlet {

    private static final Logger logger = LoggerFactory.getLogger(AuthServlet.class);
    private final AuthService authService = new AuthServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String servletPath = req.getServletPath();

        if ("/logout".equals(servletPath)) {
            HttpSession session = req.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            resp.sendRedirect(req.getContextPath() + "/login?msg=logged_out");
            return;
        }

        if ("/become-seller".equals(servletPath)) {
            forwardView(req, resp, "auth/become-seller");
            return;
        }

        if ("/register".equals(servletPath)) {
            forwardView(req, resp, "auth/register");
            return;
        }

        // Default: /login
        String redirect = req.getParameter("redirect");
        req.setAttribute("redirect", redirect);
        forwardView(req, resp, "auth/login");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String servletPath = req.getServletPath();

        if ("/login".equals(servletPath)) {
            handleLogin(req, resp);
        } else if ("/register".equals(servletPath)) {
            handleRegister(req, resp);
        } else if ("/become-seller".equals(servletPath)) {
            handleBecomeSeller(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath() + "/");
        }
    }

    private void handleLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String redirect = req.getParameter("redirect");

        try {
            User user = authService.login(email, password);

            // Mandatory Engineering Rule 3: Regenerate session ID on login to protect against session fixation
            HttpSession oldSession = req.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }
            HttpSession newSession = req.getSession(true);
            newSession.setMaxInactiveInterval(30 * 60); // 30 minutes explicit timeout
            newSession.setAttribute("currentUser", user);

            if (user.getRole() == Role.SELLER) {
                authService.getSellerByUserId(user.getId()).ifPresent(seller -> newSession.setAttribute("currentSeller", seller));
            }

            logger.info("User {} ({}) successfully logged in.", user.getEmail(), user.getRole());

            if (redirect != null && !redirect.trim().isEmpty() && !redirect.contains("login") && !redirect.contains("logout")) {
                resp.sendRedirect(req.getContextPath() + (redirect.startsWith("/") ? redirect : "/" + redirect));
            } else if (user.getRole() == Role.ADMIN) {
                resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
            } else if (user.getRole() == Role.SELLER) {
                resp.sendRedirect(req.getContextPath() + "/seller/dashboard");
            } else {
                resp.sendRedirect(req.getContextPath() + "/");
            }

        } catch (Exception e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.setAttribute("email", email);
            req.setAttribute("redirect", redirect);
            forwardView(req, resp, "auth/login");
        }
    }

    private void handleRegister(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String phone = req.getParameter("phone");
        String roleStr = req.getParameter("role");

        Role role = "SELLER".equalsIgnoreCase(roleStr) ? Role.SELLER : Role.BUYER;

        try {
            User user = authService.register(name, email, password, phone, role);

            if (role == Role.SELLER) {
                String storeName = req.getParameter("storeName");
                if (storeName == null || storeName.trim().isEmpty()) {
                    storeName = name + "'s Store";
                }
                String desc = req.getParameter("storeDescription");
                authService.becomeSeller(user.getId(), storeName, desc, phone);
            }

            // Auto-login registered user
            HttpSession session = req.getSession(true);
            session.setMaxInactiveInterval(30 * 60);
            session.setAttribute("currentUser", user);

            if (user.getRole() == Role.SELLER) {
                authService.getSellerByUserId(user.getId()).ifPresent(s -> session.setAttribute("currentSeller", s));
                resp.sendRedirect(req.getContextPath() + "/seller/dashboard?registered=true");
            } else {
                resp.sendRedirect(req.getContextPath() + "/?registered=true");
            }

        } catch (Exception e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.setAttribute("name", name);
            req.setAttribute("email", email);
            req.setAttribute("phone", phone);
            forwardView(req, resp, "auth/register");
        }
    }

    private void handleBecomeSeller(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User currentUser = getCurrentUser(req);
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login?redirect=/become-seller");
            return;
        }

        String storeName = req.getParameter("storeName");
        String storeDescription = req.getParameter("storeDescription");
        String phone = req.getParameter("phone");

        try {
            Seller seller = authService.becomeSeller(currentUser.getId(), storeName, storeDescription, phone);

            // Update user in session
            currentUser.setRole(Role.SELLER);
            HttpSession session = req.getSession(false);
            if (session != null) {
                session.setAttribute("currentUser", currentUser);
                session.setAttribute("currentSeller", seller);
            }

            resp.sendRedirect(req.getContextPath() + "/seller/dashboard?welcome=seller");
        } catch (Exception e) {
            req.setAttribute("errorMessage", e.getMessage());
            forwardView(req, resp, "auth/become-seller");
        }
    }
}
