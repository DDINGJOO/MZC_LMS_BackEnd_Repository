package com.mzc.lms.notification.config;

import com.google.firebase.messaging.FirebaseMessaging;
import com.mzc.lms.notification.adapter.out.notification.EmailNotificationAdapter;
import com.mzc.lms.notification.adapter.out.notification.FirebasePushNotificationAdapter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
public class NotificationConfig {

    @Bean
    @ConditionalOnBean(FirebaseMessaging.class)
    public FirebasePushNotificationAdapter firebasePushNotificationAdapter(FirebaseMessaging firebaseMessaging) {
        return new FirebasePushNotificationAdapter(firebaseMessaging);
    }

    @Bean
    public EmailNotificationAdapter emailNotificationAdapter(JavaMailSender mailSender) {
        return new EmailNotificationAdapter(mailSender);
    }
}
