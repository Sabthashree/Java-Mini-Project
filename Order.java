package models;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

public class Order {
    private int orderId;
    private int userId;
    private double totalAmount;
    private double deliveryCharge;
    private String paymentMethod;
    private String deliveryAddress;
    private String orderStatus;
    private Timestamp orderDate;
    private Date expectedDeliveryDate;
    private List<OrderItem> orderItems;
    
    // Constructors
    public Order() {}
    
    public Order(int userId, double totalAmount, double deliveryCharge, 
                 String paymentMethod, String deliveryAddress) {
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.deliveryCharge = deliveryCharge;
        this.paymentMethod = paymentMethod;
        this.deliveryAddress = deliveryAddress;
    }
    
    // Getters and Setters
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    
    public double getDeliveryCharge() { return deliveryCharge; }
    public void setDeliveryCharge(double deliveryCharge) { this.deliveryCharge = deliveryCharge; }
    
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    
    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }
    
    public String getOrderStatus() { return orderStatus; }
    public void setOrderStatus(String orderStatus) { this.orderStatus = orderStatus; }
    
    public Timestamp getOrderDate() { return orderDate; }
    public void setOrderDate(Timestamp orderDate) { this.orderDate = orderDate; }
    
    public Date getExpectedDeliveryDate() { return expectedDeliveryDate; }
    public void setExpectedDeliveryDate(Date expectedDeliveryDate) { this.expectedDeliveryDate = expectedDeliveryDate; }
    
    public List<OrderItem> getOrderItems() { return orderItems; }
    public void setOrderItems(List<OrderItem> orderItems) { this.orderItems = orderItems; }
}