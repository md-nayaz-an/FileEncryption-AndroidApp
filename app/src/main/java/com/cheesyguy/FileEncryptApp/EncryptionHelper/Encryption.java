package com.cheesyguy.FileEncryptApp.EncryptionHelper;

import androidx.annotation.NonNull;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class Encryption {
    String buildOutputPath (@NonNull String inputPath) {
        String newExtension = ".enc";

        String OutputPath = inputPath.substring(0, inputPath.lastIndexOf(".")) + newExtension;

        return OutputPath;
    }

    public boolean encrypt(String inputPath, @NonNull String password ) {

        String outputPath = buildOutputPath(inputPath);
        
        try {
            // Generate a random salt
            SecureRandom secureRandom = new SecureRandom();
            byte[] salt = new byte[16];
            secureRandom.nextBytes(salt);

            // Generate a SecretKey using PBKDF2
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
            SecretKey secretKey = factory.generateSecret(keySpec);
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");

            // Create and initialize the cipher
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

            // Read the input file and encrypt the data
            FileInputStream inputStream = new FileInputStream(inputPath);
            FileOutputStream outputStream = new FileOutputStream(outputPath);

            outputStream.write(salt); // Write the salt to the output file

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byte[] encryptedBytes = cipher.update(buffer, 0, bytesRead);
                if (encryptedBytes != null) {
                    outputStream.write(encryptedBytes);
                }
            }

            byte[] finalBytes = cipher.doFinal();
            if (finalBytes != null) {
                outputStream.write(finalBytes);
            }

            inputStream.close();
            outputStream.flush();
            outputStream.close();

            System.out.println("File encrypted successfully.");
            return true;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 IOException | IllegalBlockSizeException | BadPaddingException |
                 InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return false;
    }
}
