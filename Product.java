package models;

import java.sql.Timestamp;

public class Product {
    private int productId;
    private String productName;
    private String description;
    private double price;
    private double discountPercentage;
    private int categoryId;
    private int stockQuantity;
    private String warranty;
    private boolean isTopSelling;
    private String productImages; // Comma-separated
    private Timestamp createdAt;
    
    // Constructors
    public Product() {}
    
    public Product(int productId, String productName, String description, double price, 
                   double discountPercentage, int stockQuantity, String warranty, String productImages) {
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.discountPercentage = discountPercentage;
        this.stockQuantity = stockQuantity;
        this.warranty = warranty;
        this.productImages = productImages;
    }
    
    // Calculate discounted price
    public double getDiscountedPrice() {
        return price - (price * discountPercentage / 100);
    }
    
    // Get first image
    public String getFirstImage() {
        if (productImages != null && !productImages.isEmpty()) {
            return productImages.split(",")[0];
        }
        return "placeholder.png";
    }
    
    // Getters and Setters
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public double getDiscountPercentage() { return discountPercentage; }
    public void setDiscountPercentage(double discountPercentage) { this.discountPercentage = discountPercentage; }
    
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    
    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }
    
    public String getWarranty() { return warranty; }
    public void setWarranty(String warranty) { this.warranty = warranty; }
    
    public boolean isTopSelling() { return isTopSelling; }
    public void setTopSelling(boolean topSelling) { isTopSelling = topSelling; }
    
    public String getProductImages() { return productImages; }
    public void setProductImages(String productImages) { this.productImages = productImages; }
    
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}