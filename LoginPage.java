package ui;

import dao.UserDAO;
import models.User;
import javax.swing.*;
import java.awt.*;

public class LoginPage extends JFrame {
    private UserDAO userDAO;
    private JTextField emailField;
    private JPasswordField passwordField;
    
    public LoginPage() {
        userDAO = new UserDAO();
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Smart E-com - Login");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(74, 144, 226), 
                                                     0, getHeight(), new Color(138, 80, 229));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(null);
        
        // Logo/Title
        JLabel titleLabel = new JLabel("Smart E-com");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(120, 50, 260, 50);
        mainPanel.add(titleLabel);
        
        JLabel subtitleLabel = new JLabel("Login to your account");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setForeground(Color.WHITE);
        subtitleLabel.setBounds(150, 100, 200, 30);
        mainPanel.add(subtitleLabel);
        
        // Login form panel
        JPanel formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);
        formPanel.setBounds(50, 160, 400, 320);
        formPanel.setLayout(null);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Email field
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        emailLabel.setBounds(30, 30, 100, 25);
        formPanel.add(emailLabel);
        
        emailField = new JTextField();
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        emailField.setBounds(30, 60, 340, 35);
        emailField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(emailField);
        
        // Password field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordLabel.setBounds(30, 110, 100, 25);
        formPanel.add(passwordLabel);
        
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBounds(30, 140, 340, 35);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(passwordField);
        
        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBounds(30, 195, 340, 40);
        loginButton.setBackground(new Color(74, 144, 226));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(e -> handleLogin());
        formPanel.add(loginButton);
        
        // OR separator
        JLabel orLabel = new JLabel("OR");
        orLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        orLabel.setForeground(Color.GRAY);
        orLabel.setBounds(185, 245, 30, 20);
        formPanel.add(orLabel);
        
        // Google login button
        JButton googleButton = new JButton("Login with Google");
        googleButton.setFont(new Font("Arial", Font.PLAIN, 13));
        googleButton.setBounds(30, 275, 340, 35);
        googleButton.setBackground(Color.WHITE);
        googleButton.setForeground(Color.BLACK);
        googleButton.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        googleButton.setFocusPainted(false);
        googleButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        googleButton.addActionListener(e -> handleGoogleLogin());
        formPanel.add(googleButton);
        
        mainPanel.add(formPanel);
        
        // Create account link
        JLabel createAccountLabel = new JLabel("Don't have an account?");
        createAccountLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        createAccountLabel.setForeground(Color.WHITE);
        createAccountLabel.setBounds(140, 500, 150, 25);
        mainPanel.add(createAccountLabel);
        
        JButton createAccountButton = new JButton("Sign Up");
        createAccountButton.setFont(new Font("Arial", Font.BOLD, 13));
        createAccountButton.setBounds(290, 500, 80, 25);
        createAccountButton.setForeground(Color.WHITE);
        createAccountButton.setContentAreaFilled(false);
        createAccountButton.setBorderPainted(false);
        createAccountButton.setFocusPainted(false);
        createAccountButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        createAccountButton.addActionListener(e -> openRegisterPage());
        mainPanel.add(createAccountButton);
        
        add(mainPanel);
    }
    
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        User user = userDAO.loginUser(email, password);
        
        if (user != null) {
            JOptionPane.showMessageDialog(this, "Login successful!", 
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
            openHomePage(user);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid email or password!", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleGoogleLogin() {
        // Simplified Google login simulation
        String email = JOptionPane.showInputDialog(this, "Enter your Google email:");
        if (email != null && !email.isEmpty()) {
            String name = JOptionPane.showInputDialog(this, "Enter your name:");
            if (name != null && !name.isEmpty()) {
                String googleId = "google_" + System.currentTimeMillis();
                User user = userDAO.loginWithGoogle(googleId, name, email);
                
                if (user != null) {
                    JOptionPane.showMessageDialog(this, "Google login successful!", 
                                                "Success", JOptionPane.INFORMATION_MESSAGE);
                    openHomePage(user);
                }
            }
        }
    }
    
    private void openHomePage(User user) {
        HomePage homePage = new HomePage(user);
        homePage.setVisible(true);
        this.dispose();
    }
    
    private void openRegisterPage() {
        RegisterPage registerPage = new RegisterPage();
        registerPage.setVisible(true);
        this.dispose();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginPage loginPage = new LoginPage();
            loginPage.setVisible(true);
        });
    }
}
