package com.mzc.backend.lms.domains.user.application.service;

import com.mzc.backend.lms.domains.user.application.port.in.CheckEmailUseCase;
import com.mzc.backend.lms.domains.user.application.port.out.DataEncryptorPort;
import com.mzc.backend.lms.domains.user.application.port.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

/**
 * 이메일 확인 서비스
 * CheckEmailUseCase 구현체 (Port 기반)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CheckEmailService implements CheckEmailUseCase {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private final UserRepositoryPort userRepositoryPort;
    private final DataEncryptorPort dataEncryptorPort;

    @Override
    @Transactional(readOnly = true)
    public boolean execute(String email) {
        if (!isValidFormat(email)) {
            return false;
        }

        String encryptedEmail = dataEncryptorPort.encryptEmail(email);
        boolean available = !userRepositoryPort.existsByEmail(encryptedEmail);

        log.debug("이메일 중복 확인: email={}, available={}", email, available);

        return available;
    }

    @Override
    public boolean isValidFormat(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        return EMAIL_PATTERN.matcher(email).matches();
    }
}
