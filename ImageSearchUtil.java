package utils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class ImageSearchUtil {
    
    /**
     * Open file chooser to select an image
     * @param parent Parent component
     * @return Selected file or null
     */
    public static File selectImage(JFrame parent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Product Image");
        
        // Set file filter for images
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Image Files", "jpg", "jpeg", "png", "gif", "bmp");
        fileChooser.setFileFilter(filter);
        
        int result = fileChooser.showOpenDialog(parent);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        
        return null;
    }
    
    /**
     * Validate if file is a valid image
     * @param file File to validate
     * @return true if valid image, false otherwise
     */
    public static boolean isValidImage(File file) {
        if (file == null || !file.exists()) {
            return false;
        }
        
        try {
            BufferedImage image = ImageIO.read(file);
            return image != null;
        } catch (IOException e) {
            return false;
        }
    }
    
    /**
     * Get image dimensions
     * @param file Image file
     * @return String with dimensions (WIDTHxHEIGHT) or null
     */
    public static String getImageDimensions(File file) {
        try {
            BufferedImage image = ImageIO.read(file);
            if (image != null) {
                return image.getWidth() + "x" + image.getHeight();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Simulate image search (finds products based on image analysis)
     * In a real implementation, this would use computer vision/ML
     * @param imageFile Image file to search
     * @return Search keywords extracted from image
     */
    public static String extractSearchKeywords(File imageFile) {
        // This is a simulation. In real implementation:
        // 1. Use computer vision API (Google Vision, AWS Rekognition)
        // 2. Extract objects, colors, text from image
        // 3. Generate relevant search keywords
        
        if (!isValidImage(imageFile)) {
            return null;
        }
        
        // Simulated keywords based on file name (for demo purposes)
        String fileName = imageFile.getName().toLowerCase();
        
        if (fileName.contains("phone") || fileName.contains("mobile")) {
            return "smartphone";
        } else if (fileName.contains("laptop") || fileName.contains("computer")) {
            return "laptop";
        } else if (fileName.contains("shoe") || fileName.contains("footwear")) {
            return "shoes";
        } else if (fileName.contains("shirt") || fileName.contains("tshirt")) {
            return "shirt";
        } else if (fileName.contains("watch")) {
            return "watch";
        } else if (fileName.contains("book")) {
            return "book";
        }
        
        // Default: suggest general electronics category
        return "electronics";
    }
    
    /**
     * Process image search request
     * @param parent Parent component
     * @return Search keywords or null
     */
    public static String performImageSearch(JFrame parent) {
        // Show instruction dialog
        int choice = JOptionPane.showConfirmDialog(parent,
            "Select an image of a product to search for similar items.\n" +
            "Supported formats: JPG, PNG, GIF, BMP",
            "Image Search",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.INFORMATION_MESSAGE);
        
        if (choice != JOptionPane.OK_OPTION) {
            return null;
        }
        
        // Select image
        File imageFile = selectImage(parent);
        
        if (imageFile == null) {
            return null;
        }
        
        // Validate image
        if (!isValidImage(imageFile)) {
            JOptionPane.showMessageDialog(parent,
                "Invalid image file. Please select a valid image.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return null;
        }
        
        // Extract keywords
        String keywords = extractSearchKeywords(imageFile);
        
        if (keywords != null) {
            JOptionPane.showMessageDialog(parent,
                "Analyzing image...\nFound: " + keywords,
                "Image Analysis",
                JOptionPane.INFORMATION_MESSAGE);
        }
        
        return keywords;
    }
    
    /**
     * Get supported image formats
     * @return Array of supported extensions
     */
    public static String[] getSupportedFormats() {
        return new String[]{"jpg", "jpeg", "png", "gif", "bmp"};
    }
    
    /**
     * Check if file has valid image extension
     * @param file File to check
     * @return true if has valid extension
     */
    public static boolean hasValidImageExtension(File file) {
        if (file == null) {
            return false;
        }
        
        String name = file.getName().toLowerCase();
        for (String format : getSupportedFormats()) {
            if (name.endsWith("." + format)) {
                return true;
            }
        }
        return false;
    }

    public static ImageIcon loadAndScaleImage(String firstImage, int i, int j) {
        throw new UnsupportedOperationException("Unimplemented method 'loadAndScaleImage'");
    }
}