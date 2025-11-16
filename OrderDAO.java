package dao;

import config.DatabaseConfig;
import models.Order;
import models.OrderItem;
import models.CartItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class OrderDAO {
    private CartDAO cartDAO = new CartDAO();
    private ProductDAO productDAO = new ProductDAO();
    
    // Create order from cart
    public int createOrder(Order order, List<CartItem> cartItems) {
        String orderSql = "INSERT INTO orders (user_id, total_amount, delivery_charge, payment_method, " +
                         "delivery_address, expected_delivery_date) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {
            
            // Calculate expected delivery date (7 days from now)
            LocalDate deliveryDate = LocalDate.now().plusDays(7);
            
            pstmt.setInt(1, order.getUserId());
            pstmt.setDouble(2, order.getTotalAmount());
            pstmt.setDouble(3, order.getDeliveryCharge());
            pstmt.setString(4, order.getPaymentMethod());
            pstmt.setString(5, order.getDeliveryAddress());
            pstmt.setDate(6, Date.valueOf(deliveryDate));
            
            int rows = pstmt.executeUpdate();
            
            if (rows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    int orderId = rs.getInt(1);
                    
                    // Add order items
                    for (CartItem cartItem : cartItems) {
                        addOrderItem(orderId, cartItem);
                        
                        // Update product stock
                        productDAO.updateStock(cartItem.getProductId(), cartItem.getQuantity());
                    }
                    
                    // Clear cart after successful order
                    cartDAO.clearCart(order.getUserId());
                    
                    return orderId;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    
    // Add order item
    private boolean addOrderItem(int orderId, CartItem cartItem) {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity, price_at_purchase, gift_wrap) " +
                    "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, orderId);
            pstmt.setInt(2, cartItem.getProductId());
            pstmt.setInt(3, cartItem.getQuantity());
            pstmt.setDouble(4, cartItem.getProduct().getDiscountedPrice());
            pstmt.setBoolean(5, false); // Gift wrap default false
            
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Get orders by user
    public List<Order> getOrdersByUser(int userId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE user_id = ? ORDER BY order_date DESC";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Order order = extractOrderFromResultSet(rs);
                
                // Get order items
                order.setOrderItems(getOrderItems(order.getOrderId()));
                
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
    
    // Get order by ID
    public Order getOrderById(int orderId) {
        String sql = "SELECT * FROM orders WHERE order_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, orderId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Order order = extractOrderFromResultSet(rs);
                order.setOrderItems(getOrderItems(orderId));
                return order;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Get order items
    private List<OrderItem> getOrderItems(int orderId) {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT * FROM order_items WHERE order_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, orderId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                OrderItem item = new OrderItem();
                item.setOrderItemId(rs.getInt("order_item_id"));
                item.setOrderItemId(rs.getInt("order_id"));
                item.setProductId(rs.getInt("product_id"));
                item.setQuantity(rs.getInt("quantity"));
                item.setPriceAtPurchase(rs.getDouble("price_at_purchase"));
                item.setGiftWrap(rs.getBoolean("gift_wrap"));
                
                // Get product details
                item.setProduct(productDAO.getProductById(rs.getInt("product_id")));
                
                items.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }
    
    // Extract Order from ResultSet
    private Order extractOrderFromResultSet(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setOrderId(rs.getInt("order_id"));
        order.setUserId(rs.getInt("user_id"));
        order.setTotalAmount(rs.getDouble("total_amount"));
        order.setDeliveryCharge(rs.getDouble("delivery_charge"));
        order.setPaymentMethod(rs.getString("payment_method"));
        order.setDeliveryAddress(rs.getString("delivery_address"));
        order.setOrderStatus(rs.getString("order_status"));
        order.setOrderDate(rs.getTimestamp("order_date"));
        order.setExpectedDeliveryDate(rs.getDate("expected_delivery_date"));
        return order;
    }
}