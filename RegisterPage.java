package ui;

import dao.UserDAO;
import models.User;
import javax.swing.*;
import java.awt.*;

public class RegisterPage extends JFrame {
    private UserDAO userDAO;
    private JTextField nameField, emailField, phoneField;
    private JPasswordField passwordField, confirmPasswordField;
    private JTextArea addressArea;
    
    public RegisterPage() {
        userDAO = new UserDAO();
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Smart E-com - Register");
        setSize(500, 700);
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
        
        // Title
        JLabel titleLabel = new JLabel("Create Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(130, 30, 240, 40);
        mainPanel.add(titleLabel);
        
        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);
        formPanel.setBounds(50, 90, 400, 520);
        formPanel.setLayout(null);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Name
        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        nameLabel.setBounds(30, 20, 100, 20);
        formPanel.add(nameLabel);
        
        nameField = new JTextField();
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));
        nameField.setBounds(30, 45, 340, 30);
        nameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(nameField);
        
        // Email
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        emailLabel.setBounds(30, 85, 100, 20);
        formPanel.add(emailLabel);
        
        emailField = new JTextField();
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        emailField.setBounds(30, 110, 340, 30);
        emailField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(emailField);
        
        // Phone
        JLabel phoneLabel = new JLabel("Phone Number:");
        phoneLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        phoneLabel.setBounds(30, 150, 100, 20);
        formPanel.add(phoneLabel);
        
        phoneField = new JTextField();
        phoneField.setFont(new Font("Arial", Font.PLAIN, 14));
        phoneField.setBounds(30, 175, 340, 30);
        phoneField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(phoneField);
        
        // Address
        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        addressLabel.setBounds(30, 215, 100, 20);
        formPanel.add(addressLabel);
        
        addressArea = new JTextArea();
        addressArea.setFont(new Font("Arial", Font.PLAIN, 14));
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);
        addressArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        JScrollPane addressScroll = new JScrollPane(addressArea);
        addressScroll.setBounds(30, 240, 340, 60);
        formPanel.add(addressScroll);
        
        // Password
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        passwordLabel.setBounds(30, 310, 100, 20);
        formPanel.add(passwordLabel);
        
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBounds(30, 335, 340, 30);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(passwordField);
        
        // Confirm Password
        JLabel confirmLabel = new JLabel("Confirm Password:");
        confirmLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        confirmLabel.setBounds(30, 375, 150, 20);
        formPanel.add(confirmLabel);
        
        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setFont(new Font("Arial", Font.PLAIN, 14));
        confirmPasswordField.setBounds(30, 400, 340, 30);
        confirmPasswordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(confirmPasswordField);
        
        // Register button
        JButton registerButton = new JButton("Create Account");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBounds(30, 450, 340, 40);
        registerButton.setBackground(new Color(74, 144, 226));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setBorderPainted(false);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.addActionListener(e -> handleRegister());
        formPanel.add(registerButton);
        
        mainPanel.add(formPanel);
        
        // Back to login link
        JLabel backLabel = new JLabel("Already have an account?");
        backLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        backLabel.setForeground(Color.WHITE);
        backLabel.setBounds(130, 630, 170, 25);
        mainPanel.add(backLabel);
        
        JButton backButton = new JButton("Login");
        backButton.setFont(new Font("Arial", Font.BOLD, 13));
        backButton.setBounds(300, 630, 70, 25);
        backButton.setForeground(Color.WHITE);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> openLoginPage());
        mainPanel.add(backButton);
        
        add(mainPanel);
    }
    
    private void handleRegister() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String address = addressArea.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        
        // Validation
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || 
            address.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this, "Password must be at least 6 characters!", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (userDAO.emailExists(email)) {
            JOptionPane.showMessageDialog(this, "Email already registered!", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Create user
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPhoneNo(phone);
        user.setAddress(address);
        user.setPassword(password);
        
        if (userDAO.registerUser(user)) {
            JOptionPane.showMessageDialog(this, "Registration successful! Please login.", 
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
            openLoginPage();
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed! Please try again.", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void openLoginPage() {
        LoginPage loginPage = new LoginPage();
        loginPage.setVisible(true);
        this.dispose();
    }
}