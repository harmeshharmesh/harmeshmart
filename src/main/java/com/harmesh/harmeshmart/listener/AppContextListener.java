package com.harmesh.harmeshmart.listener;

import com.harmesh.harmeshmart.util.DBInitializer;
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
            // Initialize Database and Seed Data via DBInitializer
            DBInitializer.initialize();
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
