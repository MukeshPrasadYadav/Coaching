package com.projects.coaching_offline_support.audit.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.projects.coaching_offline_support.audit.entity.AuditLog;
import com.projects.coaching_offline_support.audit.entity.AuditMutationEvent;
import com.projects.coaching_offline_support.audit.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService{

    private final AuditLogRepository auditLogRepository;
    private final ObjectMapper objectMapper;

    public void save(AuditMutationEvent event) {
        AuditLog log = new AuditLog();
        log.setActor(event.getActor());
        log.setLogType(event.getLogType());
        log.setActionType(event.getActionType());
        log.setDescription(event.getDescription());
        log.setOldValue(toJson(event.getBefore()));
        log.setNewValue(toJson(event.getAfter()));
        // log.setEntityId(event.getEntityId()); // once you add this field

        auditLogRepository.save(log);
    }

    private String toJson(Object obj) {
        if (obj == null) return null;
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            return "{}"; // don't let a serialization failure break the audit write
        }
    }
}
