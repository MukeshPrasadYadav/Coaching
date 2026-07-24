package com.projects.coaching_offline_support.audit.repository;

import com.projects.coaching_offline_support.audit.entity.AuditLog;
import com.projects.coaching_offline_support.audit.enums.ActionType;
import com.projects.coaching_offline_support.audit.enums.LogType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {

    List<AuditLog> findByLogTypeAndActionType(LogType type , ActionType action);
}
