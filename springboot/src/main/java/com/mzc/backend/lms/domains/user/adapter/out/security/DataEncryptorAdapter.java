package com.mzc.backend.lms.domains.user.adapter.out.security;

import com.mzc.backend.lms.domains.user.application.port.out.DataEncryptorPort;
import com.mzc.backend.lms.domains.user.exception.AuthException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Data Encryptor Adapter
 * AES-256 기반 개인정보 암/복호화 구현
 */
@Component
public class DataEncryptorAdapter implements DataEncryptorPort {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String AES = "AES";

    @Value("${security.aes.encryption-key:defaultSecretKey1234567890123456}")
    private String secretKey;

    @Value("${security.aes.iv:defaultIvParam456}")
    private String iv;

    @Override
    public String encryptEmail(String email) {
        return encrypt(email);
    }

    @Override
    public String decryptEmail(String encryptedEmail) {
        return decrypt(encryptedEmail);
    }

    @Override
    public String encryptName(String name) {
        return encrypt(name);
    }

    @Override
    public String decryptName(String encryptedName) {
        return decrypt(encryptedName);
    }

    @Override
    public String encryptPhoneNumber(String phoneNumber) {
        return encrypt(phoneNumber);
    }

    @Override
    public String decryptPhoneNumber(String encryptedPhoneNumber) {
        return decrypt(encryptedPhoneNumber);
    }

    @Override
    public String encrypt(String data) {
        if (data == null || data.isEmpty()) {
            return null;
        }

        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(getKey(), AES);
            IvParameterSpec ivSpec = new IvParameterSpec(getIv());

            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw AuthException.encryptionFailed(e);
        }
    }

    @Override
    public String decrypt(String encryptedData) {
        if (encryptedData == null || encryptedData.isEmpty()) {
            return null;
        }

        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(getKey(), AES);
            IvParameterSpec ivSpec = new IvParameterSpec(getIv());

            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
            byte[] decrypted = cipher.doFinal(decodedBytes);

            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw AuthException.decryptionFailed(e);
        }
    }

    private byte[] getKey() {
        byte[] key = new byte[32];
        byte[] paramBytes = secretKey.getBytes(StandardCharsets.UTF_8);

        int len = Math.min(paramBytes.length, key.length);
        System.arraycopy(paramBytes, 0, key, 0, len);

        return key;
    }

    private byte[] getIv() {
        byte[] ivBytes = new byte[16];
        byte[] paramBytes = iv.getBytes(StandardCharsets.UTF_8);

        int len = Math.min(paramBytes.length, ivBytes.length);
        System.arraycopy(paramBytes, 0, ivBytes, 0, len);

        return ivBytes;
    }
}
