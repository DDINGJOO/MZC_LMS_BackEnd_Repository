package com.mzc.backend.lms.domains.user.application.port.in;

/**
 * 이메일 인증 유스케이스 인터페이스
 *
 * 책임:
 * - 인증 코드 발송
 * - 인증 코드 검증
 * - 인증 상태 관리
 */
public interface EmailVerificationUseCase {

    /**
     * 인증 코드 발송
     *
     * @param email 수신자 이메일
     */
    void sendVerificationCode(String email);

    /**
     * 인증 코드 검증
     *
     * @param email 이메일
     * @param code 인증 코드
     * @return true: 인증 성공, false: 인증 실패
     */
    boolean verifyCode(String email, String code);

    /**
     * 이메일 인증 완료 여부 확인
     *
     * @param email 이메일
     * @return true: 인증 완료, false: 미인증
     */
    boolean isVerified(String email);
}
