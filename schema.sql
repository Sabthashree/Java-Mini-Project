-- Smart E-com Database Schema
-- Save this as resources/sql/schema.sql

CREATE DATABASE IF NOT EXISTS smart_ecom;
USE smart_ecom;

-- Users Table
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255),
    phone_no VARCHAR(15),
    address TEXT,
    google_id VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_email (email)
);

-- Categories Table
CREATE TABLE categories (
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    category_name VARCHAR(50) NOT NULL,
    category_image VARCHAR(255)
);

-- Products Table
CREATE TABLE products (
    product_id INT PRIMARY KEY AUTO_INCREMENT,
    product_name VARCHAR(200) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    discount_percentage DECIMAL(5, 2) DEFAULT 0,
    category_id INT,
    stock_quantity INT DEFAULT 0,
    warranty VARCHAR(100),
    is_top_selling BOOLEAN DEFAULT FALSE,
    product_images TEXT, -- Comma-separated image paths
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(category_id),
    INDEX idx_category (category_id),
    INDEX idx_top_selling (is_top_selling)
);

-- Cart Table
CREATE TABLE cart (
    cart_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT DEFAULT 1,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_product (user_id, product_id)
);

-- Wishlist Table
CREATE TABLE wishlist (
    wishlist_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_product_wishlist (user_id, product_id)
);

-- Orders Table
CREATE TABLE orders (
    order_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    delivery_charge DECIMAL(10, 2) DEFAULT 0,
    payment_method ENUM('COD', 'NetBanking', 'UPI', 'EMI') NOT NULL,
    delivery_address TEXT NOT NULL,
    order_status ENUM('Pending', 'Confirmed', 'Shipped', 'Delivered', 'Cancelled') DEFAULT 'Pending',
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expected_delivery_date DATE,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    INDEX idx_user_orders (user_id),
    INDEX idx_order_date (order_date)
);

-- Order Items Table
CREATE TABLE order_items (
    order_item_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    price_at_purchase DECIMAL(10, 2) NOT NULL,
    gift_wrap BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);

-- Reviews Table
CREATE TABLE reviews (
    review_id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT NOT NULL,
    user_id INT NOT NULL,
    rating INT CHECK (rating >= 1 AND rating <= 5),
    review_text TEXT,
    review_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    INDEX idx_product_reviews (product_id)
);

-- Browsing History Table
CREATE TABLE browsing_history (
    history_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    viewed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE,
    INDEX idx_user_history (user_id, viewed_at)
);

-- Stock Reminders Table
CREATE TABLE stock_reminders (
    reminder_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    notified BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
);

-- Insert Sample Categories
INSERT INTO categories (category_name, category_image) VALUES
('Electronics', 'electronics.png'),
('Fashion', 'fashion.png'),
('Home & Kitchen', 'home_kitchen.png'),
('Books', 'books.png'),
('Sports', 'sports.png'),
('Beauty', 'beauty.png'),
('Toys', 'toys.png'),
('Groceries', 'groceries.png');

-- Insert Sample Products
INSERT INTO products (product_name, description, price, discount_percentage, category_id, stock_quantity, warranty, is_top_selling, product_images) VALUES
('Smartphone XYZ', 'Latest 5G smartphone with 128GB storage', 25000.00, 15, 1, 50, '1 Year Warranty', TRUE, 'phone1.jpg,phone2.jpg,phone3.jpg'),
('Wireless Headphones', 'Noise-cancelling bluetooth headphones', 3500.00, 20, 1, 100, '6 Months Warranty', TRUE, 'headphone1.jpg,headphone2.jpg'),
('Running Shoes', 'Comfortable running shoes for all terrains', 2500.00, 10, 5, 80, 'No Warranty', FALSE, 'shoes1.jpg,shoes2.jpg'),
('Cotton T-Shirt', 'Premium quality cotton t-shirt', 499.00, 25, 2, 200, 'No Warranty', TRUE, 'tshirt1.jpg,tshirt2.jpg'),
('Laptop Pro', 'High-performance laptop with 16GB RAM', 65000.00, 10, 1, 30, '2 Year Warranty', TRUE, 'laptop1.jpg,laptop2.jpg,laptop3.jpg'),
('Cooking Pan Set', 'Non-stick 5-piece cooking pan set', 1500.00, 30, 3, 60, '1 Year Warranty', FALSE, 'pan1.jpg,pan2.jpg'),
('Novel Book Collection', 'Bestseller fiction book collection', 799.00, 15, 4, 150, 'No Warranty', FALSE, 'book1.jpg'),
('Smart Watch', 'Fitness tracking smart watch', 8000.00, 20, 1, 45, '1 Year Warranty', TRUE, 'watch1.jpg,watch2.jpg');

-- Insert Sample Admin User (password: admin123 - should be hashed in production)
INSERT INTO users (name, email, password, phone_no, address) VALUES
('Admin User', 'admin@smartecom.com', 'admin123', '9876543210', '123 Admin Street, Chennai');

-- Create indexes for better performance
CREATE INDEX idx_product_name ON products(product_name);
CREATE INDEX idx_product_price ON products(price);
CREATE INDEX idx_order_status ON orders(order_status);