package com.cheesyguy.FileEncryptApp.EncryptionHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.AlgorithmParameters;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Decryption {

    private static final String keyAlgorithm = "PBKDF2WithHmacSHA256";

    private static final String specification = "AES";

    private static final String cipherInstance = "AES/CBC/PKCS5Padding";


    int getExtension(String filePath) {
        return filePath.lastIndexOf(".");
    }
    public String decrypt(String encryptedFilePath, String password) {

        File encryptedFile = new File(encryptedFilePath);
        String fileName = encryptedFile.getName();

        try {

            FileInputStream fileInputStream = new FileInputStream(encryptedFile);

            //Get salt and iv from encrypted file headers. Note that this means this decrypt function will only work with the corresponding encrypt algorithm in the same class
            byte[] salt = new byte[8];
            fileInputStream.read(salt, 0, 8);

            byte[] iv = new byte[16];
            fileInputStream.read(iv, 0, 16);

            //Setup secret key generation algorithm using the Password-Based Key Derivation Function found in PKCS #5 v2.0.
            SecretKeyFactory factory = SecretKeyFactory.getInstance(keyAlgorithm);
            //Values must match all values used to do the encryption.
            //65536 and 256 is the standard combination for security. 256 requires Java Cryptography Extension (JCE) to be installed however, 128 can be used in default Java
            KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            SecretKey secretKey = factory.generateSecret(keySpec);
            //Use AES specification
            SecretKey secret = new SecretKeySpec(secretKey.getEncoded(), specification);

            //Initialise cipher in decrypt mode use the same secret key and iv as used to encrypt
            Cipher cipher = Cipher.getInstance(cipherInstance);
            cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));



            //Remove the .aes extension from the file
            fileName = encryptedFilePath.substring(0, encryptedFilePath.length() - 4);

            String extension = fileName.substring(getExtension(fileName) + 1);
            fileName = fileName.substring(0, getExtension(fileName));

            String modifiedName = fileName + "(dec)." + extension;
            File decryptedFile = new File(modifiedName);
            try (FileOutputStream fileOutputStream = new FileOutputStream(decryptedFile)) {
                //While there is still content to be decrypted
                byte[] in = new byte[64];
                int read;
                while ((read = fileInputStream.read(in)) != -1) {
                    byte[] output = cipher.update(in, 0, read);
                    if (output != null) {
                        fileOutputStream.write(output);
                    }
                }

                //Finalise decryption
                byte[] output = cipher.doFinal();
                if (output != null) {
                    fileOutputStream.write(output);
                }

                fileInputStream.close();
                fileOutputStream.flush();

            } catch (Exception e) {

                //Close input stream. Output stream closed by Java try block
                fileInputStream.close();

                //Delete left over file if decryption failed
                decryptedFile.delete();

                e.printStackTrace();
                return "false";
            }

            return decryptedFile.getPath();

        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }

    }
}
