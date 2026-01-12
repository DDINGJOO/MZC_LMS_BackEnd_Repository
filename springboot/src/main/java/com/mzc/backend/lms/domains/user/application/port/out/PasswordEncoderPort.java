package com.mzc.backend.lms.domains.user.application.port.out;

/**
 * Password Encoder Port
 * 비밀번호 암호화 및 검증을 위한 인터페이스
 */
public interface PasswordEncoderPort {

    /**
     * 비밀번호 암호화
     *
     * @param rawPassword 평문 비밀번호
     * @return 암호화된 비밀번호
     */
    String encode(String rawPassword);

    /**
     * 비밀번호 매칭 확인
     *
     * @param rawPassword 평문 비밀번호
     * @param encodedPassword 암호화된 비밀번호
     * @return 일치 여부
     */
    boolean matches(String rawPassword, String encodedPassword);
}
