package com.projects.coaching_offline_support.notification;

import com.projects.coaching_offline_support.notification.enums.NotificationMode;
import com.projects.coaching_offline_support.notification.enums.NotificationType;

public interface NotificationStrategy {
    public NotificationMode getMode();
    public void send( );


}
