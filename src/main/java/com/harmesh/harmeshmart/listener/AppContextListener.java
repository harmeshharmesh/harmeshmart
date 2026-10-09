package com.harmesh.harmeshmart.listener;

import com.harmesh.harmeshmart.util.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Application Lifecycle Listener managing connection pool and database startup/teardown.
 * Conforms to Section 2 Mandatory Engineering Rule 5.
 */
@WebListener
public class AppContextListener implements ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(AppContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("Initializing HarmeshMart Application Context...");

        try {
            // 1. Initialize Connection Pool
            DBUtil.initialize();

            // 2. Check if tables are created, if not run schema and seed
            boolean tablesExist = false;
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement ps = conn.prepareStatement(
                         "SELECT count(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='PUBLIC' AND TABLE_NAME='USERS'")) {
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        tablesExist = true;
                    }
                }
            } catch (Exception e) {
                logger.info("Database check: {}", e.getMessage());
            }

            if (!tablesExist) {
                logger.info("Tables not found. Running schema.sql and seed.sql...");
                DBUtil.executeSqlScript("schema.sql");
                DBUtil.executeSqlScript("seed.sql");
                logger.info("HarmeshMart schema and seed data loaded successfully.");
            } else {
                logger.info("HarmeshMart database tables already present.");
            }

        } catch (Exception e) {
            logger.error("Failed to initialize database during startup: {}", e.getMessage(), e);
        }

        logger.info("HarmeshMart Application Context initialized successfully.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("Shutting down HarmeshMart Application Context...");
        DBUtil.shutdown();
        logger.info("HarmeshMart Application Context destroyed.");
    }
}
