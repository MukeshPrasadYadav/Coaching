package com.projects.coaching_offline_support.audit.listener;

import com.projects.coaching_offline_support.audit.entity.AuditMutationEvent;
import com.projects.coaching_offline_support.audit.service.AuditLogService;
import com.projects.coaching_offline_support.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class AuditEventListener {

    private final AuditLogService auditLogService;
    private final NotificationService notificationService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onMutation(AuditMutationEvent event) {
        auditLogService.save(event); // synchronous, reliable — no @Async here
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onMutationNotify(AuditMutationEvent event) {
        notificationService.notify(event); // async — slow sender won't block anything
    }
}