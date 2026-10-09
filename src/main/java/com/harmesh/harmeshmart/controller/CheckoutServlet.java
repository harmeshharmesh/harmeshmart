package com.harmesh.harmeshmart.controller;

import com.harmesh.harmeshmart.dao.AddressDAO;
import com.harmesh.harmeshmart.dao.DAOFactory;
import com.harmesh.harmeshmart.dto.CartSummaryDTO;
import com.harmesh.harmeshmart.model.Address;
import com.harmesh.harmeshmart.model.Order;
import com.harmesh.harmeshmart.model.User;
import com.harmesh.harmeshmart.service.OrderService;
import com.harmesh.harmeshmart.service.impl.OrderServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Controller for multi-step checkout, address management, and mock payment confirmation.
 */
@WebServlet(name = "CheckoutServlet", urlPatterns = {"/checkout", "/checkout/*", "/order/confirmation"})
public class CheckoutServlet extends BaseServlet {

    private final OrderService orderService = new OrderServiceImpl();
    private final AddressDAO addressDAO = DAOFactory.getAddressDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User currentUser = getCurrentUser(req);
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login?redirect=/checkout");
            return;
        }

        String uri = req.getRequestURI();
        if (uri.contains("/order/confirmation")) {
            String orderIdStr = req.getParameter("id");
            if (orderIdStr != null) {
                try {
                    Long orderId = Long.parseLong(orderIdStr.trim());
                    Optional<Order> orderOpt = orderService.getOrderById(orderId, currentUser.getId(), currentUser.getRole());
                    if (orderOpt.isPresent()) {
                        req.setAttribute("order", orderOpt.get());
                        forwardView(req, resp, "order-success");
                        return;
                    }
                } catch (Exception ignored) {
                }
            }
            resp.sendRedirect(req.getContextPath() + "/orders");
            return;
        }

        String promoCode = req.getParameter("promoCode");
        try {
            CartSummaryDTO cartSummary = cartService.getCartSummary(currentUser.getId(), promoCode);
            if (cartSummary.getItems().isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/cart?error=empty");
                return;
            }

            List<Address> addresses = addressDAO.findByUserId(currentUser.getId());
            req.setAttribute("cartSummary", cartSummary);
            req.setAttribute("addresses", addresses);

            forwardView(req, resp, "checkout");
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Error loading checkout: " + e.getMessage());
            forwardView(req, resp, "checkout");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User currentUser = getCurrentUser(req);
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login?redirect=/checkout");
            return;
        }

        String action = req.getPathInfo();
        if (action == null) action = "";

        if (action.contains("/address/add")) {
            // Save a new address to address book
            try {
                Address a = new Address();
                a.setUserId(currentUser.getId());
                a.setFullName(req.getParameter("fullName"));
                a.setPhone(req.getParameter("phone"));
                a.setStreet(req.getParameter("street"));
                a.setCity(req.getParameter("city"));
                a.setState(req.getParameter("state"));
                a.setPostalCode(req.getParameter("postalCode"));
                a.setDefault("true".equalsIgnoreCase(req.getParameter("isDefault")));
                addressDAO.save(a);

                resp.sendRedirect(req.getContextPath() + "/checkout?addressAdded=true");
            } catch (Exception e) {
                req.setAttribute("errorMessage", "Error saving address: " + e.getMessage());
                doGet(req, resp);
            }
            return;
        }

        // Place order
        try {
            Long addressId = null;
            String addressIdStr = req.getParameter("addressId");
            if (addressIdStr != null && !addressIdStr.trim().isEmpty() && !addressIdStr.equals("new")) {
                try {
                    addressId = Long.parseLong(addressIdStr.trim());
                } catch (NumberFormatException ignored) {
                }
            }

            String manualAddress = null;
            if (addressId == null) {
                String street = req.getParameter("street");
                String city = req.getParameter("city");
                String state = req.getParameter("state");
                String postal = req.getParameter("postalCode");
                String phone = req.getParameter("phone");
                String name = req.getParameter("fullName");
                if (street != null && !street.trim().isEmpty()) {
                    manualAddress = name + ", " + street + ", " + city + ", " + state + " - " + postal + " (Phone: " + phone + ")";

                    // Optionally save into address book
                    try {
                        Address a = new Address();
                        a.setUserId(currentUser.getId());
                        a.setFullName(name != null ? name : currentUser.getName());
                        a.setPhone(phone != null ? phone : currentUser.getPhone());
                        a.setStreet(street);
                        a.setCity(city != null ? city : "");
                        a.setState(state != null ? state : "");
                        a.setPostalCode(postal != null ? postal : "");
                        a.setDefault(true);
                        addressDAO.save(a);
                    } catch (Exception ignored) {
                    }
                }
            }

            String paymentMethod = req.getParameter("paymentMethod");
            String paymentDetails = req.getParameter("paymentDetails");
            String promoCode = req.getParameter("promoCode");

            Order placedOrder = orderService.placeOrder(
                    currentUser.getId(),
                    addressId,
                    manualAddress,
                    paymentMethod,
                    paymentDetails,
                    promoCode
            );

            resp.sendRedirect(req.getContextPath() + "/order/confirmation?id=" + placedOrder.getId());

        } catch (Exception e) {
            req.setAttribute("errorMessage", "Order could not be placed: " + e.getMessage());
            doGet(req, resp);
        }
    }
}
