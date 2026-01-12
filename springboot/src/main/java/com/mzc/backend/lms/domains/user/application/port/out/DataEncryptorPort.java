package com.mzc.backend.lms.domains.user.application.port.out;

/**
 * Data Encryptor Port
 * 개인정보 암/복호화를 위한 인터페이스 (AES-256)
 */
public interface DataEncryptorPort {

    /**
     * 이메일 암호화
     */
    String encryptEmail(String email);

    /**
     * 이메일 복호화
     */
    String decryptEmail(String encryptedEmail);

    /**
     * 이름 암호화
     */
    String encryptName(String name);

    /**
     * 이름 복호화
     */
    String decryptName(String encryptedName);

    /**
     * 전화번호 암호화
     */
    String encryptPhoneNumber(String phoneNumber);

    /**
     * 전화번호 복호화
     */
    String decryptPhoneNumber(String encryptedPhoneNumber);

    /**
     * 일반 개인정보 암호화
     */
    String encrypt(String data);

    /**
     * 일반 개인정보 복호화
     */
    String decrypt(String encryptedData);
}
