package com.mycompany.textencryptiondecryption;

import javax.swing.*;
import java.awt.*;
import java.security.MessageDigest;

public class TextEncryptionDecryption extends JFrame {

    // GUI components
    private JTextArea inputTextArea, outputTextArea;
    private JTextField keyTextField;
    private JButton processButton;
    private JComboBox<String> modeSelection, cipherSelection, hashSelection;

    // Cipher instances
    private VigenereCipher vigenereCipher;
    private HillCipher hillCipher;

    public TextEncryptionDecryption() {
        // Initialize cipher instances
        vigenereCipher = new VigenereCipher();
        hillCipher = new HillCipher(new int[][]{
            {6, 24, 1},
            {13, 16, 10},
            {20, 17, 15}
        });

        // GUI setup
        setTitle("CRYPTO X");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel backgroundLabel = new JLabel(new ImageIcon("backgnd.jpg"));
        backgroundLabel.setBounds(0, 0, 600, 600);
        add(backgroundLabel);

        JPanel container = new JPanel();
        container.setLayout(null);
        container.setBackground(new Color(255, 255, 255, 150));
        container.setBounds(50, 50, 500, 500);
        backgroundLabel.add(container);

        JLabel titleLabel = new JLabel("Text Encryption - Decryption - Hashing");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBounds(100, 20, 400, 30);
        container.add(titleLabel);

        JLabel inputLabel = new JLabel("Input Text:");
        inputLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        inputLabel.setBounds(50, 60, 80, 25);
        container.add(inputLabel);

        inputTextArea = new JTextArea();
        inputTextArea.setBounds(50, 90, 400, 50);
        inputTextArea.setLineWrap(true);
        inputTextArea.setWrapStyleWord(true);
        container.add(inputTextArea);

        JLabel modeLabel = new JLabel("Select Mode:");
        modeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        modeLabel.setBounds(50, 150, 100, 25);
        container.add(modeLabel);

        String[] modes = {"Text Encryption", "Text Decryption", "Hashing"};
        modeSelection = new JComboBox<>(modes);
        modeSelection.setBounds(160, 150, 200, 25);
        modeSelection.addActionListener(e -> toggleMode());
        container.add(modeSelection);

        JLabel cipherLabel = new JLabel("Select Cipher:");
        cipherLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        cipherLabel.setBounds(50, 190, 100, 25);
        container.add(cipherLabel);

        String[] ciphers = {"Playfair Cipher", "Vigenère Cipher", "Hill Cipher"};
        cipherSelection = new JComboBox<>(ciphers);
        cipherSelection.setBounds(160, 190, 200, 25);
        container.add(cipherSelection);

        JLabel hashLabel = new JLabel("Select Hash:");
        hashLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        hashLabel.setBounds(50, 230, 100, 25);
        container.add(hashLabel);

        String[] hashes = {"SHA-512", "MD5"};
        hashSelection = new JComboBox<>(hashes);
        hashSelection.setBounds(160, 230, 200, 25);
        container.add(hashSelection);

        JLabel keyLabel = new JLabel("Key:");
        keyLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        keyLabel.setBounds(50, 270, 80, 25);
        container.add(keyLabel);

        keyTextField = new JTextField();
        keyTextField.setBounds(160, 270, 200, 25);
        container.add(keyTextField);

        processButton = new JButton("Process");
        processButton.setBounds(200, 310, 100, 30);
        processButton.addActionListener(e -> processText());
        container.add(processButton);

        JLabel outputLabel = new JLabel("Output Generated:");
        outputLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        outputLabel.setBounds(50, 350, 150, 25);
        container.add(outputLabel);

        outputTextArea = new JTextArea();
        outputTextArea.setBounds(50, 380, 400, 50);
        outputTextArea.setEditable(false);
        outputTextArea.setLineWrap(true);
        outputTextArea.setWrapStyleWord(true);
        container.add(outputTextArea);

        toggleMode();
        setVisible(true);
    }

    private void toggleMode() {
        String selectedMode = (String) modeSelection.getSelectedItem();

        if ("Hashing".equals(selectedMode)) {
            cipherSelection.setEnabled(false);
            hashSelection.setEnabled(true);
            keyTextField.setEnabled(false); // Key is not needed for hashing
        } else {
            cipherSelection.setEnabled(true);
            hashSelection.setEnabled(false);
            keyTextField.setEnabled(true); // Key is required for encryption/decryption
        }
    }

    private void processText() {
        String inputText = inputTextArea.getText().trim();
        String selectedMode = (String) modeSelection.getSelectedItem();
        String processedText = "";

        try {
            if ("Hashing".equals(selectedMode)) {
                // Hashing Mode
                String selectedHash = (String) hashSelection.getSelectedItem();
                if (inputText.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter text to hash", "Input Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if ("MD5".equals(selectedHash)) {
                    MD5Hasher md5Hasher = new MD5Hasher();
                    processedText = md5Hasher.hash(inputText);
                } else if ("SHA-512".equals(selectedHash)) {
                    SHA512Hasher sha512Hasher = new SHA512Hasher();
                    processedText = sha512Hasher.hash(inputText);
                }
            } else if ("Text Encryption".equals(selectedMode) || "Text Decryption".equals(selectedMode)) {
                // Text Encryption/Decryption Mode
                String selectedCipher = (String) cipherSelection.getSelectedItem();
                String key = keyTextField.getText().trim();

                if (inputText.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter text to process", "Input Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if ("Playfair Cipher".equals(selectedCipher)) {
                    if (key.isEmpty()) key = "DEFAULTKEY"; // Use a default key
                    PlayfairCipher playfairCipher = new PlayfairCipher(key);
                    processedText = "Text Encryption".equals(selectedMode)
                            ? playfairCipher.encrypt(inputText)
                            : playfairCipher.decrypt(inputText);
                } else if ("Vigenère Cipher".equals(selectedCipher)) {
                    if (key.isEmpty()) key = "DEFAULTKEY"; // Use a default key
                    processedText = "Text Encryption".equals(selectedMode)
                            ? vigenereCipher.encrypt(inputText, key)
                            : vigenereCipher.decrypt(inputText, key);
                } else if ("Hill Cipher".equals(selectedCipher)) {
                    processedText = "Text Encryption".equals(selectedMode)
                            ? hillCipher.encrypt(inputText)
                            : hillCipher.decrypt(inputText);
                }
            }

            // Display the result
            outputTextArea.setText(processedText);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Processing Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new TextEncryptionDecryption();
    }
}
