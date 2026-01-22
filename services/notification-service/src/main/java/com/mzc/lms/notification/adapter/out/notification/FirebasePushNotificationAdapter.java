package com.mzc.lms.notification.adapter.out.notification;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.mzc.lms.notification.application.port.out.NotificationSenderPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class FirebasePushNotificationAdapter {

    private final FirebaseMessaging firebaseMessaging;

    public void sendPush(com.mzc.lms.notification.domain.model.Notification notification, String deviceToken) {
        if (firebaseMessaging == null) {
            log.warn("Firebase messaging is not configured. Skipping push notification.");
            return;
        }

        try {
            Notification fcmNotification = Notification.builder()
                    .setTitle(notification.getTitle())
                    .setBody(notification.getContent())
                    .build();

            Message.Builder messageBuilder = Message.builder()
                    .setToken(deviceToken)
                    .setNotification(fcmNotification);

            // Add custom data if available
            if (notification.getData() != null && !notification.getData().isEmpty()) {
                messageBuilder.putAllData(convertToStringMap(notification.getData()));
            }

            String response = firebaseMessaging.send(messageBuilder.build());
            log.info("Successfully sent push notification: {}", response);
        } catch (Exception e) {
            log.error("Failed to send push notification to device token: {}", deviceToken, e);
            throw new RuntimeException("Failed to send push notification", e);
        }
    }


    private Map<String, String> convertToStringMap(Map<String, Object> data) {
        return data.entrySet().stream()
                .collect(java.util.stream.Collectors.toMap(
                        Map.Entry::getKey,
                        e -> String.valueOf(e.getValue())
                ));
    }
}
