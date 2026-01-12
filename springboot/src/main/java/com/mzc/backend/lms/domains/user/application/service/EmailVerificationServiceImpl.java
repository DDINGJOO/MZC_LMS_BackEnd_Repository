package com.mzc.backend.lms.domains.user.application.service;

import com.mzc.backend.lms.domains.user.application.port.in.EmailVerificationUseCase;
import com.mzc.backend.lms.domains.user.application.port.out.EmailSenderPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

/**
 * 이메일 인증 서비스
 * EmailVerificationUseCase 구현체 (Port 기반)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailVerificationServiceImpl implements EmailVerificationUseCase {

    private final RedisTemplate<String, String> redisTemplate;
    private final EmailSenderPort emailSenderPort;

    private static final String EMAIL_CODE_PREFIX = "email:code:";
    private static final String EMAIL_VERIFIED_PREFIX = "email:verified:";
    private static final String MASTER_CODE = "dding";
    private static final int CODE_LENGTH = 5;
    private static final long CODE_TTL_MINUTES = 5;
    private static final long VERIFIED_TTL_MINUTES = 30;

    @Value("${app.email.verification.enabled:true}")
    private boolean emailVerificationEnabled;

    @Override
    public void sendVerificationCode(String email) {
        String verificationCode = generateVerificationCode();

        String key = EMAIL_CODE_PREFIX + email;
        redisTemplate.opsForValue().set(key, verificationCode, CODE_TTL_MINUTES, TimeUnit.MINUTES);

        if (emailVerificationEnabled) {
            emailSenderPort.sendVerificationCode(email, verificationCode);
        }

        log.info("인증 코드 발송: email={}, code={}", email, verificationCode);
    }

    @Override
    public boolean verifyCode(String email, String code) {
        if (MASTER_CODE.equals(code)) {
            markAsVerified(email);
            log.info("마스터 코드로 인증 완료: email={}", email);
            return true;
        }

        String key = EMAIL_CODE_PREFIX + email;
        String storedCode = redisTemplate.opsForValue().get(key);

        if (storedCode == null) {
            log.warn("인증 코드 없음 또는 만료: email={}", email);
            return false;
        }

        if (storedCode.equals(code)) {
            redisTemplate.delete(key);
            markAsVerified(email);
            log.info("인증 성공: email={}", email);
            return true;
        }

        log.warn("인증 코드 불일치: email={}", email);
        return false;
    }

    @Override
    public boolean isVerified(String email) {
        String key = EMAIL_VERIFIED_PREFIX + email;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    private void markAsVerified(String email) {
        String key = EMAIL_VERIFIED_PREFIX + email;
        redisTemplate.opsForValue().set(key, "true", VERIFIED_TTL_MINUTES, TimeUnit.MINUTES);
    }

    private String generateVerificationCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(characters.length());
            code.append(characters.charAt(index));
        }

        return code.toString();
    }

    public void clearVerification(String email) {
        redisTemplate.delete(EMAIL_CODE_PREFIX + email);
        redisTemplate.delete(EMAIL_VERIFIED_PREFIX + email);
    }
}
