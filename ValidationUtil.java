package utils;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ValidationUtil {
    
    // Email validation pattern
    private static final String EMAIL_PATTERN = 
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    
    // Phone number pattern (Indian format)
    private static final String PHONE_PATTERN = "^[6-9]\\d{9}$";
    
    // Password pattern (min 6 chars, at least one letter and one number)
    private static final String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&]{6,}$";
    
    /**
     * Validate email address
     * @param email Email address to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    
    /**
     * Validate phone number (Indian format: 10 digits starting with 6-9)
     * @param phone Phone number to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        // Remove spaces and hyphens
        phone = phone.replaceAll("[\\s-]", "");
        Pattern pattern = Pattern.compile(PHONE_PATTERN);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }
    
    /**
     * Validate password strength
     * @param password Password to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            return false;
        }
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
    
    /**
     * Check if string is not null or empty
     * @param str String to check
     * @return true if not empty, false otherwise
     */
    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }
    
    /**
     * Validate name (letters, spaces, and common punctuation only)
     * @param name Name to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        // Allow letters, spaces, apostrophes, hyphens, and periods
        Pattern pattern = Pattern.compile("^[a-zA-Z\\s.'-]{2,50}$");
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }
    
    /**
     * Validate address (minimum length check)
     * @param address Address to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidAddress(String address) {
        return address != null && address.trim().length() >= 10;
    }
    
    /**
     * Validate price (positive number with max 2 decimal places)
     * @param price Price to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPrice(double price) {
        return price > 0 && price <= 10000000; // Max price 1 crore
    }
    
    /**
     * Validate quantity (positive integer)
     * @param quantity Quantity to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidQuantity(int quantity) {
        return quantity > 0 && quantity <= 1000; // Max quantity 1000
    }
    
    /**
     * Validate discount percentage (0-100)
     * @param discount Discount percentage
     * @return true if valid, false otherwise
     */
    public static boolean isValidDiscount(double discount) {
        return discount >= 0 && discount <= 100;
    }
    
    /**
     * Sanitize input string (remove potential SQL injection characters)
     * @param input Input string
     * @return Sanitized string
     */
    public static String sanitizeInput(String input) {
        if (input == null) {
            return "";
        }
        // Remove potential SQL injection characters
        return input.replaceAll("[';\"\\\\]", "").trim();
    }
    
 
    public static String formatPhoneNumber(String phone) {
        if (phone == null || phone.length() != 10) {
            return phone;
        }
        return phone.substring(0, 3) + "-" + phone.substring(3, 6) + "-" + phone.substring(6);
    }
    
    /**
     * Format price with currency symbol
     * @param price Price value
     * @return Formatted price string
     */
    public static String formatPrice(double price) {
        return String.format("₹%.2f", price);
    }
    
    /**
     * Get validation error message for email
     * @param email Email to validate
     * @return Error message or null if valid
     */
    public static String getEmailError(String email) {
        if (email == null || email.trim().isEmpty()) {
            return "Email is required";
        }
        if (!isValidEmail(email)) {
            return "Invalid email format";
        }
        return null;
    }
    
    /**
     * Get validation error message for phone
     * @param phone Phone to validate
     * @return Error message or null if valid
     */
    public static String getPhoneError(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return "Phone number is required";
        }
        if (!isValidPhone(phone)) {
            return "Invalid phone number (must be 10 digits starting with 6-9)";
        }
        return null;
    }
    
    /**
     * Get validation error message for password
     * @param password Password to validate
     * @return Error message or null if valid
     */
    public static String getPasswordError(String password) {
        if (password == null || password.trim().isEmpty()) {
            return "Password is required";
        }
        if (password.length() < 6) {
            return "Password must be at least 6 characters long";
        }
        if (!isValidPassword(password)) {
            return "Password must contain at least one letter and one number";
        }
        return null;
    }
    
    /**
     * Get validation error message for name
     * @param name Name to validate
     * @return Error message or null if valid
     */
    public static String getNameError(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "Name is required";
        }
        if (!isValidName(name)) {
            return "Name must contain only letters and be 2-50 characters long";
        }
        return null;
    }
}