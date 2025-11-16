import config.DatabaseConfig;
import ui.LoginPage;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Set Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Test database connection
        System.out.println("Testing database connection...");
        if (DatabaseConfig.testConnection()) {
            System.out.println("Database connection successful!");
            
            // Launch application
            SwingUtilities.invokeLater(() -> {
                LoginPage loginPage = new LoginPage();
                loginPage.setVisible(true);
            });
        } else {
            System.err.println("Database connection failed!");
            System.err.println("Please check:");
            System.err.println("1. MySQL server is running");
            System.err.println("2. Database 'smart_ecom' exists");
            System.err.println("3. Credentials in DatabaseConfig.java are correct");
            
            JOptionPane.showMessageDialog(null, 
                "Database connection failed!\nPlease check your MySQL configuration.", 
                "Connection Error", 
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
}