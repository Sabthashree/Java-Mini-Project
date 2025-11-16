package ui;

import dao.*;
import models.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.text.SimpleDateFormat;

public class UserProfilePage extends JFrame {
    private User currentUser;
    private HomePage homePage;
    private UserDAO userDAO;
    private OrderDAO orderDAO;
    
    private JTextField nameField, phoneField;
    private JTextArea addressArea;
    
    public UserProfilePage(User user, HomePage homePage) {
        this.currentUser = user;
        this.homePage = homePage;
        this.userDAO = new UserDAO();
        this.orderDAO = new OrderDAO();
        
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Smart E-com - My Profile");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        // Header
        JPanel headerPanel = createHeader();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Content with tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 14));
        
        tabbedPane.addTab("Profile Details", createProfileTab());
        tabbedPane.addTab("Order History", createOrderHistoryTab());
        tabbedPane.addTab("Browsing History", createBrowsingHistoryTab());
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        add(mainPanel);
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                homePage.setVisible(true);
            }
        });
    }
    
    private JPanel createHeader() {
        JPanel header = new JPanel(null);
        header.setPreferredSize(new Dimension(1000, 60));
        header.setBackground(new Color(74, 144, 226));
        
        JButton backButton = new JButton("← Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.setBounds(20, 15, 100, 30);
        backButton.setForeground(Color.WHITE);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            homePage.setVisible(true);
            dispose();
        });
        header.add(backButton);
        
        JLabel titleLabel = new JLabel("My Profile");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(430, 15, 200, 30);
        header.add(titleLabel);
        
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("Arial", Font.PLAIN, 14));
        logoutBtn.setBounds(860, 15, 100, 30);
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setContentAreaFilled(false);
        logoutBtn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> logout());
        header.add(logoutBtn);
        
        return header;
    }
    
    private JPanel createProfileTab() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        
        // Profile info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        infoPanel.setMaximumSize(new Dimension(700, 500));
        infoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel titleLabel = new JLabel("Personal Information");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(titleLabel);
        infoPanel.add(Box.createVerticalStrut(30));
        
        // Name
        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        
        nameField = new JTextField(currentUser.getName());
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        nameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(nameField);
        infoPanel.add(Box.createVerticalStrut(15));
        
        // Email (read-only)
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(emailLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        
        JTextField emailField = new JTextField(currentUser.getEmail());
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        emailField.setAlignmentX(Component.LEFT_ALIGNMENT);
        emailField.setEditable(false);
        emailField.setBackground(new Color(240, 240, 240));
        infoPanel.add(emailField);
        infoPanel.add(Box.createVerticalStrut(15));
        
        // Phone
        JLabel phoneLabel = new JLabel("Phone Number:");
        phoneLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        phoneLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(phoneLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        
        phoneField = new JTextField(currentUser.getPhoneNo());
        phoneField.setFont(new Font("Arial", Font.PLAIN, 14));
        phoneField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        phoneField.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(phoneField);
        infoPanel.add(Box.createVerticalStrut(15));
        
        // Address
        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        addressLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(addressLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        
        addressArea = new JTextArea(currentUser.getAddress());
        addressArea.setFont(new Font("Arial", Font.PLAIN, 14));
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);
        JScrollPane addressScroll = new JScrollPane(addressArea);
        addressScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        addressScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(addressScroll);
        infoPanel.add(Box.createVerticalStrut(25));
        
        // Update button
        JButton updateBtn = new JButton("Update Profile");
        updateBtn.setFont(new Font("Arial", Font.BOLD, 14));
        updateBtn.setMaximumSize(new Dimension(200, 40));
        updateBtn.setBackground(new Color(74, 144, 226));
        updateBtn.setForeground(Color.WHITE);
        updateBtn.setFocusPainted(false);
        updateBtn.setBorderPainted(false);
        updateBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        updateBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        updateBtn.addActionListener(e -> updateProfile());
        infoPanel.add(updateBtn);
        
        panel.add(infoPanel);
        
        return panel;
    }
    
    private JPanel createOrderHistoryTab() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        
        List<Order> orders = orderDAO.getOrdersByUser(currentUser.getUserId());
        
        if (orders.isEmpty()) {
            JLabel emptyLabel = new JLabel("No orders yet");
            emptyLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(Box.createVerticalStrut(100));
            panel.add(emptyLabel);
        } else {
            JLabel titleLabel = new JLabel(orders.size() + " Orders");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
            titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(titleLabel);
            panel.add(Box.createVerticalStrut(20));
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
            
            for (Order order : orders) {
                JPanel orderPanel = createOrderPanel(order, dateFormat);
                panel.add(orderPanel);
                panel.add(Box.createVerticalStrut(15));
            }
        }
        
        return panel;
    }
    
    private JPanel createOrderPanel(Order order, SimpleDateFormat dateFormat) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        
        JLabel orderIdLabel = new JLabel("Order #" + order.getOrderId());
        orderIdLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(orderIdLabel, BorderLayout.WEST);
        
        JLabel statusLabel = new JLabel(order.getOrderStatus());
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statusLabel.setForeground(getStatusColor(order.getOrderStatus()));
        headerPanel.add(statusLabel, BorderLayout.EAST);
        
        panel.add(headerPanel);
        panel.add(Box.createVerticalStrut(10));
        
        // Date
        JLabel dateLabel = new JLabel("Ordered on: " + dateFormat.format(order.getOrderDate()));
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        dateLabel.setForeground(Color.GRAY);
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(dateLabel);
        panel.add(Box.createVerticalStrut(5));
        
        // Expected delivery
        JLabel deliveryLabel = new JLabel("Expected Delivery: " + dateFormat.format(order.getExpectedDeliveryDate()));
        deliveryLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        deliveryLabel.setForeground(Color.GRAY);
        deliveryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(deliveryLabel);
        panel.add(Box.createVerticalStrut(10));
        
        // Items
        JLabel itemsLabel = new JLabel("Items: " + order.getOrderItems().size());
        itemsLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        itemsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(itemsLabel);
        panel.add(Box.createVerticalStrut(10));
        
        // Total
        JLabel totalLabel = new JLabel("Total: ₹" + String.format("%.0f", order.getTotalAmount() + order.getDeliveryCharge()));
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalLabel.setForeground(new Color(0, 128, 0));
        totalLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(totalLabel);
        
        return panel;
    }
    
    private Color getStatusColor(String status) {
        switch (status) {
            case "Delivered": return new Color(0, 128, 0);
            case "Shipped": return new Color(0, 100, 200);
            case "Pending": return new Color(255, 153, 0);
            case "Cancelled": return Color.RED;
            default: return Color.BLACK;
        }
    }
    
    private JPanel createBrowsingHistoryTab() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        
        JLabel infoLabel = new JLabel("Your browsing history will appear here");
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        infoLabel.setForeground(Color.GRAY);
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createVerticalStrut(100));
        panel.add(infoLabel);
        
        return panel;
    }
    
    private void updateProfile() {
        currentUser.setName(nameField.getText().trim());
        currentUser.setPhoneNo(phoneField.getText().trim());
        currentUser.setAddress(addressArea.getText().trim());
        
        if (userDAO.updateUser(currentUser)) {
            JOptionPane.showMessageDialog(this, 
                "Profile updated successfully!", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Failed to update profile!", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to logout?", 
            "Confirm Logout", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            LoginPage loginPage = new LoginPage();
            loginPage.setVisible(true);
            homePage.dispose();
            this.dispose();
        }
    }
}