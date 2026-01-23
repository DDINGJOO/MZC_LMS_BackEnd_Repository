package com.mzc.lms.notification.adapter.out.notification;

import com.mzc.lms.notification.application.port.out.NotificationSenderPort;
import com.mzc.lms.notification.domain.model.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CompositeNotificationSender implements NotificationSenderPort {

    private final FirebasePushNotificationAdapter firebaseAdapter;
    private final EmailNotificationAdapter emailAdapter;

    @Autowired
    public CompositeNotificationSender(
            @Autowired(required = false) FirebasePushNotificationAdapter firebaseAdapter,
            EmailNotificationAdapter emailAdapter) {
        this.firebaseAdapter = firebaseAdapter;
        this.emailAdapter = emailAdapter;
    }

    @Override
    public void sendPush(Notification notification, String deviceToken) {
        if (firebaseAdapter == null) {
            log.warn("Firebase push notification adapter is not configured. Skipping push notification.");
            return;
        }
        firebaseAdapter.sendPush(notification, deviceToken);
    }

    @Override
    public void sendEmail(Notification notification, String email) {
        emailAdapter.sendEmail(notification, email);
    }

    @Override
    public void sendSms(Notification notification, String phoneNumber) {
        log.warn("SMS sending is not implemented yet");
        // TODO: Implement SMS sending with a service like Twilio
    }
}
