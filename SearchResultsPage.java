package ui;

import dao.ProductDAO;
import models.Product;
import models.User;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SearchResultsPage extends JFrame {
    private User currentUser;
    private String searchKeyword;
    private ProductDAO productDAO;
    
    public SearchResultsPage(User user, String keyword) {
        this.currentUser = user;
        this.searchKeyword = keyword;
        this.productDAO = new ProductDAO();
        
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Smart E-com - Search Results");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        // Header
        JPanel headerPanel = createHeader();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Results
        List<Product> results = productDAO.searchProducts(searchKeyword);
        JPanel resultsPanel = createResultsPanel(results);
        
        JScrollPane scrollPane = new JScrollPane(resultsPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createHeader() {
        JPanel header = new JPanel(null);
        header.setPreferredSize(new Dimension(1200, 80));
        header.setBackground(new Color(74, 144, 226));
        
        JButton backButton = new JButton("← Back to Home");
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.setBounds(20, 25, 150, 30);
        backButton.setForeground(Color.WHITE);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            HomePage homePage = new HomePage(currentUser);
            homePage.setVisible(true);
            dispose();
        });
        header.add(backButton);
        
        JLabel titleLabel = new JLabel("Search Results for: \"" + searchKeyword + "\"");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(400, 25, 400, 30);
        header.add(titleLabel);
        
        return header;
    }
    
    private JPanel createResultsPanel(List<Product> products) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        
        if (products.isEmpty()) {
            JLabel noResultsLabel = new JLabel("No products found for \"" + searchKeyword + "\"");
            noResultsLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            noResultsLabel.setForeground(Color.GRAY);
            noResultsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(Box.createVerticalStrut(100));
            panel.add(noResultsLabel);
            return panel;
        }
        
        JLabel countLabel = new JLabel(products.size() + " products found");
        countLabel.setFont(new Font("Arial", Font.BOLD, 16));
        countLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(countLabel);
        panel.add(Box.createVerticalStrut(20));
        
        // Products grid
        int cols = 4;
        int rows = (int) Math.ceil(products.size() / (double) cols);
        
        for (int i = 0; i < rows; i++) {
            JPanel rowPanel = new JPanel(new GridLayout(1, cols, 20, 0));
            rowPanel.setBackground(new Color(245, 245, 245));
            rowPanel.setMaximumSize(new Dimension(1100, 320));
            rowPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            for (int j = 0; j < cols; j++) {
                int index = i * cols + j;
                if (index < products.size()) {
                    Product product = products.get(index);
                    JPanel productCard = createProductCard(product);
                    rowPanel.add(productCard);
                } else {
                    rowPanel.add(new JPanel());
                }
            }
            
            panel.add(rowPanel);
            panel.add(Box.createVerticalStrut(20));
        }
        
        return panel;
    }
    
    private JPanel createProductCard(Product product) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setPreferredSize(new Dimension(250, 300));
        
        // Product image placeholder
        JLabel imageLabel = new JLabel("📦", SwingConstants.CENTER);
        imageLabel.setFont(new Font("Arial", Font.PLAIN, 80));
        imageLabel.setPreferredSize(new Dimension(250, 180));
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
        pricePanel.setBackground(Color.WHITE);
        
        if (product.getDiscountPercentage() > 0) {
            JLabel originalPrice = new JLabel("<html><strike>₹" + String.format("%.0f", product.getPrice()) + "</strike></html>");
            originalPrice.setFont(new Font("Arial", Font.PLAIN, 12));
            originalPrice.setForeground(Color.GRAY);
            pricePanel.add(originalPrice);
            
            JLabel discountPrice = new JLabel("₹" + String.format("%.0f", product.getDiscountedPrice()));
            discountPrice.setFont(new Font("Arial", Font.BOLD, 18));
            discountPrice.setForeground(new Color(0, 128, 0));
            pricePanel.add(discountPrice);
        } else {
            JLabel priceLabel = new JLabel("₹" + String.format("%.0f", product.getPrice()));
            priceLabel.setFont(new Font("Arial", Font.BOLD, 18));
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
                HomePage homePage = new HomePage(currentUser);
                ProductDetailsPage detailsPage = new ProductDetailsPage(currentUser, product, homePage);
                detailsPage.setVisible(true);
                dispose();
            }
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBackground(new Color(245, 245, 245));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBackground(Color.WHITE);
            }
        });
        
        return card;
    }
}