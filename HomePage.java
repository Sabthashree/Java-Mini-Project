// ==============================================
// src/ui/HomePage.java
// ==============================================
package ui;

import dao.*;
import models.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class HomePage extends JFrame {
    private User currentUser;
    private ProductDAO productDAO;
    private CartDAO cartDAO;
    private WishlistDAO wishlistDAO;
    
    private JPanel contentPanel;
    private JTextField searchField;
    private JLabel cartCountLabel, wishlistCountLabel;
    
    // Dashboard buttons
    private JButton homeBtn, cartBtn, wishlistBtn, profileBtn;
    
    public HomePage(User user) {
        this.currentUser = user;
        this.productDAO = new ProductDAO();
        this.cartDAO = new CartDAO();
        this.wishlistDAO = new WishlistDAO();
        
        initComponents();
        loadHomeContent();
    }
    
    private void initComponents() {
        setTitle("Smart E-com - Home");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main container
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        // Header
        JPanel headerPanel = createHeader();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Content area with scroll
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(245, 245, 245));
        
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Bottom navigation dashboard
        JPanel dashboardPanel = createDashboard();
        mainPanel.add(dashboardPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createHeader() {
        JPanel headerPanel = new JPanel(null);
        headerPanel.setPreferredSize(new Dimension(1200, 120));
        headerPanel.setBackground(new Color(74, 144, 226));
        
        // Logo
        JLabel logoLabel = new JLabel("Smart E-com");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setBounds(30, 20, 200, 30);
        headerPanel.add(logoLabel);
        
        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getName());
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setBounds(30, 50, 300, 20);
        headerPanel.add(welcomeLabel);
        
        // Search bar
        JPanel searchPanel = new JPanel(null);
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBounds(250, 25, 700, 50);
        searchPanel.setBorder(BorderFactory.createEmptyBorder());
        
        searchField = new JTextField("Search products...");
        searchField.setFont(new Font("Arial", Font.PLAIN, 16));
        searchField.setBounds(10, 10, 550, 30);
        searchField.setForeground(Color.GRAY);
        searchField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        // Focus listener for placeholder text
        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (searchField.getText().equals("Search products...")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Search products...");
                    searchField.setForeground(Color.GRAY);
                }
            }
        });
        
        searchField.addActionListener(e -> performSearch());
        searchPanel.add(searchField);
        
        // Search button
        JButton searchButton = new JButton("🔍");
        searchButton.setFont(new Font("Arial", Font.PLAIN, 18));
        searchButton.setBounds(570, 10, 50, 30);
        searchButton.setBackground(new Color(74, 144, 226));
        searchButton.setForeground(Color.WHITE);
        searchButton.setBorderPainted(false);
        searchButton.setFocusPainted(false);
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchButton.addActionListener(e -> performSearch());
        searchPanel.add(searchButton);
        
        // Camera icon (Image search)
        JButton cameraButton = new JButton("📷");
        cameraButton.setFont(new Font("Arial", Font.PLAIN, 16));
        cameraButton.setBounds(630, 10, 30, 30);
        cameraButton.setContentAreaFilled(false);
        cameraButton.setBorderPainted(false);
        cameraButton.setFocusPainted(false);
        cameraButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cameraButton.setToolTipText("Image Search");
        cameraButton.addActionListener(e -> imageSearch());
        searchPanel.add(cameraButton);
        
        // Voice icon
        JButton voiceButton = new JButton("🎤");
        voiceButton.setFont(new Font("Arial", Font.PLAIN, 16));
        voiceButton.setBounds(665, 10, 30, 30);
        voiceButton.setContentAreaFilled(false);
        voiceButton.setBorderPainted(false);
        voiceButton.setFocusPainted(false);
        voiceButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        voiceButton.setToolTipText("Voice Search");
        voiceButton.addActionListener(e -> voiceSearch());
        searchPanel.add(voiceButton);
        
        headerPanel.add(searchPanel);
        
        // Cart icon with count
        JButton cartIcon = new JButton("🛒");
        cartIcon.setFont(new Font("Arial", Font.PLAIN, 24));
        cartIcon.setBounds(1020, 30, 60, 40);
        cartIcon.setForeground(Color.WHITE);
        cartIcon.setContentAreaFilled(false);
        cartIcon.setBorderPainted(false);
        cartIcon.setFocusPainted(false);
        cartIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cartIcon.addActionListener(e -> showCartPage());
        headerPanel.add(cartIcon);
        
        cartCountLabel = new JLabel("0");
        cartCountLabel.setFont(new Font("Arial", Font.BOLD, 11));
        cartCountLabel.setForeground(Color.WHITE);
        cartCountLabel.setBackground(Color.RED);
        cartCountLabel.setOpaque(true);
        cartCountLabel.setBounds(1060, 25, 20, 20);
        cartCountLabel.setHorizontalAlignment(SwingConstants.CENTER);
        updateCartCount();
        headerPanel.add(cartCountLabel);
        
        // Wishlist icon with count
        JButton wishlistIcon = new JButton("❤️");
        wishlistIcon.setFont(new Font("Arial", Font.PLAIN, 24));
        wishlistIcon.setBounds(1100, 30, 60, 40);
        wishlistIcon.setForeground(Color.WHITE);
        wishlistIcon.setContentAreaFilled(false);
        wishlistIcon.setBorderPainted(false);
        wishlistIcon.setFocusPainted(false);
        wishlistIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        wishlistIcon.addActionListener(e -> showWishlistPage());
        headerPanel.add(wishlistIcon);
        
        wishlistCountLabel = new JLabel("0");
        wishlistCountLabel.setFont(new Font("Arial", Font.BOLD, 11));
        wishlistCountLabel.setForeground(Color.WHITE);
        wishlistCountLabel.setBackground(Color.RED);
        wishlistCountLabel.setOpaque(true);
        wishlistCountLabel.setBounds(1140, 25, 20, 20);
        wishlistCountLabel.setHorizontalAlignment(SwingConstants.CENTER);
        updateWishlistCount();
        headerPanel.add(wishlistCountLabel);
        
        return headerPanel;
    }
    
    private JPanel createDashboard() {
        JPanel dashboardPanel = new JPanel(new GridLayout(1, 4, 0, 0));
        dashboardPanel.setPreferredSize(new Dimension(1200, 70));
        dashboardPanel.setBackground(Color.WHITE);
        dashboardPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)));
        
        // Home button
        homeBtn = createDashboardButton("🏠", "Home");
        homeBtn.setBackground(new Color(230, 240, 255));
        homeBtn.addActionListener(e -> {
            resetDashboardButtons();
            homeBtn.setBackground(new Color(230, 240, 255));
            loadHomeContent();
        });
        dashboardPanel.add(homeBtn);
        
        // Cart button
        cartBtn = createDashboardButton("🛒", "Cart");
        cartBtn.addActionListener(e -> {
            resetDashboardButtons();
            cartBtn.setBackground(new Color(230, 240, 255));
            showCartPage();
        });
        dashboardPanel.add(cartBtn);
        
        // Wishlist button
        wishlistBtn = createDashboardButton("❤️", "Wishlist");
        wishlistBtn.addActionListener(e -> {
            resetDashboardButtons();
            wishlistBtn.setBackground(new Color(230, 240, 255));
            showWishlistPage();
        });
        dashboardPanel.add(wishlistBtn);
        
        // Profile button
        profileBtn = createDashboardButton("👤", "Profile");
        profileBtn.addActionListener(e -> {
            resetDashboardButtons();
            profileBtn.setBackground(new Color(230, 240, 255));
            showProfilePage();
        });
        dashboardPanel.add(profileBtn);
        
        return dashboardPanel;
    }
    
    private JButton createDashboardButton(String icon, String text) {
        JButton button = new JButton("<html><center>" + icon + "<br>" + text + "</center></html>");
        button.setFont(new Font("Arial", Font.PLAIN, 12));
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    private void resetDashboardButtons() {
        homeBtn.setBackground(Color.WHITE);
        cartBtn.setBackground(Color.WHITE);
        wishlistBtn.setBackground(Color.WHITE);
        profileBtn.setBackground(Color.WHITE);
    }
    
    private void loadHomeContent() {
        contentPanel.removeAll();
        
        // Top Selling Section
        JPanel topSellingSection = createProductSection("🔥 Top Selling Products", 
                                                        productDAO.getTopSellingProducts());
        contentPanel.add(topSellingSection);
        
        // Discount Section
        JPanel discountSection = createProductSection("💰 Great Discounts", 
                                                      productDAO.getDiscountProducts());
        contentPanel.add(discountSection);
        
        // Categories Section
        JPanel categoriesSection = createCategoriesSection();
        contentPanel.add(categoriesSection);
        
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private JPanel createProductSection(String title, List<Product> products) {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(Color.WHITE);
        section.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        section.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));
        
        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.add(titleLabel);
        
        section.add(Box.createVerticalStrut(15));
        
        // Products grid
        JPanel productsGrid = new JPanel(new GridLayout(1, Math.min(products.size(), 5), 15, 0));
        productsGrid.setBackground(Color.WHITE);
        productsGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        for (int i = 0; i < Math.min(products.size(), 5); i++) {
            Product product = products.get(i);
            JPanel productCard = createProductCard(product);
            productsGrid.add(productCard);
        }
        
        section.add(productsGrid);
        
        return section;
    }
    
    private JPanel createProductCard(Product product) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(250, 250, 250));
        card.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setPreferredSize(new Dimension(200, 280));
        
        // Product image placeholder
        JLabel imageLabel = new JLabel("📦", SwingConstants.CENTER);
        imageLabel.setFont(new Font("Arial", Font.PLAIN, 60));
        imageLabel.setPreferredSize(new Dimension(200, 150));
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(imageLabel);
        
        // Product name
        JLabel nameLabel = new JLabel("<html><center>" + product.getProductName() + "</center></html>");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        card.add(nameLabel);
        
        // Price
        JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        pricePanel.setBackground(new Color(250, 250, 250));
        
        if (product.getDiscountPercentage() > 0) {
            JLabel originalPrice = new JLabel("₹" + String.format("%.0f", product.getPrice()));
            originalPrice.setFont(new Font("Arial", Font.PLAIN, 12));
            originalPrice.setForeground(Color.GRAY);
            originalPrice.setText("<html><strike>₹" + String.format("%.0f", product.getPrice()) + "</strike></html>");
            pricePanel.add(originalPrice);
            
            JLabel discountPrice = new JLabel("₹" + String.format("%.0f", product.getDiscountedPrice()));
            discountPrice.setFont(new Font("Arial", Font.BOLD, 16));
            discountPrice.setForeground(new Color(0, 128, 0));
            pricePanel.add(discountPrice);
            
            JLabel discountLabel = new JLabel(String.format("%.0f%% OFF", product.getDiscountPercentage()));
            discountLabel.setFont(new Font("Arial", Font.PLAIN, 11));
            discountLabel.setForeground(Color.RED);
            pricePanel.add(discountLabel);
        } else {
            JLabel priceLabel = new JLabel("₹" + String.format("%.0f", product.getPrice()));
            priceLabel.setFont(new Font("Arial", Font.BOLD, 16));
            pricePanel.add(priceLabel);
        }
        
        card.add(pricePanel);
        
        // Stock status
        JLabel stockLabel = new JLabel(product.getStockQuantity() > 0 ? "In Stock" : "Out of Stock");
        stockLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        stockLabel.setForeground(product.getStockQuantity() > 0 ? new Color(0, 128, 0) : Color.RED);
        stockLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(stockLabel);
        
        // Click listener
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                openProductDetails(product);
            }
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBackground(new Color(240, 240, 240));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBackground(new Color(250, 250, 250));
            }
        });
        
        return card;
    }
    
    private JPanel createCategoriesSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(Color.WHITE);
        section.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("📂 Shop by Category");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.add(titleLabel);
        
        section.add(Box.createVerticalStrut(15));
        
        List<String> categories = productDAO.getAllCategories();
        JPanel categoriesGrid = new JPanel(new GridLayout(2, 4, 15, 15));
        categoriesGrid.setBackground(Color.WHITE);
        categoriesGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        String[] categoryIcons = {"💻", "👕", "🏠", "📚", "⚽", "💄", "🧸", "🛒"};
        
        for (int i = 0; i < Math.min(categories.size(), 8); i++) {
            String category = categories.get(i);
            JPanel categoryCard = createCategoryCard(categoryIcons[i], category);
            categoriesGrid.add(categoryCard);
        }
        
        section.add(categoriesGrid);
        
        return section;
    }
    
    private JPanel createCategoryCard(String icon, String categoryName) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(245, 245, 245));
        card.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setPreferredSize(new Dimension(130, 120));
        
        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 48));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(Box.createVerticalStrut(20));
        card.add(iconLabel);
        
        JLabel nameLabel = new JLabel(categoryName);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(Box.createVerticalStrut(10));
        card.add(nameLabel);
        
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                searchField.setText(categoryName);
                performSearch();
            }
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBackground(new Color(230, 240, 255));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBackground(new Color(245, 245, 245));
            }
        });
        
        return card;
    }
    
    private void performSearch() {
        String keyword = searchField.getText().trim();
        if (!keyword.isEmpty() && !keyword.equals("Search products...")) {
            SearchResultsPage searchPage = new SearchResultsPage(currentUser, keyword);
            searchPage.setVisible(true);
            this.dispose();
        }
    }
    
    private void imageSearch() {
        JOptionPane.showMessageDialog(this, 
            "Image search feature coming soon!\nYou can upload a product image to find similar items.",
            "Image Search", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void voiceSearch() {
        String voiceInput = JOptionPane.showInputDialog(this, 
            "Speak your search query:\n(Type to simulate voice input)");
        
        if (voiceInput != null && !voiceInput.trim().isEmpty()) {
            searchField.setText(voiceInput);
            performSearch();
        }
    }
    
    private void openProductDetails(Product product) {
        ProductDetailsPage detailsPage = new ProductDetailsPage(currentUser, product, this);
        detailsPage.setVisible(true);
        this.setVisible(false);
    }
    
    private void showCartPage() {
        CartPage cartPage = new CartPage(currentUser, this);
        cartPage.setVisible(true);
        this.setVisible(false);
    }
    
    private void showWishlistPage() {
        WishlistPage wishlistPage = new WishlistPage(currentUser, this);
        wishlistPage.setVisible(true);
        this.setVisible(false);
    }
    
    private void showProfilePage() {
        UserProfilePage profilePage = new UserProfilePage(currentUser, this);
        profilePage.setVisible(true);
        this.setVisible(false);
    }
    
    public void updateCartCount() {
        int count = cartDAO.getCartItemCount(currentUser.getUserId());
        cartCountLabel.setText(String.valueOf(count));
        cartCountLabel.setVisible(count > 0);
    }
    
    public void updateWishlistCount() {
        int count = wishlistDAO.getWishlistCount(currentUser.getUserId());
        wishlistCountLabel.setText(String.valueOf(count));
        wishlistCountLabel.setVisible(count > 0);
    }
}