package com.mzc.lms.notification.application.port.out;

import com.mzc.lms.notification.domain.model.Notification;

public interface NotificationSenderPort {

    void sendPush(Notification notification, String deviceToken);

    void sendEmail(Notification notification, String email);

    void sendSms(Notification notification, String phoneNumber);
}
