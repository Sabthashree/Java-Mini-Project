package ui;

import dao.*;
import models.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import utils.ImageSearchUtil; // NEW IMPORT

public class CartPage extends JFrame {
    private User currentUser;
    private HomePage homePage;
    private CartDAO cartDAO;
    private JPanel itemsPanel;
    private JLabel totalLabel;
    
    public CartPage(User user, HomePage homePage) {
        this.currentUser = user;
        this.homePage = homePage;
        this.cartDAO = new CartDAO();
        
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Smart E-com - Shopping Cart");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        // Header
        JPanel headerPanel = createHeader();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Content
        JPanel contentPanel = new JPanel(new BorderLayout(20, 0));
        contentPanel.setBackground(new Color(245, 245, 245));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        
        // Cart items
        itemsPanel = new JPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
        itemsPanel.setBackground(Color.WHITE);
        loadCartItems();
        
        JScrollPane scrollPane = new JScrollPane(itemsPanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Summary panel
        JPanel summaryPanel = createSummaryPanel();
        contentPanel.add(summaryPanel, BorderLayout.EAST);
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel);
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                homePage.setVisible(true);
                homePage.updateCartCount();
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
            homePage.updateCartCount();
            dispose();
        });
        header.add(backButton);
        
        JLabel titleLabel = new JLabel("Shopping Cart");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(420, 15, 200, 30);
        header.add(titleLabel);
        
        return header;
    }
    
    private void loadCartItems() {
        itemsPanel.removeAll();
        
        List<CartItem> cartItems = cartDAO.getCartItems(currentUser.getUserId());
        
        if (cartItems.isEmpty()) {
            JLabel emptyLabel = new JLabel("Your cart is empty");
            emptyLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            itemsPanel.add(Box.createVerticalStrut(100));
            itemsPanel.add(emptyLabel);
        } else {
            for (CartItem item : cartItems) {
                JPanel itemPanel = createCartItemPanel(item);
                itemsPanel.add(itemPanel);
                itemsPanel.add(Box.createVerticalStrut(10));
            }
        }
        
        itemsPanel.revalidate();
        itemsPanel.repaint();
    }
    
    private JPanel createCartItemPanel(CartItem item) {
        JPanel panel = new JPanel(new BorderLayout(15, 0));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        
        Product product = item.getProduct();
        
        // Image -> REPLACED WITH IMAGE LOADING
        JLabel imageLabel = new JLabel();
        // FIX: Removed "assets/images/" prefix
        ImageIcon productIcon = ImageSearchUtil.loadAndScaleImage(
            product.getFirstImage(), 80, 80);
        
        if (productIcon.getIconWidth() > 0) {
            imageLabel.setIcon(productIcon);
        } else {
            imageLabel.setText("📦");
            imageLabel.setFont(new Font("Arial", Font.PLAIN, 50));
        }

        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(80, 80));
        panel.add(imageLabel, BorderLayout.WEST);
        
        // Details
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(Color.WHITE);
        
        JLabel nameLabel = new JLabel(product.getProductName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        detailsPanel.add(nameLabel);
        detailsPanel.add(Box.createVerticalStrut(5));
        
        JLabel priceLabel = new JLabel("₹" + String.format("%.0f", product.getDiscountedPrice()) + " each");
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        priceLabel.setForeground(Color.GRAY);
        detailsPanel.add(priceLabel);
        detailsPanel.add(Box.createVerticalStrut(10));
        
        // Quantity controls
        JPanel qtyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        qtyPanel.setBackground(Color.WHITE);
        
        JButton minusBtn = new JButton("-");
        minusBtn.setPreferredSize(new Dimension(40, 30));
        minusBtn.setFocusPainted(false);
        minusBtn.addActionListener(e -> updateQuantity(item, -1));
        qtyPanel.add(minusBtn);
        
        JLabel qtyLabel = new JLabel(String.valueOf(item.getQuantity()));
        qtyLabel.setFont(new Font("Arial", Font.BOLD, 14));
        qtyLabel.setPreferredSize(new Dimension(40, 30));
        qtyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        qtyPanel.add(qtyLabel);
        
        JButton plusBtn = new JButton("+");
        plusBtn.setPreferredSize(new Dimension(40, 30));
        plusBtn.setFocusPainted(false);
        plusBtn.addActionListener(e -> updateQuantity(item, 1));
        qtyPanel.add(plusBtn);
        
        detailsPanel.add(qtyPanel);
        panel.add(detailsPanel, BorderLayout.CENTER);
        
        // Right panel (price and remove)
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.WHITE);
        
        JLabel subtotalLabel = new JLabel("₹" + String.format("%.0f", item.getSubtotal()));
        subtotalLabel.setFont(new Font("Arial", Font.BOLD, 18));
        subtotalLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        rightPanel.add(subtotalLabel);
        rightPanel.add(Box.createVerticalStrut(20));
        
        JButton removeBtn = new JButton("🗑️ Remove");
        removeBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        removeBtn.setForeground(Color.RED);
        removeBtn.setContentAreaFilled(false);
        removeBtn.setBorderPainted(false);
        removeBtn.setFocusPainted(false);
        removeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        removeBtn.setAlignmentX(Component.RIGHT_ALIGNMENT);
        removeBtn.addActionListener(e -> removeItem(item));
        rightPanel.add(removeBtn);
        
        panel.add(rightPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    private void updateQuantity(CartItem item, int change) {
        int newQty = item.getQuantity() + change;
        
        if (newQty <= 0) {
            removeItem(item);
            return;
        }
        
        if (newQty > item.getProduct().getStockQuantity()) {
            JOptionPane.showMessageDialog(this, "Cannot exceed available stock!");
            return;
        }
        
        if (cartDAO.setCartQuantity(currentUser.getUserId(), item.getProductId(), newQty)) {
            loadCartItems();
            updateTotal();
        }
    }
    
    private void removeItem(CartItem item) {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Remove this item from cart?", "Confirm", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (cartDAO.removeFromCart(currentUser.getUserId(), item.getProductId())) {
                loadCartItems();
                updateTotal();
                homePage.updateCartCount();
            }
        }
    }
    
    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        panel.setPreferredSize(new Dimension(300, 400));
        
        JLabel titleLabel = new JLabel("Order Summary");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(20));
        
        // Subtotal
        double cartTotal = cartDAO.getCartTotal(currentUser.getUserId());
        JPanel subtotalPanel = createSummaryRow("Subtotal:", "₹" + String.format("%.0f", cartTotal));
        panel.add(subtotalPanel);
        panel.add(Box.createVerticalStrut(10));
        
        // Delivery charge
        double deliveryCharge = cartTotal > 0 ? (cartTotal >= 500 ? 0 : 50) : 0;
        JPanel deliveryPanel = createSummaryRow("Delivery:", 
            deliveryCharge == 0 ? "FREE" : "₹" + String.format("%.0f", deliveryCharge));
        panel.add(deliveryPanel);
        panel.add(Box.createVerticalStrut(15));
        
        // Divider
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        panel.add(separator);
        panel.add(Box.createVerticalStrut(15));
        
        // Total
        double finalTotal = cartTotal + deliveryCharge;
        JPanel totalPanel = createSummaryRow("Total:", "₹" + String.format("%.0f", finalTotal));
        totalLabel = (JLabel) ((JPanel) totalPanel.getComponent(1)).getComponent(0);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 20));
        totalLabel.setForeground(new Color(0, 128, 0));
        panel.add(totalPanel);
        panel.add(Box.createVerticalStrut(30));
        
        // Checkout button
        JButton checkoutBtn = new JButton("Proceed to Checkout");
        checkoutBtn.setFont(new Font("Arial", Font.BOLD, 14));
        checkoutBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        checkoutBtn.setBackground(new Color(74, 144, 226));
        checkoutBtn.setForeground(Color.WHITE);
        checkoutBtn.setFocusPainted(false);
        checkoutBtn.setBorderPainted(false);
        checkoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        checkoutBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        checkoutBtn.addActionListener(e -> proceedToCheckout());
        panel.add(checkoutBtn);
        panel.add(Box.createVerticalStrut(15));
        
        // Continue shopping button
        JButton continueBtn = new JButton("Continue Shopping");
        continueBtn.setFont(new Font("Arial", Font.PLAIN, 13));
        continueBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        continueBtn.setBackground(Color.WHITE);
        continueBtn.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        continueBtn.setFocusPainted(false);
        continueBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        continueBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        continueBtn.addActionListener(e -> {
            homePage.setVisible(true);
            dispose();
        });
        panel.add(continueBtn);
        
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
    
    private void updateTotal() {
        double cartTotal = cartDAO.getCartTotal(currentUser.getUserId());
        double deliveryCharge = cartTotal > 0 ? (cartTotal >= 500 ? 0 : 50) : 0;
        double finalTotal = cartTotal + deliveryCharge;
        
        if (totalLabel != null) {
            totalLabel.setText("₹" + String.format("%.0f", finalTotal));
        }
    }
    
    private void proceedToCheckout() {
        List<CartItem> items = cartDAO.getCartItems(currentUser.getUserId());
        
        if (items.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your cart is empty!");
            return;
        }
        
        CheckoutPage checkoutPage = new CheckoutPage(currentUser, homePage);
        checkoutPage.setVisible(true);
        this.dispose();
    }
}