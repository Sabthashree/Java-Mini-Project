// src/config/DatabaseConfig.java
package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    // Database credentials - CHANGE THESE ACCORDING TO YOUR MYSQL SETUP
    private static final String DB_URL = "jdbc:mysql://localhost:3306/smart_ecom";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Sanjay@20067"; // Change this!
    
    private static Connection connection = null;
    
    // Get database connection
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                // Load MySQL JDBC Driver
                Class.forName("com.mysql.cj.jdbc.Driver");
                
                // Establish connection
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                System.out.println("Database connected successfully!");
            }
            return connection;
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            System.err.println("Connection failed!");
            e.printStackTrace();
            return null;
        }
    }
    
    // Close database connection
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Test connection
    public static boolean testConnection() {
        Connection conn = getConnection();
        return conn != null;
    }
}