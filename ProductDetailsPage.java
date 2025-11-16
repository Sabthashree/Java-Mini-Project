package ui;

import dao.*;
import models.*;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ProductDetailsPage extends JFrame {
    private User currentUser;
    private Product product;
    private HomePage homePage;
    private ProductDAO productDAO;
    private CartDAO cartDAO;
    private WishlistDAO wishlistDAO;
    private JSpinner quantitySpinner;
    private JCheckBox giftWrapCheckBox;
    
    public ProductDetailsPage(User user, Product product, HomePage homePage) {
        this.currentUser = user;
        this.product = product;
        this.homePage = homePage;
        this.productDAO = new ProductDAO();
        this.cartDAO = new CartDAO();
        this.wishlistDAO = new WishlistDAO();
        
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Smart E-com - " + product.getProductName());
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        // Header
        JPanel headerPanel = createHeader();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Content
        JScrollPane scrollPane = new JScrollPane(createContentPanel());
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(mainPanel);
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                homePage.setVisible(true);
                homePage.updateCartCount();
                homePage.updateWishlistCount();
            }
        });
    }
    
    private JPanel createHeader() {
        JPanel header = new JPanel(null);
        header.setPreferredSize(new Dimension(1200, 60));
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
            homePage.updateWishlistCount();
            dispose();
        });
        header.add(backButton);
        
        JLabel titleLabel = new JLabel("Product Details");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(500, 15, 200, 30);
        header.add(titleLabel);
        
        return header;
    }
    
    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        
        // Product main section
        JPanel mainSection = new JPanel(new BorderLayout(30, 0));
        mainSection.setBackground(Color.WHITE);
        mainSection.setMaximumSize(new Dimension(1100, 500));
        
        // Left: Images
        JPanel imagesPanel = createImagesPanel();
        mainSection.add(imagesPanel, BorderLayout.WEST);
        
        // Right: Details
        JPanel detailsPanel = createDetailsPanel();
        mainSection.add(detailsPanel, BorderLayout.CENTER);
        
        contentPanel.add(mainSection);
        contentPanel.add(Box.createVerticalStrut(30));
        
        // Description section
        JPanel descriptionPanel = createDescriptionPanel();
        contentPanel.add(descriptionPanel);
        contentPanel.add(Box.createVerticalStrut(30));
        
        // Reviews section
        JPanel reviewsPanel = createReviewsPanel();
        contentPanel.add(reviewsPanel);
        contentPanel.add(Box.createVerticalStrut(30));
        
        // Similar products
        JPanel similarPanel = createSimilarProductsPanel();
        contentPanel.add(similarPanel);
        
        return contentPanel;
    }
    
    private JPanel createImagesPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 245, 245));
        panel.setPreferredSize(new Dimension(400, 500));
        panel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        
        // Main image
        JLabel mainImage = new JLabel("📦", SwingConstants.CENTER);
        mainImage.setFont(new Font("Arial", Font.PLAIN, 120));
        mainImage.setPreferredSize(new Dimension(400, 400));
        mainImage.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(mainImage);
        
        // Thumbnail images
        JPanel thumbnails = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        thumbnails.setBackground(new Color(245, 245, 245));
        
        for (int i = 0; i < 3; i++) {
            JLabel thumb = new JLabel("📷", SwingConstants.CENTER);
            thumb.setFont(new Font("Arial", Font.PLAIN, 24));
            thumb.setPreferredSize(new Dimension(80, 80));
            thumb.setBackground(Color.WHITE);
            thumb.setOpaque(true);
            thumb.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
            thumb.setCursor(new Cursor(Cursor.HAND_CURSOR));
            thumbnails.add(thumb);
        }
        
        panel.add(thumbnails);
        
        return panel;
    }
    
    private JPanel createDetailsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        // Product name
        JLabel nameLabel = new JLabel(product.getProductName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 28));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(nameLabel);
        panel.add(Box.createVerticalStrut(20));
        
        // Price section
        JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pricePanel.setBackground(Color.WHITE);
        pricePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        if (product.getDiscountPercentage() > 0) {
            JLabel originalPrice = new JLabel("<html><strike>₹" + String.format("%.0f", product.getPrice()) + "</strike></html>");
            originalPrice.setFont(new Font("Arial", Font.PLAIN, 18));
            originalPrice.setForeground(Color.GRAY);
            pricePanel.add(originalPrice);
            
            JLabel discountPrice = new JLabel("₹" + String.format("%.0f", product.getDiscountedPrice()));
            discountPrice.setFont(new Font("Arial", Font.BOLD, 32));
            discountPrice.setForeground(new Color(0, 128, 0));
            pricePanel.add(discountPrice);
            
            JLabel discountLabel = new JLabel(String.format("(%.0f%% OFF)", product.getDiscountPercentage()));
            discountLabel.setFont(new Font("Arial", Font.BOLD, 16));
            discountLabel.setForeground(Color.RED);
            pricePanel.add(discountLabel);
        } else {
            JLabel priceLabel = new JLabel("₹" + String.format("%.0f", product.getPrice()));
            priceLabel.setFont(new Font("Arial", Font.BOLD, 32));
            pricePanel.add(priceLabel);
        }
        
        panel.add(pricePanel);
        panel.add(Box.createVerticalStrut(15));
        
        // Stock status
        JLabel stockLabel = new JLabel(product.getStockQuantity() > 0 ? 
            "✓ In Stock (" + product.getStockQuantity() + " available)" : "✗ Out of Stock");
        stockLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        stockLabel.setForeground(product.getStockQuantity() > 0 ? new Color(0, 128, 0) : Color.RED);
        stockLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(stockLabel);
        panel.add(Box.createVerticalStrut(15));
        
        // Warranty
        if (product.getWarranty() != null && !product.getWarranty().equals("No Warranty")) {
            JLabel warrantyLabel = new JLabel("🛡️ " + product.getWarranty());
            warrantyLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            warrantyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(warrantyLabel);
            panel.add(Box.createVerticalStrut(15));
        }
        
        // Delivery info
        LocalDate deliveryDate = LocalDate.now().plusDays(7);
        JLabel deliveryLabel = new JLabel("🚚 Delivery by " + 
            deliveryDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
        deliveryLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        deliveryLabel.setForeground(new Color(0, 128, 0));
        deliveryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(deliveryLabel);
        panel.add(Box.createVerticalStrut(25));
        
        // Quantity selector
        JPanel quantityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        quantityPanel.setBackground(Color.WHITE);
        quantityPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel qtyLabel = new JLabel("Quantity:");
        qtyLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        quantityPanel.add(qtyLabel);
        
        SpinnerModel spinnerModel = new SpinnerNumberModel(1, 1, 
            Math.min(product.getStockQuantity(), 10), 1);
        quantitySpinner = new JSpinner(spinnerModel);
        quantitySpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        ((JSpinner.DefaultEditor) quantitySpinner.getEditor()).getTextField().setEditable(false);
        quantityPanel.add(quantitySpinner);
        
        panel.add(quantityPanel);
        panel.add(Box.createVerticalStrut(15));
        
        // Gift wrap option
        giftWrapCheckBox = new JCheckBox("🎁 Add gift wrap (+₹50)");
        giftWrapCheckBox.setFont(new Font("Arial", Font.PLAIN, 14));
        giftWrapCheckBox.setBackground(Color.WHITE);
        giftWrapCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(giftWrapCheckBox);
        panel.add(Box.createVerticalStrut(25));
        
        // Action buttons
        if (product.getStockQuantity() > 0) {
            JButton addToCartBtn = new JButton("🛒 Add to Cart");
            addToCartBtn.setFont(new Font("Arial", Font.BOLD, 16));
            addToCartBtn.setPreferredSize(new Dimension(250, 45));
            addToCartBtn.setMaximumSize(new Dimension(250, 45));
            addToCartBtn.setBackground(new Color(255, 153, 0));
            addToCartBtn.setForeground(Color.WHITE);
            addToCartBtn.setFocusPainted(false);
            addToCartBtn.setBorderPainted(false);
            addToCartBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            addToCartBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
            addToCartBtn.addActionListener(e -> addToCart());
            panel.add(addToCartBtn);
            panel.add(Box.createVerticalStrut(15));
            
            JButton buyNowBtn = new JButton("⚡ Buy Now");
            buyNowBtn.setFont(new Font("Arial", Font.BOLD, 16));
            buyNowBtn.setPreferredSize(new Dimension(250, 45));
            buyNowBtn.setMaximumSize(new Dimension(250, 45));
            buyNowBtn.setBackground(new Color(74, 144, 226));
            buyNowBtn.setForeground(Color.WHITE);
            buyNowBtn.setFocusPainted(false);
            buyNowBtn.setBorderPainted(false);
            buyNowBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            buyNowBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
            buyNowBtn.addActionListener(e -> buyNow());
            panel.add(buyNowBtn);
        } else {
            JButton notifyBtn = new JButton("🔔 Notify when in stock");
            notifyBtn.setFont(new Font("Arial", Font.BOLD, 16));
            notifyBtn.setPreferredSize(new Dimension(250, 45));
            notifyBtn.setMaximumSize(new Dimension(250, 45));
            notifyBtn.setBackground(new Color(220, 220, 220));
            notifyBtn.setForeground(Color.BLACK);
            notifyBtn.setFocusPainted(false);
            notifyBtn.setBorderPainted(false);
            notifyBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            notifyBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
            notifyBtn.addActionListener(e -> notifyWhenInStock());
            panel.add(notifyBtn);
        }
        
        panel.add(Box.createVerticalStrut(15));
        
        // Wishlist button
        boolean isInWishlist = wishlistDAO.isInWishlist(currentUser.getUserId(), product.getProductId());
        JButton wishlistBtn = new JButton(isInWishlist ? "❤️ In Wishlist" : "🤍 Add to Wishlist");
        wishlistBtn.setFont(new Font("Arial", Font.PLAIN, 14));
        wishlistBtn.setPreferredSize(new Dimension(250, 40));
        wishlistBtn.setMaximumSize(new Dimension(250, 40));
        wishlistBtn.setBackground(Color.WHITE);
        wishlistBtn.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        wishlistBtn.setFocusPainted(false);
        wishlistBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        wishlistBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        wishlistBtn.addActionListener(e -> toggleWishlist(wishlistBtn));
        panel.add(wishlistBtn);
        
        return panel;
    }
    
    private JPanel createDescriptionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        panel.setMaximumSize(new Dimension(1100, Integer.MAX_VALUE));
        
        JLabel titleLabel = new JLabel("📝 Product Description");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(15));
        
        JTextArea descArea = new JTextArea(product.getDescription());
        descArea.setFont(new Font("Arial", Font.PLAIN, 14));
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setEditable(false);
        descArea.setBackground(Color.WHITE);
        descArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(descArea);
        
        return panel;
    }
    
    private JPanel createReviewsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        panel.setMaximumSize(new Dimension(1100, 300));
        
        JLabel titleLabel = new JLabel("⭐ Customer Reviews");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(15));
        
        // Sample reviews
        String[] reviews = {
            "Great product! Highly recommended. - John D. (5★)",
            "Good quality for the price. - Sarah M. (4★)",
            "Fast delivery and excellent packaging. - Mike R. (5★)"
        };
        
        for (String review : reviews) {
            JLabel reviewLabel = new JLabel("• " + review);
            reviewLabel.setFont(new Font("Arial", Font.PLAIN, 13));
            reviewLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(reviewLabel);
            panel.add(Box.createVerticalStrut(10));
        }
        
        return panel;
    }
    
    private JPanel createSimilarProductsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(1100, 350));
        
        JLabel titleLabel = new JLabel("🔍 Similar Products");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(15));
        
        List<Product> similarProducts = productDAO.getSimilarProducts(
            product.getProductId(), product.getCategoryId());
        
        JPanel productsGrid = new JPanel(new GridLayout(1, Math.min(similarProducts.size(), 4), 15, 0));
        productsGrid.setBackground(Color.WHITE);
        productsGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        for (int i = 0; i < Math.min(similarProducts.size(), 4); i++) {
            Product p = similarProducts.get(i);
            JPanel card = createSmallProductCard(p);
            productsGrid.add(card);
        }
        
        panel.add(productsGrid);
        
        return panel;
    }
    
    private JPanel createSmallProductCard(Product p) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(250, 250, 250));
        card.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JLabel imageLabel = new JLabel("📦", SwingConstants.CENTER);
        imageLabel.setFont(new Font("Arial", Font.PLAIN, 40));
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(imageLabel);
        
        JLabel nameLabel = new JLabel("<html><center>" + p.getProductName() + "</center></html>");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));
        card.add(nameLabel);
        
        JLabel priceLabel = new JLabel("₹" + String.format("%.0f", p.getDiscountedPrice()));
        priceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(priceLabel);
        
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                ProductDetailsPage newPage = new ProductDetailsPage(currentUser, p, homePage);
                newPage.setVisible(true);
                dispose();
            }
        });
        
        return card;
    }
    
    private void addToCart() {
        int quantity = (Integer) quantitySpinner.getValue();
        
        if (cartDAO.addToCart(currentUser.getUserId(), product.getProductId(), quantity)) {
            JOptionPane.showMessageDialog(this, 
                "Product added to cart successfully!", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
            homePage.updateCartCount();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Failed to add product to cart!", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void buyNow() {
        int quantity = (Integer) quantitySpinner.getValue();
        
        // Add to cart first
        cartDAO.addToCart(currentUser.getUserId(), product.getProductId(), quantity);
        
        // Open checkout page
        CheckoutPage checkoutPage = new CheckoutPage(currentUser, homePage);
        checkoutPage.setVisible(true);
        this.dispose();
    }
    
    private void toggleWishlist(JButton button) {
        boolean isInWishlist = wishlistDAO.isInWishlist(currentUser.getUserId(), product.getProductId());
        
        if (isInWishlist) {
            if (wishlistDAO.removeFromWishlist(currentUser.getUserId(), product.getProductId())) {
                button.setText("🤍 Add to Wishlist");
                JOptionPane.showMessageDialog(this, "Removed from wishlist");
            }
        } else {
            if (wishlistDAO.addToWishlist(currentUser.getUserId(), product.getProductId())) {
                button.setText("❤️ In Wishlist");
                JOptionPane.showMessageDialog(this, "Added to wishlist");
            }
        }
        homePage.updateWishlistCount();
    }
    
    private void notifyWhenInStock() {
        JOptionPane.showMessageDialog(this, 
            "You will be notified when this product is back in stock!", 
            "Stock Reminder", JOptionPane.INFORMATION_MESSAGE);
    }
}