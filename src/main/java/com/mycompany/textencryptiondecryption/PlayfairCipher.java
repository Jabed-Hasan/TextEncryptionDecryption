package com.mycompany.textencryptiondecryption;

public class PlayfairCipher {

    private char[][] matrix = new char[5][5];
    private String key;
    private static final String ALPHABET = "ABCDEFGHIKLMNOPQRSTUVWXYZ"; // I and J are combined

    public PlayfairCipher(String key) {
        this.key = key;
        buildMatrix();
    }

    // Build the matrix for the Playfair cipher using the key
    private void buildMatrix() {
        StringBuilder matrixContent = new StringBuilder();

        // Remove duplicate letters and add key to the matrix content
        for (char ch : key.toUpperCase().toCharArray()) {
            if (matrixContent.indexOf(String.valueOf(ch)) == -1 && ch != 'J') {
                matrixContent.append(ch);
            }
        }

        // Add remaining letters of the alphabet
        for (char ch : ALPHABET.toCharArray()) {
            if (matrixContent.indexOf(String.valueOf(ch)) == -1) {
                matrixContent.append(ch);
            }
        }

        // Fill the 5x5 matrix
        int index = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                matrix[i][j] = matrixContent.charAt(index++);
            }
        }
    }

    // Format the input text for the Playfair cipher
    private String formatText(String text) {
        text = text.toUpperCase().replace("J", "I").replaceAll("[^A-Z]", ""); // Remove non-letters, treat J as I
        StringBuilder formattedText = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char first = text.charAt(i);
            char second = (i + 1 < text.length()) ? text.charAt(i + 1) : 'X'; // Add 'X' if needed
            if (first == second) {
                formattedText.append(first).append('X');
            } else {
                formattedText.append(first).append(second);
                i++;
            }
        }
        if (formattedText.length() % 2 != 0) {
            formattedText.append('X'); // Ensure even length
        }
        return formattedText.toString();
    }

    // Process digraphs for encryption or decryption
    private String processDigraphs(String text, boolean encrypt) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i += 2) {
            char first = text.charAt(i);
            char second = text.charAt(i + 1);

            int[] firstPos = findPosition(first);
            int[] secondPos = findPosition(second);

            if (firstPos[0] == secondPos[0]) {
                // Same row
                result.append(matrix[firstPos[0]][(firstPos[1] + (encrypt ? 1 : 4)) % 5]);
                result.append(matrix[secondPos[0]][(secondPos[1] + (encrypt ? 1 : 4)) % 5]);
            } else if (firstPos[1] == secondPos[1]) {
                // Same column
                result.append(matrix[(firstPos[0] + (encrypt ? 1 : 4)) % 5][firstPos[1]]);
                result.append(matrix[(secondPos[0] + (encrypt ? 1 : 4)) % 5][secondPos[1]]);
            } else {
                // Rectangle swap
                result.append(matrix[firstPos[0]][secondPos[1]]);
                result.append(matrix[secondPos[0]][firstPos[1]]);
            }
        }
        return result.toString();
    }

    // Find the position of a letter in the matrix
    private int[] findPosition(char letter) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (matrix[i][j] == letter) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    // Encrypt the text using the Playfair cipher
    public String encrypt(String text) {
        text = formatText(text);
        return processDigraphs(text, true);
    }

    // Decrypt the text using the Playfair cipher
    public String decrypt(String text) {
        text = formatText(text); // Use the same formatting logic
        return processDigraphs(text, false); // Pass 'false' for decryption
    }
}
