package com.projects.coaching_offline_support.notification;

import com.projects.coaching_offline_support.notification.enums.NotificationMode;
import com.projects.coaching_offline_support.notification.enums.NotificationType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class InAppNotificationStrategy implements NotificationStrategy {
    @Override
    public NotificationMode getMode() {
        return NotificationMode.IN_APP;
    }

    @Override
    public void send() {
        log.info("Sending notification via in App notification service");
        System.out.println("Sending notification via in App notification service");
    }
}
