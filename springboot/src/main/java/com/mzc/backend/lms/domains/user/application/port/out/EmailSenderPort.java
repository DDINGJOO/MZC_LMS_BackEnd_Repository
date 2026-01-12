package com.mzc.backend.lms.domains.user.application.port.out;

/**
 * Email Sender Port
 * 이메일 발송을 위한 인터페이스
 */
public interface EmailSenderPort {

    /**
     * 이메일 발송
     *
     * @param to 수신자 이메일
     * @param subject 제목
     * @param content 내용 (HTML)
     */
    void send(String to, String subject, String content);

    /**
     * 인증 코드 이메일 발송
     *
     * @param to 수신자 이메일
     * @param verificationCode 인증 코드
     */
    void sendVerificationCode(String to, String verificationCode);
}
