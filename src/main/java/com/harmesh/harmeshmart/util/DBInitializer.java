package com.harmesh.harmeshmart.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Automatically creates all tables and seeds realistic catalog items priced in INR (₹)
 * upon Tomcat startup.
 * Satisfies User Specification for DBInitializer with schema.sql & data.sql.
 */
public final class DBInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DBInitializer.class);

    private DBInitializer() {
    }

    /**
     * Entrypoint called by AppContextListener during application startup.
     */
    public static synchronized void initialize() {
        logger.info("Starting HarmeshMart DBInitializer...");

        try {
            DBUtil.initialize();

            boolean tablesExist = checkTableExists("USERS");

            if (!tablesExist) {
                logger.info("Executing schema.sql to construct database tables...");
                DBUtil.executeSqlScript("schema.sql");

                logger.info("Executing data.sql to seed realistic catalog & Indian ₹ items...");
                DBUtil.executeSqlScript("data.sql");

                // Also try seed.sql if data.sql didn't seed or as fallback
                if (!checkTableExists("PRODUCTS") || countRows("PRODUCTS") == 0) {
                    DBUtil.executeSqlScript("seed.sql");
                }
            } else {
                logger.info("HarmeshMart database tables already exist. Verifying required tables and views...");
            }

            // Create convenience views for 'cart' and 'wishlist' aliases
            createAliasViews();

            // Ensure the 3 required seed roles & users exist with exact requested credentials
            ensureRequiredUsers();

            // Advance auto-increment sequences past initial seeds
            advanceSequences();

            logger.info("DBInitializer completed successfully. All tables and seed catalog ready.");

        } catch (Exception e) {
            logger.error("DBInitializer failed: {}", e.getMessage(), e);
        }
    }

    private static boolean checkTableExists(String tableName) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT count(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='PUBLIC' AND (UPPER(TABLE_NAME)=? OR UPPER(TABLE_NAME)=?)")) {
            ps.setString(1, tableName.toUpperCase());
            ps.setString(2, tableName.toLowerCase());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            logger.warn("Could not check table existence for {}: {}", tableName, e.getMessage());
        }
        return false;
    }

    private static int countRows(String tableName) {
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT count(*) FROM " + tableName)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            logger.warn("Could not count rows in {}: {}", tableName, e.getMessage());
        }
        return 0;
    }

    private static void createAliasViews() {
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE VIEW IF NOT EXISTS cart AS SELECT * FROM cart_items");
            stmt.execute("CREATE VIEW IF NOT EXISTS wishlist AS SELECT * FROM wishlist_items");
        } catch (SQLException e) {
            logger.debug("View alias creation note: {}", e.getMessage());
        }
    }

    private static void ensureRequiredUsers() {
        // Admin: admin@harmeshmart.com / admin123
        // Seller: seller@harmeshmart.com / seller123
        // Buyer: buyer@harmeshmart.com / buyer123
        String adminHash = "$2a$10$mosllc3Jja2nJhzBf65O7Ou9tHU9LHo0cLpBUac/M9YsMdT40Sn/K"; // admin123
        String sellerHash = "$2a$10$0wnriCutd5.eFGHu.MS1vetdKBFIdfRKhy1SKMtTW46mazj6tYVfu"; // seller123
        String buyerHash = "$2a$10$3EpeYK7GwwIT32VqUPnUVudHxX6NNuPTal8EIZHljCNbj3AUL8aPy"; // buyer123

        upsertUser("admin@harmeshmart.com", "HarmeshMart Administrator", adminHash, "ADMIN", "+91 9876543210");
        long sellerUserId = upsertUser("seller@harmeshmart.com", "Harmesh Verified Seller", sellerHash, "SELLER", "+91 9811223344");
        upsertUser("buyer@harmeshmart.com", "Harmesh Kumar", buyerHash, "BUYER", "+91 9844556677");

        // Ensure seller profile exists for seller@harmeshmart.com
        if (sellerUserId > 0) {
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement checkPs = conn.prepareStatement("SELECT count(*) FROM sellers WHERE user_id = ?")) {
                checkPs.setLong(1, sellerUserId);
                try (ResultSet rs = checkPs.executeQuery()) {
                    if (rs.next() && rs.getInt(1) == 0) {
                        try (PreparedStatement insPs = conn.prepareStatement(
                                "INSERT INTO sellers (user_id, store_name, store_description, contact_email, phone, verified, rating) " +
                                "VALUES (?, 'Harmesh Official Merchant Store', 'Premier verified seller offering fast dispatch and genuine Indian warranty.', 'seller@harmeshmart.com', '+91 9811223344', TRUE, 4.95)")) {
                            insPs.setLong(1, sellerUserId);
                            insPs.executeUpdate();
                            logger.info("Created seller profile for seller@harmeshmart.com (user_id: {})", sellerUserId);
                        }
                    }
                }
            } catch (SQLException e) {
                logger.warn("Error ensuring seller profile: {}", e.getMessage());
            }
        }
    }

    private static long upsertUser(String email, String name, String hash, String role, String phone) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement check = conn.prepareStatement("SELECT id FROM users WHERE LOWER(email) = LOWER(?)")) {
            check.setString(1, email);
            try (ResultSet rs = check.executeQuery()) {
                if (rs.next()) {
                    long id = rs.getLong("id");
                    // Update password hash to guarantee match with required test password
                    try (PreparedStatement upd = conn.prepareStatement("UPDATE users SET password_hash = ?, role = ? WHERE id = ?")) {
                        upd.setString(1, hash);
                        upd.setString(2, role);
                        upd.setLong(3, id);
                        upd.executeUpdate();
                    }
                    return id;
                } else {
                    try (PreparedStatement ins = conn.prepareStatement(
                            "INSERT INTO users (name, email, password_hash, role, phone) VALUES (?, ?, ?, ?, ?)",
                            Statement.RETURN_GENERATED_KEYS)) {
                        ins.setString(1, name);
                        ins.setString(2, email);
                        ins.setString(3, hash);
                        ins.setString(4, role);
                        ins.setString(5, phone);
                        ins.executeUpdate();
                        try (ResultSet gk = ins.getGeneratedKeys()) {
                            if (gk.next()) {
                                return gk.getLong(1);
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            logger.warn("Could not upsert user {}: {}", email, e.getMessage());
        }
        return -1;
    }

    private static void advanceSequences() {
        String[] tables = {
                "users", "sellers", "categories", "products", "addresses",
                "orders", "order_items", "reviews", "wishlist_items", "cart_items"
        };
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement()) {
            for (String table : tables) {
                try {
                    stmt.execute("ALTER TABLE " + table + " ALTER COLUMN id RESTART WITH 100");
                } catch (SQLException ignored) {
                }
            }
        } catch (SQLException e) {
            logger.debug("Sequence advance notice: {}", e.getMessage());
        }
    }
}
