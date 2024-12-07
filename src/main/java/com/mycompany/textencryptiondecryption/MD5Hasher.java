package com.mycompany.textencryptiondecryption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Hasher {

    public String hash(String text) {
        try {
            // Create MessageDigest 
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Compute the hash
            byte[] hashBytes = md.digest(text.getBytes());

            // Convert hash bytes to hexadecimal format
            return bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("MD5 algorithm is not available in this environment.", e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred while hashing.", e);
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }
}
