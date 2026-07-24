package com.projects.coaching_offline_support.notification;

import com.projects.coaching_offline_support.notification.enums.NotificationMode;
import com.projects.coaching_offline_support.notification.enums.NotificationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailNotificationStrategy implements NotificationStrategy {

    private final JavaMailSender mailSender;

    @Override
    public NotificationMode getMode() {
        return NotificationMode.EMAIL;
    }



    @Override
    public void send() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("mukeshprasadyadav2002@gmail.com");
        message.setTo("my699546@gmail.com");
        message.setSubject("testing");
        message.setText("Hello from brevo testing");
        mailSender.send(message);
        System.out.println("Sending notification with email"); // todo add email provider here
    }
}
