package com.mzc.lms.notification.adapter.out.notification;

import com.mzc.lms.notification.application.port.out.NotificationSenderPort;
import com.mzc.lms.notification.domain.model.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
public class EmailNotificationAdapter {

    private final JavaMailSender mailSender;

    public void sendEmail(Notification notification, String email) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject(notification.getTitle());
            message.setText(notification.getContent());

            mailSender.send(message);
            log.info("Successfully sent email notification to: {}", email);
        } catch (Exception e) {
            log.error("Failed to send email notification to: {}", email, e);
            throw new RuntimeException("Failed to send email notification", e);
        }
    }

}
