package com.projects.coaching_offline_support.notification.service;

import com.projects.coaching_offline_support.audit.entity.AuditMutationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {

    // swap this for your real email/push/SMS sender later
    // private final EmailSender emailSender;

    private static final Map<String, String> TEMPLATES = Map.of(
            "TEACHER:ASSIGN", "A teacher has been assigned to your batch.",
            "TEACHER:REMOVE", "A teacher has been removed from your batch.",
            "BATCH:CREATE",   "A new batch has been created for you.",
            "BATCH:UPDATE",   "Your batch details were updated."
    );

    public void notify(AuditMutationEvent event) {
        if (event.getTargetUserId() == null) return; // nothing to notify

        String key = event.getLogType() + ":" + event.getActionType();
        String message = TEMPLATES.getOrDefault(key, "An update was made to your account.");

        send(event.getTargetUserId(), message);
    }

    private void send(java.util.UUID userId, String message) {
        // placeholder — replace with real email/push/SMS integration
        System.out.println("Notify user " + userId + ": " + message);
    }
}