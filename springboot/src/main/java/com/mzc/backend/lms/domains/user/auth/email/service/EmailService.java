package com.mzc.backend.lms.domains.user.auth.email.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * 이메일 발송 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:noreply@mzc-lms.com}")
    private String fromEmail;

    /**
     * 인증 코드 이메일 발송
     */
    public void sendVerificationCode(String toEmail, String verificationCode) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("[MZC LMS] 이메일 인증 코드");
            message.setText(buildVerificationEmailContent(verificationCode));

            mailSender.send(message);
            log.info("인증 코드 이메일 발송 완료: {}", toEmail);
        } catch (Exception e) {
            log.error("이메일 발송 실패: {}", toEmail, e);
            throw new RuntimeException("이메일 발송에 실패했습니다.", e);
        }
    }

    /**
     * 회원가입 완료 이메일 발송
     */
    public void sendWelcomeEmail(String toEmail, String name, String studentNumber) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("[MZC LMS] 회원가입을 환영합니다!");
            message.setText(buildWelcomeEmailContent(name, studentNumber));

            mailSender.send(message);
            log.info("환영 이메일 발송 완료: {}", toEmail);
        } catch (Exception e) {
            log.error("환영 이메일 발송 실패: {}", toEmail, e);
            // 환영 이메일은 실패해도 회원가입은 진행
        }
    }

    /**
     * 인증 코드 이메일 내용 생성
     */
    private String buildVerificationEmailContent(String verificationCode) {
        return String.format("""
            안녕하세요, MZC LMS입니다.

            요청하신 이메일 인증 코드를 안내드립니다.

            인증 코드: %s

            이 코드는 5분간 유효합니다.
            본인이 요청하지 않은 경우, 이 메일을 무시해주세요.

            감사합니다.
            MZC LMS 팀
            """, verificationCode);
    }

    /**
     * 환영 이메일 내용 생성
     */
    private String buildWelcomeEmailContent(String name, String identifier) {
        return String.format("""
            안녕하세요, %s님!

            MZC LMS 회원가입을 축하드립니다.

            회원 정보:
            - 이름: %s
            - 학번/교번: %s

            이제 MZC LMS의 모든 서비스를 이용하실 수 있습니다.

            문의사항이 있으시면 언제든지 연락주세요.

            감사합니다.
            MZC LMS 팀
            """, name, name, identifier);
    }
}