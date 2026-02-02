package edu.aitu.oop3.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DatabaseConnectionManager {
    private static DatabaseConnectionManager instance;

    private final String url;
    private final Properties props;

    private DatabaseConnectionManager() {

        String host = System.getenv("DB_HOST"); // мыс: abcdefg.supabase.co
        String db   = System.getenv().getOrDefault("DB_NAME", "postgres");
        String user = System.getenv("DB_USER");
        String pass = System.getenv("DB_PASS");

        this.url = "jdbc:postgresql://" + host + ":5432/" + db;

        props = new Properties();
        props.setProperty("user", user);
        props.setProperty("password", pass);
        props.setProperty("ssl", "true");
        props.setProperty("sslmode", "require");
    }

    public static synchronized DatabaseConnectionManager getInstance() {
        if (instance == null) {
            instance = new DatabaseConnectionManager();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, props);
    }
}
