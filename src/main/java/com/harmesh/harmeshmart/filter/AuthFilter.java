package com.harmesh.harmeshmart.filter;

import com.harmesh.harmeshmart.model.Role;
import com.harmesh.harmeshmart.model.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Role-Based Access Control (RBAC) and Session Verification Filter.
 * Conforms to Section 9 Security Checklist & Section 2 Architecture.
 */
@WebFilter(filterName = "AuthFilter", urlPatterns = {"/seller/*", "/admin/*", "/checkout/*", "/orders/*", "/wishlist/*", "/profile/*"})
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String contextPath = req.getContextPath();
        String uri = req.getRequestURI();
        String path = uri.substring(contextPath.length());

        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;

        if (currentUser == null) {
            // Not authenticated: save target url and redirect to login
            String returnUrl = path;
            if (req.getQueryString() != null) {
                returnUrl += "?" + req.getQueryString();
            }
            resp.sendRedirect(contextPath + "/login?redirect=" + java.net.URLEncoder.encode(returnUrl, "UTF-8"));
            return;
        }

        // Check Admin routes
        if (path.startsWith("/admin")) {
            if (currentUser.getRole() != Role.ADMIN) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: Administrator role required.");
                return;
            }
        }

        // Check Seller routes
        if (path.startsWith("/seller")) {
            if (currentUser.getRole() != Role.SELLER && currentUser.getRole() != Role.ADMIN) {
                resp.sendRedirect(contextPath + "/become-seller?error=seller_required");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
