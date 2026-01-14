package com.mzc.backend.lms.domains.user.adapter.out.security;

import com.mzc.backend.lms.domains.user.application.service.EncryptionService;
import com.mzc.backend.lms.domains.user.application.port.out.DataEncryptionPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 데이터 암호화/복호화 어댑터
 */
@Component
@RequiredArgsConstructor
public class DataEncryptionAdapter implements DataEncryptionPort {

    private final EncryptionService encryptionService;

    @Override
    public String encryptEmail(String email) {
        return encryptionService.encryptEmail(email);
    }

    @Override
    public String decryptEmail(String encryptedEmail) {
        return encryptionService.decryptEmail(encryptedEmail);
    }

    @Override
    public String encryptName(String name) {
        return encryptionService.encryptName(name);
    }

    @Override
    public String decryptName(String encryptedName) {
        return encryptionService.decryptName(encryptedName);
    }

    @Override
    public String encryptPhoneNumber(String phoneNumber) {
        return encryptionService.encryptPhoneNumber(phoneNumber);
    }

    @Override
    public String decryptPhoneNumber(String encryptedPhoneNumber) {
        return encryptionService.decryptPhoneNumber(encryptedPhoneNumber);
    }
}
