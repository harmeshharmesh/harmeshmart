package com.harmesh.harmeshmart.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Database utility managing the HikariCP connection pool.
 * Conforms to Section 2 Mandatory Engineering Rule 5 & 6:
 * Singleton pool owned by AppContextListener.
 */
public final class DBUtil {

    private static final Logger logger = LoggerFactory.getLogger(DBUtil.class);
    private static volatile HikariDataSource dataSource;

    private DBUtil() {
    }

    /**
     * Initialize connection pool from config.properties.
     */
    public static synchronized void initialize() {
        if (dataSource != null && !dataSource.isClosed()) {
            return;
        }

        try {
            Properties props = new Properties();
            try (InputStream in = DBUtil.class.getClassLoader().getResourceAsStream("config.properties")) {
                if (in != null) {
                    props.load(in);
                } else {
                    logger.warn("config.properties not found on classpath, using defaults.");
                }
            }

            String driver = props.getProperty("db.driver", "org.h2.Driver");
            String url = props.getProperty("db.url", "jdbc:h2:file:./data/harmeshmart;DB_CLOSE_DELAY=-1;AUTO_SERVER=TRUE");
            String user = props.getProperty("db.username", "sa");
            String pass = props.getProperty("db.password", "");

            HikariConfig config = new HikariConfig();
            config.setDriverClassName(driver);
            config.setJdbcUrl(url);
            config.setUsername(user);
            config.setPassword(pass);
            config.setPoolName("HarmeshMart-HikariPool");

            int maxPool = Integer.parseInt(props.getProperty("db.pool.maximumPoolSize", "10"));
            int minIdle = Integer.parseInt(props.getProperty("db.pool.minimumIdle", "2"));
            config.setMaximumPoolSize(maxPool);
            config.setMinimumIdle(minIdle);
            config.setIdleTimeout(30000);
            config.setConnectionTimeout(20000);

            dataSource = new HikariDataSource(config);
            logger.info("HikariCP DataSource initialized successfully with URL: {}", url);

        } catch (Exception e) {
            logger.error("Failed to initialize HikariCP connection pool: {}", e.getMessage(), e);
            throw new RuntimeException("Could not initialize database connection pool", e);
        }
    }

    public static DataSource getDataSource() {
        if (dataSource == null) {
            initialize();
        }
        return dataSource;
    }

    /**
     * Get a pooled connection.
     * All calls MUST be enclosed in try-with-resources.
     */
    public static Connection getConnection() throws SQLException {
        if (dataSource == null || dataSource.isClosed()) {
            initialize();
        }
        return dataSource.getConnection();
    }

    /**
     * Shutdown the HikariCP connection pool.
     */
    public static synchronized void shutdown() {
        if (dataSource != null && !dataSource.isClosed()) {
            logger.info("Shutting down HikariCP DataSource...");
            dataSource.close();
            dataSource = null;
        }
    }

    /**
     * Execute SQL statements from a classpath resource script.
     */
    public static void executeSqlScript(String resourcePath) {
        try (InputStream in = DBUtil.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (in == null) {
                logger.warn("SQL script not found: {}", resourcePath);
                return;
            }

            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String trimmed = line.trim();
                    if (trimmed.startsWith("--") || trimmed.isEmpty()) {
                        continue;
                    }
                    sb.append(line).append("\n");
                }
            }

            String[] statements = sb.toString().split(";");
            try (Connection conn = getConnection();
                 Statement stmt = conn.createStatement()) {
                for (String sql : statements) {
                    String trimmed = sql.trim();
                    if (!trimmed.isEmpty()) {
                        stmt.execute(trimmed);
                    }
                }
                logger.info("Executed script {} successfully ({} statements)", resourcePath, statements.length);
            }
        } catch (Exception e) {
            logger.error("Error executing SQL script {}: {}", resourcePath, e.getMessage(), e);
            throw new RuntimeException("Failed executing SQL script " + resourcePath, e);
        }
    }
}
