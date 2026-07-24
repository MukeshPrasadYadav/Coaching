package com.projects.coaching_offline_support.notification;

import com.projects.coaching_offline_support.notification.enums.NotificationMode;
import com.projects.coaching_offline_support.notification.enums.NotificationType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SmsNotificationStrategy implements NotificationStrategy {
    @Override
    public NotificationMode getMode() {
        return NotificationMode.SMS;
    }

    @Override
    public void send() {
        log.info("Sending notification via sms");
        System.out.println("Sending notification via sms");
    }
}
