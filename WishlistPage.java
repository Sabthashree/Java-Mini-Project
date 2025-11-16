package ui;

import dao.*;
import models.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import utils.ImageSearchUtil; // NEW IMPORT

public class WishlistPage extends JFrame {
    private User currentUser;
    private HomePage homePage;
    private WishlistDAO wishlistDAO;
    private CartDAO cartDAO;
    private JPanel itemsPanel;
    
    public WishlistPage(User user, HomePage homePage) {
        this.currentUser = user;
        this.homePage = homePage;
        this.wishlistDAO = new WishlistDAO();
        this.cartDAO = new CartDAO();
        
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Smart E-com - Wishlist");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        // Header
        JPanel headerPanel = createHeader();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Content
        itemsPanel = new JPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
        itemsPanel.setBackground(new Color(245, 245, 245));
        itemsPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        loadWishlistItems();
        
        JScrollPane scrollPane = new JScrollPane(itemsPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(mainPanel);
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                homePage.setVisible(true);
                homePage.updateWishlistCount();
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
            homePage.updateWishlistCount();
            dispose();
        });
        header.add(backButton);
        
        JLabel titleLabel = new JLabel("My Wishlist");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(430, 15, 200, 30);
        header.add(titleLabel);
        
        return header;
    }
    
    private void loadWishlistItems() {
        itemsPanel.removeAll();
        
        List<Product> wishlistItems = wishlistDAO.getWishlistItems(currentUser.getUserId());
        
        if (wishlistItems.isEmpty()) {
            JLabel emptyLabel = new JLabel("❤️ Your wishlist is empty");
            emptyLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            itemsPanel.add(Box.createVerticalStrut(100));
            itemsPanel.add(emptyLabel);
        } else {
            JLabel countLabel = new JLabel(wishlistItems.size() + " items in wishlist");
            countLabel.setFont(new Font("Arial", Font.BOLD, 16));
            countLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            itemsPanel.add(countLabel);
            itemsPanel.add(Box.createVerticalStrut(20));
            
            // Grid layout
            int cols = 3;
            int rows = (int) Math.ceil(wishlistItems.size() / (double) cols);
            
            for (int i = 0; i < rows; i++) {
                JPanel rowPanel = new JPanel(new GridLayout(1, cols, 20, 0));
                rowPanel.setBackground(new Color(245, 245, 245));
                rowPanel.setMaximumSize(new Dimension(900, 350));
                rowPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                
                for (int j = 0; j < cols; j++) {
                    int index = i * cols + j;
                    if (index < wishlistItems.size()) {
                        Product product = wishlistItems.get(index);
                        JPanel productCard = createWishlistCard(product);
                        rowPanel.add(productCard);
                    } else {
                        rowPanel.add(new JPanel());
                    }
                }
                
                itemsPanel.add(rowPanel);
                itemsPanel.add(Box.createVerticalStrut(20));
            }
        }
        
        itemsPanel.revalidate();
        itemsPanel.repaint();
    }
    
    private JPanel createWishlistCard(Product product) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        
        // Image -> REPLACED WITH IMAGE LOADING
        JLabel imageLabel = new JLabel();
        // FIX: Removed "assets/images/" prefix
        ImageIcon productIcon = ImageSearchUtil.loadAndScaleImage(
            product.getFirstImage(), 280, 160);
        
        if (productIcon.getIconWidth() > 0) {
            imageLabel.setIcon(productIcon);
        } else {
            imageLabel.setText("📦");
            imageLabel.setFont(new Font("Arial", Font.PLAIN, 70));
        }

        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(280, 160));
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(imageLabel);
        
        // Name
        JLabel nameLabel = new JLabel("<html><center>" + product.getProductName() + "</center></html>");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        card.add(nameLabel);
        
        // Price
        JLabel priceLabel = new JLabel("₹" + String.format("%.0f", product.getDiscountedPrice()));
        priceLabel.setFont(new Font("Arial", Font.BOLD, 18));
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(priceLabel);
        card.add(Box.createVerticalStrut(10));
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        buttonsPanel.setBackground(Color.WHITE);
        
        JButton addToCartBtn = new JButton("Add to Cart");
        addToCartBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        addToCartBtn.setBackground(new Color(255, 153, 0));
        addToCartBtn.setForeground(Color.WHITE);
        addToCartBtn.setFocusPainted(false);
        addToCartBtn.setBorderPainted(false);
        addToCartBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addToCartBtn.addActionListener(e -> {
            if (cartDAO.addToCart(currentUser.getUserId(), product.getProductId(), 1)) {
                JOptionPane.showMessageDialog(this, "Added to cart!");
                homePage.updateCartCount();
            }
        });
        buttonsPanel.add(addToCartBtn);
        
        JButton removeBtn = new JButton("❌");
        removeBtn.setFont(new Font("Arial", Font.PLAIN, 14));
        removeBtn.setBackground(Color.WHITE);
        removeBtn.setForeground(Color.RED);
        removeBtn.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
        removeBtn.setFocusPainted(false);
        removeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        removeBtn.setToolTipText("Remove from wishlist");
        removeBtn.addActionListener(e -> {
            if (wishlistDAO.removeFromWishlist(currentUser.getUserId(), product.getProductId())) {
                loadWishlistItems();
                homePage.updateWishlistCount();
            }
        });
        buttonsPanel.add(removeBtn);
        
        card.add(buttonsPanel);
        
        return card;
    }
}