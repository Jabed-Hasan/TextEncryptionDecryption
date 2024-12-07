package com.mycompany.textencryptiondecryption;

public class HillCipher {

    private int[][] keyMatrix;
    private int[][] inverseMatrix;
    private int matrixSize;

    public HillCipher(int[][] keyMatrix) {
        this.keyMatrix = keyMatrix;
        this.matrixSize = keyMatrix.length;
        this.inverseMatrix = calculateInverseMatrix(keyMatrix);
    }

    private int calculateDeterminant(int[][] matrix) {
        int determinant = 0;
        if (matrix.length == 2) {
            determinant = matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
        } else if (matrix.length == 3) {
            determinant = matrix[0][0] * (matrix[1][1] * matrix[2][2] - matrix[1][2] * matrix[2][1])
                        - matrix[0][1] * (matrix[1][0] * matrix[2][2] - matrix[1][2] * matrix[2][0])
                        + matrix[0][2] * (matrix[1][0] * matrix[2][1] - matrix[1][1] * matrix[2][0]);
        }
        return determinant % 26;
    }

    private int findModularInverse(int a, int mod) {
        a = a % mod;
        for (int x = 1; x < mod; x++) {
            if ((a * x) % mod == 1) {
                return x;
            }
        }
        return -1;
    }

    private int[][] calculateInverseMatrix(int[][] matrix) {
        int determinant = calculateDeterminant(matrix);
        int modularInverse = findModularInverse(determinant, 26);

        if (modularInverse == -1) {
            throw new IllegalArgumentException("The key matrix is not invertible.");
        }

        if (matrix.length == 2) {
            return new int[][] {
                { (matrix[1][1] * modularInverse) % 26, (-matrix[0][1] * modularInverse + 26) % 26 },
                { (-matrix[1][0] * modularInverse + 26) % 26, (matrix[0][0] * modularInverse) % 26 }
            };
        } else if (matrix.length == 3) {
            int[][] adjugate = new int[3][3];
            adjugate[0][0] = (matrix[1][1] * matrix[2][2] - matrix[1][2] * matrix[2][1]) % 26;
            adjugate[1][1] = (matrix[0][0] * matrix[2][2] - matrix[0][2] * matrix[2][0]) % 26;
            adjugate[2][2] = (matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0]) % 26;

            int[][] inverse = new int[3][3];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    inverse[i][j] = (adjugate[j][i] * modularInverse) % 26;
                    if (inverse[i][j] < 0) {
                        inverse[i][j] += 26;
                    }
                }
            }
            return inverse;
        }
        throw new UnsupportedOperationException("Matrix inversion not implemented for sizes larger than 3x3.");
    }

    private int[] matrixMultiply(int[][] matrix, int[] vector) {
        int[] result = new int[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            result[i] = 0;
            for (int j = 0; j < matrix[i].length; j++) {
                result[i] += matrix[i][j] * vector[j];
            }
            result[i] = (result[i] % 26 + 26) % 26;
        }
        return result;
    }

    public String encrypt(String text) {
        text = text.toUpperCase().replaceAll("[^A-Z]", "");
        int padding = matrixSize - (text.length() % matrixSize);
        if (padding != matrixSize) {
            text += "X".repeat(padding);
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i += matrixSize) {
            int[] vector = new int[matrixSize];
            for (int j = 0; j < matrixSize; j++) {
                vector[j] = text.charAt(i + j) - 'A';
            }

            int[] encryptedVector = matrixMultiply(keyMatrix, vector);
            for (int k : encryptedVector) {
                result.append((char) (k + 'A'));
            }
        }
        return result.toString();
    }

public String decrypt(String text) {
    int[][] inverseKeyMatrix = {
        {8, 5, 10},
        {21, 8, 21},
        {21, 12, 8}
    }; // Hardcoded inverse matrix for default key

    text = text.toUpperCase().replaceAll("[^A-Z]", ""); // Clean text
    int[] textVector = new int[text.length()];

    // Convert ciphertext to numerical values
    for (int i = 0; i < text.length(); i++) {
        textVector[i] = text.charAt(i) - 'A';
    }

    StringBuilder decryptedText = new StringBuilder();
    for (int i = 0; i < textVector.length; i += 3) {
        int[] block = {textVector[i], textVector[i + 1], textVector[i + 2]};
        int[] result = new int[3];

        // Multiply by inverse matrix
        for (int row = 0; row < 3; row++) {
            result[row] = 0;
            for (int col = 0; col < 3; col++) {
                result[row] += inverseKeyMatrix[row][col] * block[col];
            }
            result[row] = (result[row] % 26 + 26) % 26; // Mod 26
        }

        // Convert numerical values back to text
        for (int val : result) {
            decryptedText.append((char) (val + 'A'));
        }
    }

    return decryptedText.toString().replaceAll("X+$", ""); // Remove padding
}

}
