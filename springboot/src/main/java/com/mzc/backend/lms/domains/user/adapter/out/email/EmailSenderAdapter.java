package com.mzc.backend.lms.domains.user.adapter.out.email;

import com.mzc.backend.lms.domains.user.application.port.out.EmailSenderPort;
import com.mzc.backend.lms.domains.user.exception.AuthException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * Email Sender Adapter
 * 이메일 발송 구현
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EmailSenderAdapter implements EmailSenderPort {

    private final JavaMailSender mailSender;

    @Value("${email.from}")
    private String fromEmail;

    @Value("${email.from-name}")
    private String fromName;

    @Override
    @Async("emailExecutor")
    public void send(String to, String subject, String content) {
        try {
            MimeMessage mimeMessage = createMimeMessage(to, subject, content);
            mailSender.send(mimeMessage);
            log.info("이메일 전송 완료: to={}, subject={}", to, subject);
        } catch (Exception e) {
            log.error("이메일 전송 실패: to={}, error={}", to, e.getMessage(), e);
            throw AuthException.emailSendFailed(e);
        }
    }

    @Override
    @Async("emailExecutor")
    public void sendVerificationCode(String to, String verificationCode) {
        String subject = "[LMS] 이메일 인증 코드";
        String content = generateVerificationEmailContent(verificationCode);
        send(to, subject, content);
    }

    private MimeMessage createMimeMessage(String to, String subject, String content)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage,
                true,
                StandardCharsets.UTF_8.name()
        );

        helper.setFrom(fromEmail, fromName);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);

        return mimeMessage;
    }

    private String generateVerificationEmailContent(String verificationCode) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html>");
        html.append("<head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<style>");
        html.append("body { font-family: 'Noto Sans KR', sans-serif; line-height: 1.6; color: #333; }");
        html.append(".container { max-width: 600px; margin: 0 auto; padding: 20px; }");
        html.append(".header { background: #007bff; color: white; padding: 20px; text-align: center; }");
        html.append(".content { padding: 20px; background: #f9f9f9; }");
        html.append(".footer { text-align: center; padding: 10px; color: #666; font-size: 12px; }");
        html.append(".code { font-size: 24px; font-weight: bold; color: #007bff; text-align: center; padding: 20px; background: white; border-radius: 5px; margin: 20px 0; }");
        html.append("</style>");
        html.append("</head>");
        html.append("<body>");
        html.append("<div class='container'>");
        html.append("<div class='header'><h1>LMS System</h1></div>");
        html.append("<div class='content'>");
        html.append("<h2>이메일 인증</h2>");
        html.append("<p>아래 인증 코드를 입력해주세요:</p>");
        html.append("<div class='code'>").append(verificationCode).append("</div>");
        html.append("<p>이 코드는 5분 동안 유효합니다.</p>");
        html.append("</div>");
        html.append("<div class='footer'>");
        html.append("<p>이 메일은 LMS System에서 자동으로 발송되었습니다.</p>");
        html.append("</div>");
        html.append("</div>");
        html.append("</body>");
        html.append("</html>");

        return html.toString();
    }
}
