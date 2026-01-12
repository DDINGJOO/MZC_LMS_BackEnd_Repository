package com.mzc.backend.lms.domains.user.adapter.out.persistence;

import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.EmailVerification;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.repository.EmailVerificationRepository;
import com.mzc.backend.lms.domains.user.application.port.out.EmailVerificationRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * EmailVerification 영속성 Adapter
 */
@Component
@RequiredArgsConstructor
public class EmailVerificationPersistenceAdapter implements EmailVerificationRepositoryPort {

    private final EmailVerificationRepository emailVerificationRepository;

    @Override
    public EmailVerification save(EmailVerification emailVerification) {
        return emailVerificationRepository.save(emailVerification);
    }

    @Override
    public Optional<EmailVerification> findByEmail(String email) {
        return emailVerificationRepository.findTopByEmailOrderByCreatedAtDesc(email);
    }

    @Override
    public void deleteByEmail(String email) {
        emailVerificationRepository.deleteByEmail(email);
    }

    @Override
    public int deleteExpiredVerifications() {
        return emailVerificationRepository.deleteExpiredVerifications(LocalDateTime.now());
    }
}
