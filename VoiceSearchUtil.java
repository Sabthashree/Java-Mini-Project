package utils;

import javax.swing.*;
import java.util.Random;

public class VoiceSearchUtil {
    
    private static final String[] SAMPLE_QUERIES = {
        "smartphone",
        "laptop",
        "headphones",
        "running shoes",
        "t-shirt",
        "smart watch",
        "books",
        "cooking pan"
    };
    
    /**
     * Simulate voice search (captures voice input)
     * In a real implementation, this would use speech recognition API
     * @param parent Parent component
     * @return Search query from voice or null
     */
    public static String performVoiceSearch(JFrame parent) {
        // Show voice recording dialog
        JDialog voiceDialog = createVoiceDialog(parent);
        voiceDialog.setVisible(true);
        
        // Simulate voice capture (in real app, use speech recognition API)
        String voiceQuery = simulateVoiceCapture(parent);
        
        return voiceQuery;
    }
    
    /**
     * Create voice recording dialog
     * @param parent Parent component
     * @return Voice dialog
     */
    private static JDialog createVoiceDialog(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Voice Search", true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));
        
        // Microphone icon
        JLabel micLabel = new JLabel("🎤", SwingConstants.CENTER);
        micLabel.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 80));
        micLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        
        // Status label
        JLabel statusLabel = new JLabel("Speak now...", SwingConstants.CENTER);
        statusLabel.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 16));
        statusLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        
        // Cancel button
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        cancelButton.addActionListener(e -> dialog.dispose());
        
        dialog.add(Box.createVerticalStrut(20));
        dialog.add(micLabel);
        dialog.add(Box.createVerticalStrut(20));
        dialog.add(statusLabel);
        dialog.add(Box.createVerticalStrut(30));
        dialog.add(cancelButton);
        
        return dialog;
    }
    
    /**
     * Simulate voice capture and speech-to-text conversion
     * In real implementation, use:
     * - Google Speech-to-Text API
     * - CMU Sphinx (offline)
     * - Java Speech API
     * @param parent Parent component
     * @return Recognized text
     */
    private static String simulateVoiceCapture(JFrame parent) {
        // Show dialog explaining simulation
        Object[] options = {"Enter Text (Simulate Voice)", "Cancel"};
        int choice = JOptionPane.showOptionDialog(parent,
            "Voice Search Simulation\n\n" +
            "In a production app, this would:\n" +
            "1. Capture audio from microphone\n" +
            "2. Convert speech to text using AI\n" +
            "3. Return search query\n\n" +
            "For now, please type your search query:",
            "Voice Search",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            options,
            options[0]);
        
        if (choice == 0) {
            // Get text input
            String input = JOptionPane.showInputDialog(parent,
                "What are you looking for?",
                "Voice Search",
                JOptionPane.PLAIN_MESSAGE);
            
            if (input != null && !input.trim().isEmpty()) {
                return input.trim();
            }
        }
        
        return null;
    }
    
    /**
     * Get sample voice query (for demo purposes)
     * @return Random sample query
     */
    public static String getSampleQuery() {
        Random random = new Random();
        return SAMPLE_QUERIES[random.nextInt(SAMPLE_QUERIES.length)];
    }
    
    /**
     * Process voice input and extract keywords
     * @param voiceInput Raw voice input
     * @return Processed search keywords
     */
    public static String processVoiceInput(String voiceInput) {
        if (voiceInput == null || voiceInput.trim().isEmpty()) {
            return null;
        }
        
        // Convert to lowercase
        String processed = voiceInput.toLowerCase().trim();
        
        // Remove common filler words
        processed = processed.replaceAll("\\b(um|uh|like|you know|basically)\\b", "");
        
        // Remove extra spaces
        processed = processed.replaceAll("\\s+", " ").trim();
        
        // Extract key phrases
        processed = extractKeyPhrases(processed);
        
        return processed;
    }
    
    /**
     * Extract key phrases from voice input
     * @param input Processed input
     * @return Key phrases
     */
    private static String extractKeyPhrases(String input) {
        // Remove question words
        input = input.replaceAll("^(what|where|when|who|which|how|show me|find|search for)\\s+", "");
        
        // Remove articles
        input = input.replaceAll("\\b(a|an|the)\\b", "");
        
        // Clean up
        return input.replaceAll("\\s+", " ").trim();
    }
    
    /**
     * Check if voice search is supported (microphone available)
     * @return true if supported
     */
    public static boolean isVoiceSearchSupported() {
        // In real implementation, check for:
        // 1. Microphone availability
        // 2. Speech recognition API availability
        // 3. Internet connection (if using cloud API)
        
        // For simulation, always return true
        return true;
    }
    
    /**
     * Get voice search confidence score (simulation)
     * @param recognizedText Recognized text
     * @return Confidence score (0.0 to 1.0)
     */
    public static double getConfidenceScore(String recognizedText) {
        if (recognizedText == null || recognizedText.trim().isEmpty()) {
            return 0.0;
        }
        
        // Simulate confidence based on text length and clarity
        int length = recognizedText.length();
        
        if (length < 3) {
            return 0.3;
        } else if (length < 10) {
            return 0.7;
        } else if (length < 30) {
            return 0.9;
        } else {
            return 0.95;
        }
    }
    
    /**
     * Format voice query for display
     * @param query Voice query
     * @return Formatted query
     */
    public static String formatVoiceQuery(String query) {
        if (query == null || query.trim().isEmpty()) {
            return "";
        }
        
        // Capitalize first letter
        return query.substring(0, 1).toUpperCase() + query.substring(1);
    }
}