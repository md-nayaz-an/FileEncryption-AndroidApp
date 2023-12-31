package com.cheesyguy.FileEncryptApp.EncryptionHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.AlgorithmParameters;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Encryption {

    private static final String keyAlgorithm = "PBKDF2WithHmacSHA256";

    private static final String specification = "AES";

    private static final String cipherInstance = "AES/CBC/PKCS5Padding";

    public String encrypt(String rawFilePath, String password) {

        File rawFile = new File(rawFilePath);
        String fileName = rawFile.getName();

        byte[] salt = new byte[8];

        SecureRandom secureRandom = new SecureRandom();

        //Generate new 8 byte salt using SecureRandom
        secureRandom.nextBytes(salt);

        try {
            //Setup secret key generation algorithm using the Password-Based Key Derivation Function found in PKCS #5 v2.0.
            SecretKeyFactory factory = SecretKeyFactory.getInstance(keyAlgorithm);
            //NOTE: 128 is secure enough, however to maximise security please install the JCE from the Oracle Java website and change to 256.
            //65536 and 256 is the standard combination for security. 256 requires Java Cryptography Extension (JCE) to be installed however, 128 can be used in default Java
            KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            SecretKey secretKey = factory.generateSecret(keySpec);
            //Use AES specification
            SecretKey secret = new SecretKeySpec(secretKey.getEncoded(), specification);

            //Setup Cipher with AES (128 keysize)
            Cipher cipher = Cipher.getInstance(cipherInstance);
            cipher.init(Cipher.ENCRYPT_MODE, secret);
            AlgorithmParameters params = cipher.getParameters();

            //Setup initialisation vector to add randomness to the encryption process
            byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();


            FileInputStream fileInputStream = new FileInputStream(rawFile);

            //Create output file to store on disk
            File encryptedFile = new File(rawFilePath + ".aes");
            try (FileOutputStream fileOutputStream = new FileOutputStream(encryptedFile)) {

                //Add the salt and IV to the encrypted file as headers
                fileOutputStream.write(salt);
                fileOutputStream.write(iv);

                byte[] input = new byte[64];
                int bytesRead;

                //While there are still file contents to be encrypted
                while ((bytesRead = fileInputStream.read(input)) != -1) {
                    byte[] output = cipher.update(input, 0, bytesRead);
                    if (output != null) {
                        fileOutputStream.write(output);
                    }
                }

                //Finalise the encryption
                byte[] output = cipher.doFinal();
                if (output != null) {
                    fileOutputStream.write(output);
                }

                fileInputStream.close();
                fileOutputStream.flush();
            } catch (Exception e) {

                //Close input stream. Output stream closed by Java try block
                fileInputStream.close();

                //Delete left over file if encryption failed
                encryptedFile.delete();
                e.printStackTrace();
                return "false";
            }

            return encryptedFile.getPath();

        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }

    }
}
