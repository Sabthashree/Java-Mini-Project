package ui;

import dao.*;
import models.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CheckoutPage extends JFrame {
    private User currentUser;
    private HomePage homePage;
    private CartDAO cartDAO;
    private OrderDAO orderDAO;
    
    private JTextArea deliveryAddressArea;
    private ButtonGroup paymentGroup;
    private JRadioButton codRadio, netBankingRadio, upiRadio, emiRadio;
    private List<CartItem> cartItems;
    
    public CheckoutPage(User user, HomePage homePage) {
        this.currentUser = user;
        this.homePage = homePage;
        this.cartDAO = new CartDAO();
        this.orderDAO = new OrderDAO();
        // Initial load of cart items - this list will be re-fetched in createOrderSummary
        this.cartItems = cartDAO.getCartItems(currentUser.getUserId());
        
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Smart E-com - Checkout");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        // Header
        JPanel headerPanel = createHeader();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Content
        JPanel contentPanel = createContentPanel();
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(mainPanel);
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                homePage.setVisible(true);
            }
        });
    }
    
    private JPanel createHeader() {
        JPanel header = new JPanel(null);
        header.setPreferredSize(new Dimension(900, 60));
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
        
        JLabel titleLabel = new JLabel("Checkout");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(390, 15, 200, 30);
        header.add(titleLabel);
        
        return header;
    }
    
    private JPanel createContentPanel() {
        JPanel panel = new JPanel(new BorderLayout(30, 0));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        
        // Left: Delivery and Payment
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(new Color(245, 245, 245));
        
        // Delivery address section
        JPanel deliverySection = createDeliverySection();
        leftPanel.add(deliverySection);
        leftPanel.add(Box.createVerticalStrut(20));
        
        // Payment method section
        JPanel paymentSection = createPaymentSection();
        leftPanel.add(paymentSection);
        
        panel.add(leftPanel, BorderLayout.CENTER);
        
        // Right: Order summary
        JPanel summaryPanel = createOrderSummary();
        panel.add(summaryPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createDeliverySection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(Color.WHITE);
        section.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        section.setMaximumSize(new Dimension(500, 300));
        section.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel titleLabel = new JLabel("📍 Delivery Address");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.add(titleLabel);
        section.add(Box.createVerticalStrut(15));
        
        // User details
        JLabel nameLabel = new JLabel("Name: " + currentUser.getName());
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.add(nameLabel);
        section.add(Box.createVerticalStrut(8));
        
        JLabel phoneLabel = new JLabel("Phone: " + currentUser.getPhoneNo());
        phoneLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        phoneLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.add(phoneLabel);
        section.add(Box.createVerticalStrut(15));
        
        // Address
        JLabel addressLabel = new JLabel("Delivery Address:");
        addressLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        addressLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.add(addressLabel);
        section.add(Box.createVerticalStrut(5));
        
        deliveryAddressArea = new JTextArea(currentUser.getAddress());
        deliveryAddressArea.setFont(new Font("Arial", Font.PLAIN, 14));
        deliveryAddressArea.setLineWrap(true);
        deliveryAddressArea.setWrapStyleWord(true);
        JScrollPane addressScroll = new JScrollPane(deliveryAddressArea);
        addressScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        addressScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.add(addressScroll);
        section.add(Box.createVerticalStrut(15));
        
        // Change address button
        JButton changeAddressBtn = new JButton("Change Address");
        changeAddressBtn.setFont(new Font("Arial", Font.PLAIN, 13));
        changeAddressBtn.setMaximumSize(new Dimension(150, 35));
        changeAddressBtn.setBackground(Color.WHITE);
        changeAddressBtn.setBorder(BorderFactory.createLineBorder(new Color(74, 144, 226), 1));
        changeAddressBtn.setForeground(new Color(74, 144, 226));
        changeAddressBtn.setFocusPainted(false);
        changeAddressBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        changeAddressBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        changeAddressBtn.addActionListener(e -> changeAddress());
        section.add(changeAddressBtn);
        
        return section;
    }
    
    private JPanel createPaymentSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(Color.WHITE);
        section.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        section.setMaximumSize(new Dimension(500, 250));
        section.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel titleLabel = new JLabel("💳 Payment Method");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.add(titleLabel);
        section.add(Box.createVerticalStrut(15));
        
        paymentGroup = new ButtonGroup();
        
        // COD
        codRadio = new JRadioButton("Cash on Delivery (COD)");
        codRadio.setFont(new Font("Arial", Font.PLAIN, 14));
        codRadio.setBackground(Color.WHITE);
        codRadio.setAlignmentX(Component.LEFT_ALIGNMENT);
        codRadio.setSelected(true);
        paymentGroup.add(codRadio);
        section.add(codRadio);
        section.add(Box.createVerticalStrut(10));
        
        // Net Banking
        netBankingRadio = new JRadioButton("Net Banking");
        netBankingRadio.setFont(new Font("Arial", Font.PLAIN, 14));
        netBankingRadio.setBackground(Color.WHITE);
        netBankingRadio.setAlignmentX(Component.LEFT_ALIGNMENT);
        paymentGroup.add(netBankingRadio);
        section.add(netBankingRadio);
        section.add(Box.createVerticalStrut(10));
        
        // UPI
        upiRadio = new JRadioButton("UPI (GPay, PhonePe, Paytm)");
        upiRadio.setFont(new Font("Arial", Font.PLAIN, 14));
        upiRadio.setBackground(Color.WHITE);
        upiRadio.setAlignmentX(Component.LEFT_ALIGNMENT);
        paymentGroup.add(upiRadio);
        section.add(upiRadio);
        section.add(Box.createVerticalStrut(10));
        
        // EMI (only if cart total > 10000)
        double cartTotal = cartDAO.getCartTotal(currentUser.getUserId());
        emiRadio = new JRadioButton("EMI (Available for orders above ₹10,000)");
        emiRadio.setFont(new Font("Arial", Font.PLAIN, 14));
        emiRadio.setBackground(Color.WHITE);
        emiRadio.setAlignmentX(Component.LEFT_ALIGNMENT);
        emiRadio.setEnabled(cartTotal >= 10000);
        paymentGroup.add(emiRadio);
        section.add(emiRadio);
        
        return section;
    }
    
    private JPanel createOrderSummary() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        panel.setPreferredSize(new Dimension(300, 600));
        
        JLabel titleLabel = new JLabel("Order Summary");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(15));
        
        // **FIX: Re-fetch cart items to ensure data is fresh**
        cartItems = cartDAO.getCartItems(currentUser.getUserId());
        
        // Items list
        JLabel itemsLabel = new JLabel("Items (" + cartItems.size() + ")");
        itemsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        itemsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(itemsLabel);
        panel.add(Box.createVerticalStrut(10));
        
        for (CartItem item : cartItems) {
            JPanel itemPanel = new JPanel(new BorderLayout());
            itemPanel.setBackground(Color.WHITE);
            itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            
            JLabel nameLabel = new JLabel(item.getProduct().getProductName());
            nameLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            itemPanel.add(nameLabel, BorderLayout.WEST);
            
            JLabel qtyPriceLabel = new JLabel(item.getQuantity() + " x ₹" + 
                String.format("%.0f", item.getProduct().getDiscountedPrice()));
            qtyPriceLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            itemPanel.add(qtyPriceLabel, BorderLayout.EAST);
            
            panel.add(itemPanel);
            panel.add(Box.createVerticalStrut(5));
        }
        
        panel.add(Box.createVerticalStrut(15));
        
        // Price details
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        panel.add(separator);
        panel.add(Box.createVerticalStrut(15));
        
        // **FIX: Calculate subtotal from the fresh list**
        double subtotal = 0;
        for (CartItem item : cartItems) {
            subtotal += item.getSubtotal();
        }
        
        double deliveryCharge = subtotal >= 500 ? 0 : 50;
        double total = subtotal + deliveryCharge;
        
        JPanel subtotalPanel = createSummaryRow("Subtotal:", "₹" + String.format("%.0f", subtotal));
        panel.add(subtotalPanel);
        panel.add(Box.createVerticalStrut(10));
        
        JPanel deliveryPanel = createSummaryRow("Delivery Charge:", 
            deliveryCharge == 0 ? "FREE" : "₹" + String.format("%.0f", deliveryCharge));
        panel.add(deliveryPanel);
        panel.add(Box.createVerticalStrut(15));
        
        JSeparator separator2 = new JSeparator();
        separator2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        panel.add(separator2);
        panel.add(Box.createVerticalStrut(15));
        
        JPanel totalPanel = createSummaryRow("Total Amount:", "₹" + String.format("%.0f", total));
        JLabel totalLabel = (JLabel) ((JPanel) totalPanel.getComponent(1)).getComponent(0);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 20));
        totalLabel.setForeground(new Color(0, 128, 0));
        panel.add(totalPanel);
        panel.add(Box.createVerticalStrut(20));
        
        // Delivery info
        JLabel deliveryInfoLabel = new JLabel("🚚 Delivery within 7 days");
        deliveryInfoLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        deliveryInfoLabel.setForeground(new Color(0, 128, 0));
        deliveryInfoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(deliveryInfoLabel);
        panel.add(Box.createVerticalStrut(30));
        
        // Place order button
        JButton placeOrderBtn = new JButton("Place Order");
        placeOrderBtn.setFont(new Font("Arial", Font.BOLD, 16));
        placeOrderBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        placeOrderBtn.setBackground(new Color(74, 144, 226));
        placeOrderBtn.setForeground(Color.WHITE);
        placeOrderBtn.setFocusPainted(false);
        placeOrderBtn.setBorderPainted(false);
        placeOrderBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        placeOrderBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        placeOrderBtn.addActionListener(e -> placeOrder());
        panel.add(placeOrderBtn);
        
        return panel;
    }
    
    private JPanel createSummaryRow(String label, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(Color.WHITE);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        
        JLabel leftLabel = new JLabel(label);
        leftLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        row.add(leftLabel, BorderLayout.WEST);
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightPanel.setBackground(Color.WHITE);
        JLabel rightLabel = new JLabel(value);
        rightLabel.setFont(new Font("Arial", Font.BOLD, 14));
        rightPanel.add(rightLabel);
        row.add(rightPanel, BorderLayout.EAST);
        
        return row;
    }
    
    private void changeAddress() {
        String newAddress = JOptionPane.showInputDialog(this, 
            "Enter new delivery address:", deliveryAddressArea.getText());
        
        if (newAddress != null && !newAddress.trim().isEmpty()) {
            deliveryAddressArea.setText(newAddress);
        }
    }
    
    private void placeOrder() {
        String deliveryAddress = deliveryAddressArea.getText().trim();
        
        if (deliveryAddress.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter delivery address!", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Get selected payment method
        String paymentMethod = "COD";
        if (netBankingRadio.isSelected()) paymentMethod = "NetBanking";
        else if (upiRadio.isSelected()) paymentMethod = "UPI";
        else if (emiRadio.isSelected()) paymentMethod = "EMI";
        
        // Calculate totals (Recalculating one last time for safety)
        double subtotal = 0;
        List<CartItem> currentCartItems = cartDAO.getCartItems(currentUser.getUserId());
        for (CartItem item : currentCartItems) {
            subtotal += item.getSubtotal();
        }
        
        if (currentCartItems.isEmpty()) {
             JOptionPane.showMessageDialog(this, "Your cart is empty! Cannot place order.", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        double deliveryCharge = subtotal >= 500 ? 0 : 50;
        
        // Create order
        Order order = new Order(currentUser.getUserId(), subtotal, deliveryCharge, 
                               paymentMethod, deliveryAddress);
        
        int orderId = orderDAO.createOrder(order, currentCartItems);
        
        if (orderId > 0) {
            // Show confirmation page
            OrderConfirmationPage confirmationPage = new OrderConfirmationPage(
                currentUser, orderId, homePage);
            confirmationPage.setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Failed to place order! Please try again.", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}