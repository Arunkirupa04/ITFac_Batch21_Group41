package utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

/**
 * Optional DB clean before test run (Option B). Runs once before the first scenario.
 * Disabled by default: set db.enabled=true and configure db.properties to enable.
 * On any error (missing config, connection failure, etc.) logs and returns without failing the test run.
 */
public final class DbCleanHelper {

    private static final String PROPS_RESOURCE = "db.properties";
    private static volatile boolean alreadyRun = false;
    private static final Object LOCK = new Object();

    private DbCleanHelper() {}

    /**
     * Run DB clean once per JVM. Safe to call from multiple threads/scenarios.
     * Does nothing if db.enabled is false or config is missing; never throws.
     */
    public static void runOnce() {
        if (alreadyRun) {
            return;
        }
        synchronized (LOCK) {
            if (alreadyRun) {
                return;
            }
            try {
                doRun();
            } catch (Throwable t) {
                System.err.println("[DbCleanHelper] DB clean skipped or failed (tests will continue): " + t.getMessage());
            } finally {
                alreadyRun = true;
            }
        }
    }

    private static void doRun() throws Exception {
        Properties p = loadProperties();
        if (p == null) {
            return;
        }
        String enabled = p.getProperty("db.enabled", "false").trim();
        if (!"true".equalsIgnoreCase(enabled)) {
            return;
        }
        String url = p.getProperty("db.url");
        if (url == null || url.isBlank()) {
            System.err.println("[DbCleanHelper] db.url not set in " + PROPS_RESOURCE + "; DB clean skipped.");
            return;
        }
        String user = p.getProperty("db.user", "");
        String password = p.getProperty("db.password", "");
        String tablesProp = p.getProperty("db.tables", "sale,plant,category").trim();
        if (tablesProp.isEmpty()) {
            return;
        }
        String[] tables = tablesProp.split(",");
        for (int i = 0; i < tables.length; i++) {
            tables[i] = tables[i].trim();
        }

        String driver = p.getProperty("db.driver");
        if (driver == null || driver.isBlank()) {
            if (url.contains(":h2:")) {
                driver = "org.h2.Driver";
            } else if (url.contains(":mysql:")) {
                driver = "com.mysql.cj.jdbc.Driver";
            } else if (url.contains(":postgresql:")) {
                driver = "org.postgresql.Driver";
            } else {
                System.err.println("[DbCleanHelper] Unknown DB URL; set db.driver in " + PROPS_RESOURCE);
                return;
            }
        }

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.err.println("[DbCleanHelper] JDBC driver not found: " + driver + " (add dependency to pom.xml if needed)");
            return;
        }

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            try (Statement st = conn.createStatement()) {
                for (String table : tables) {
                    if (table.isEmpty()) continue;
                    String safeTable = table.replaceAll("[^a-zA-Z0-9_]", "");
                    if (safeTable.isEmpty()) continue;
                    try {
                        if (url.contains(":h2:")) {
                            st.execute("TRUNCATE TABLE " + safeTable);
                        } else if (url.contains(":mysql:")) {
                            st.execute("SET FOREIGN_KEY_CHECKS = 0");
                            st.execute("TRUNCATE TABLE " + safeTable);
                            st.execute("SET FOREIGN_KEY_CHECKS = 1");
                        } else if (url.contains(":postgresql:")) {
                            st.execute("TRUNCATE TABLE " + safeTable + " CASCADE");
                        } else {
                            st.execute("DELETE FROM " + safeTable);
                        }
                    } catch (Exception e) {
                        System.err.println("[DbCleanHelper] Table " + safeTable + ": " + e.getMessage());
                    }
                }
            }
            System.out.println("[DbCleanHelper] DB clean completed.");
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception ignored) {}
            }
        }
    }

    private static Properties loadProperties() {
        try (InputStream is = DbCleanHelper.class.getClassLoader().getResourceAsStream(PROPS_RESOURCE)) {
            if (is == null) {
                return null;
            }
            Properties p = new Properties();
            p.load(is);
            return p;
        } catch (Exception e) {
            return null;
        }
    }
}
