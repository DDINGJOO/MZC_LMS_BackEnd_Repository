package com.mzc.backend.lms.domains.user.application.port.out;

import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.EmailVerification;

import java.util.Optional;

/**
 * EmailVerification 영속성 Port
 * 이메일 인증 정보 접근을 위한 인터페이스
 */
public interface EmailVerificationRepositoryPort {

    /**
     * 이메일 인증 정보 저장
     */
    EmailVerification save(EmailVerification emailVerification);

    /**
     * 이메일로 인증 정보 조회
     */
    Optional<EmailVerification> findByEmail(String email);

    /**
     * 이메일로 인증 정보 삭제
     */
    void deleteByEmail(String email);

    /**
     * 만료된 인증 정보 삭제
     */
    int deleteExpiredVerifications();
}
