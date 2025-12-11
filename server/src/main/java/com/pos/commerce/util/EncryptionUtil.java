package com.pos.commerce.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class EncryptionUtil {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";

    /**
     * AES 키를 올바른 길이로 조정합니다 (16, 24, 또는 32바이트).
     */
    private static byte[] adjustKey(String secretKey) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        int keyLength = keyBytes.length;
        
        if (keyLength == 16 || keyLength == 24 || keyLength == 32) {
            return keyBytes;
        } else if (keyLength < 16) {
            // 16바이트 미만이면 반복하여 16바이트로 확장
            byte[] adjustedKey = new byte[16];
            System.arraycopy(keyBytes, 0, adjustedKey, 0, keyLength);
            for (int i = keyLength; i < 16; i++) {
                adjustedKey[i] = keyBytes[i % keyLength];
            }
            return adjustedKey;
        } else if (keyLength < 24) {
            // 16바이트 이상 24바이트 미만이면 24바이트로 확장
            byte[] adjustedKey = new byte[24];
            System.arraycopy(keyBytes, 0, adjustedKey, 0, keyLength);
            for (int i = keyLength; i < 24; i++) {
                adjustedKey[i] = keyBytes[i % keyLength];
            }
            return adjustedKey;
        } else {
            // 24바이트 이상이면 32바이트로 확장하거나 자름
            byte[] adjustedKey = new byte[32];
            System.arraycopy(keyBytes, 0, adjustedKey, 0, Math.min(keyLength, 32));
            if (keyLength < 32) {
                for (int i = keyLength; i < 32; i++) {
                    adjustedKey[i] = keyBytes[i % keyLength];
                }
            }
            return adjustedKey;
        }
    }

    /**
     * 문자열을 암호화합니다.
     */
    public static String encrypt(String plainText, String secretKey) {
        try {
            byte[] adjustedKey = adjustKey(secretKey);
            SecretKeySpec secretKeySpec = new SecretKeySpec(adjustedKey, ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("암호화 실패", e);
        }
    }

    /**
     * 암호화된 문자열을 복호화합니다.
     */
    public static String decrypt(String encryptedText, String secretKey) {
        try {
            byte[] adjustedKey = adjustKey(secretKey);
            SecretKeySpec secretKeySpec = new SecretKeySpec(adjustedKey, ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("복호화 실패", e);
        }
    }
}

