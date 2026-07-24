package com.projects.coaching_offline_support.audit.entity;

import com.projects.coaching_offline_support.audit.enums.ActionType;
import com.projects.coaching_offline_support.audit.enums.LogType;
import com.projects.coaching_offline_support.notification.enums.NotificationMode;
import com.projects.coaching_offline_support.notification.enums.NotificationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Auditable {

    LogType logType();

    ActionType actionType();


    String description() default "";

    String oldValueExpression() default "";

    String newValueExpression() default "";

    NotificationType notificationType() default NotificationType.NONE;
    NotificationMode notificationMode() default NotificationMode.NONE;
}