package com.projects.coaching_offline_support.audit.service;

import com.projects.coaching_offline_support.audit.entity.AuditMutationEvent;

public interface AuditLogService {
    void save(AuditMutationEvent event);
}
