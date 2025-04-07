package com.example.mastertref.utils;

import android.util.Base64;
import android.util.Log;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

// Gen ra mật khẩu mã hóa
public class AESHelper {
    private static final String TAG = "AESHelper";
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String SECRET_KEY = "MySecretKey12345"; // 16 bytes
    private static final String IV = "1234567890123456"; // IV cố định 16 bytes

    // Mã hóa mật khẩu
    public static String encrypt(String password) {
        if (password == null || password.isEmpty()) {
            Log.e(TAG, "Password cannot be null or empty");
            return null;
        }

        try {
            IvParameterSpec ivSpec = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            byte[] encrypted = cipher.doFinal(password.getBytes(StandardCharsets.UTF_8));

            return Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (Exception e) {
            Log.e(TAG, "Error encrypting password", e);
            return null;
        }
    }

    // Giải mã mật khẩu
    public static String decrypt(String encryptedPassword) {
        if (encryptedPassword == null || encryptedPassword.isEmpty()) {
            Log.e(TAG, "Encrypted password cannot be null or empty");
            return null;
        }

        try {
            byte[] encrypted = Base64.decode(encryptedPassword, Base64.DEFAULT);
            IvParameterSpec ivSpec = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            byte[] decrypted = cipher.doFinal(encrypted);

            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            Log.e(TAG, "Error decrypting password", e);
            return null;
        }
    }
}
