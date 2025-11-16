package ui;

import dao.OrderDAO;
import models.Order;
import models.User;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;

public class OrderConfirmationPage extends JFrame {
    private int orderId;
    private HomePage homePage;
    private OrderDAO orderDAO;
    
    public OrderConfirmationPage(User user, int orderId, HomePage homePage) {
        this.orderId = orderId;
        this.homePage = homePage;
        this.orderDAO = new OrderDAO();
        
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Smart E-com - Order Confirmed");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        // Success icon
        JLabel successIcon = new JLabel("✓", SwingConstants.CENTER);
        successIcon.setFont(new Font("Arial", Font.BOLD, 100));
        successIcon.setForeground(new Color(0, 180, 0));
        successIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(successIcon);
        mainPanel.add(Box.createVerticalStrut(20));
        
        // Success message
        JLabel successLabel = new JLabel("Order Placed Successfully!");
        successLabel.setFont(new Font("Arial", Font.BOLD, 28));
        successLabel.setForeground(new Color(0, 128, 0));
        successLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(successLabel);
        mainPanel.add(Box.createVerticalStrut(15));
        
        // Order ID
        JLabel orderIdLabel = new JLabel("Order ID: #" + orderId);
        orderIdLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        orderIdLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(orderIdLabel);
        mainPanel.add(Box.createVerticalStrut(30));
        
        // Order details
        Order order = orderDAO.getOrderById(orderId);
        if (order != null) {
            JPanel detailsPanel = new JPanel();
            detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
            detailsPanel.setBackground(new Color(245, 245, 245));
            detailsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(20, 30, 20, 30)
            ));
            detailsPanel.setMaximumSize(new Dimension(500, 300)); // Increased height
            detailsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
            
            // --- PRICE BREAKDOWN (THE FIX) ---
            addDetailRow(detailsPanel, "Subtotal:", "₹" + String.format("%.0f", order.getTotalAmount()));
            String deliveryValue = order.getDeliveryCharge() == 0 ? "FREE" : "₹" + String.format("%.0f", order.getDeliveryCharge());
            addDetailRow(detailsPanel, "Delivery Charge:", deliveryValue);
            addSeparator(detailsPanel);
            
            double finalTotal = order.getTotalAmount() + order.getDeliveryCharge();
            // Displaying the final calculated total
            addDetailRow(detailsPanel, "Total Amount:", "₹" + String.format("%.0f", finalTotal));
            
            // Style the Total Amount row for emphasis
            try {
                // The last row is the total row
                JPanel totalRow = (JPanel) detailsPanel.getComponent(detailsPanel.getComponentCount() - 1);
                // The price value is the second component in the row
                JLabel valueComp = (JLabel) totalRow.getComponent(1);
                valueComp.setFont(new Font("Arial", Font.BOLD, 18));
                valueComp.setForeground(new Color(0, 128, 0));
                
                // The label is the first component in the row
                JLabel labelComp = (JLabel) totalRow.getComponent(0);
                labelComp.setFont(new Font("Arial", Font.BOLD, 16));
            } catch (Exception ex) {
                // Ignore font setting errors
            }
            // --- END PRICE BREAKDOWN ---
            
            addSeparator(detailsPanel); // Separate price details from delivery info
            
            addDetailRow(detailsPanel, "Payment Method:", order.getPaymentMethod());
            addDetailRow(detailsPanel, "Delivery Address:", order.getDeliveryAddress());
            addDetailRow(detailsPanel, "Expected Delivery:", 
                dateFormat.format(order.getExpectedDeliveryDate()));
            
            mainPanel.add(detailsPanel);
        }
        
        mainPanel.add(Box.createVerticalStrut(40));
        
        // Thank you message
        JLabel thankYouLabel = new JLabel("Thank you for shopping with us!");
        thankYouLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        thankYouLabel.setForeground(Color.GRAY);
        thankYouLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(thankYouLabel);
        mainPanel.add(Box.createVerticalStrut(30));
        
        // Buttons
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonsPanel.setBackground(Color.WHITE);
        buttonsPanel.setMaximumSize(new Dimension(500, 50));
        
        JButton continueBtn = new JButton("Continue Shopping");
        continueBtn.setFont(new Font("Arial", Font.BOLD, 14));
        continueBtn.setPreferredSize(new Dimension(200, 45));
        continueBtn.setBackground(new Color(74, 144, 226));
        continueBtn.setForeground(Color.WHITE);
        continueBtn.setFocusPainted(false);
        continueBtn.setBorderPainted(false);
        continueBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        continueBtn.addActionListener(e -> {
            homePage.setVisible(true);
            homePage.updateCartCount();
            dispose();
        });
        buttonsPanel.add(continueBtn);
        
        mainPanel.add(buttonsPanel);
        
        add(mainPanel);
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                homePage.setVisible(true);
                homePage.updateCartCount();
            }
        });
    }
    
    private void addSeparator(JPanel panel) {
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        separator.setForeground(new Color(200, 200, 200));
        panel.add(Box.createVerticalStrut(5));
        panel.add(separator);
        panel.add(Box.createVerticalStrut(5));
    }
    
    private void addDetailRow(JPanel panel, String label, String value) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        row.setBackground(new Color(245, 245, 245));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        
        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font("Arial", Font.BOLD, 14));
        labelComp.setPreferredSize(new Dimension(150, 25));
        row.add(labelComp);
        
        JLabel valueComp = new JLabel(value);
        valueComp.setFont(new Font("Arial", Font.PLAIN, 14));
        row.add(valueComp);
        
        panel.add(row);
    }
}