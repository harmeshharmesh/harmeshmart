package com.harmesh.harmeshmart.dao;

import com.harmesh.harmeshmart.dao.impl.*;

/**
 * Factory design pattern for instantiating and providing DAO implementations.
 * Conforms to Section 12 Required Design Patterns.
 */
public final class DAOFactory {

    private static final UserDAO USER_DAO = new UserDAOImpl();
    private static final SellerDAO SELLER_DAO = new SellerDAOImpl();
    private static final CategoryDAO CATEGORY_DAO = new CategoryDAOImpl();
    private static final ProductDAO PRODUCT_DAO = new ProductDAOImpl();
    private static final CartDAO CART_DAO = new CartDAOImpl();
    private static final WishlistDAO WISHLIST_DAO = new WishlistDAOImpl();
    private static final OrderDAO ORDER_DAO = new OrderDAOImpl();
    private static final ReviewDAO REVIEW_DAO = new ReviewDAOImpl();
    private static final AddressDAO ADDRESS_DAO = new AddressDAOImpl();

    private DAOFactory() {
    }

    public static UserDAO getUserDAO() {
        return USER_DAO;
    }

    public static SellerDAO getSellerDAO() {
        return SELLER_DAO;
    }

    public static CategoryDAO getCategoryDAO() {
        return CATEGORY_DAO;
    }

    public static ProductDAO getProductDAO() {
        return PRODUCT_DAO;
    }

    public static CartDAO getCartDAO() {
        return CART_DAO;
    }

    public static WishlistDAO getWishlistDAO() {
        return WISHLIST_DAO;
    }

    public static OrderDAO getOrderDAO() {
        return ORDER_DAO;
    }

    public static ReviewDAO getReviewDAO() {
        return REVIEW_DAO;
    }

    public static AddressDAO getAddressDAO() {
        return ADDRESS_DAO;
    }
}
