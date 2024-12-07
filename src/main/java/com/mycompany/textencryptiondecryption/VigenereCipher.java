package com.mycompany.textencryptiondecryption;

public class VigenereCipher {

    // Encrypts the plaintext using the Vigenère cipher
    public String encrypt(String text, String key) {
        StringBuilder result = new StringBuilder();
        text = text.toUpperCase().replaceAll("[^A-Z]", ""); // Remove non-alphabetic characters
        key = key.toUpperCase();

        for (int i = 0, j = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c >= 'A' && c <= 'Z') {
                result.append((char) ((c + key.charAt(j) - 2 * 'A') % 26 + 'A'));
                j = (j + 1) % key.length();
            }
        }
        return result.toString();
    }

    // Decrypts the ciphertext using the Vigenère cipher
    public String decrypt(String text, String key) {
        StringBuilder result = new StringBuilder();
        text = text.toUpperCase().replaceAll("[^A-Z]", ""); // Remove non-alphabetic characters
        key = key.toUpperCase();

        for (int i = 0, j = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c >= 'A' && c <= 'Z') {
                result.append((char) ((c - key.charAt(j) + 26) % 26 + 'A'));
                j = (j + 1) % key.length();
            }
        }
        return result.toString();
    }
}
