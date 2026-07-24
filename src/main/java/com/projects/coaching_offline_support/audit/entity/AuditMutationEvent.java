package com.projects.coaching_offline_support.audit.entity;

import com.projects.coaching_offline_support.audit.enums.ActionType;
import com.projects.coaching_offline_support.audit.enums.LogType;

import java.util.UUID;

public class AuditMutationEvent {

    private final UUID actor;
    private final UUID targetUserId; // nullable
    private final LogType logType;
    private final ActionType actionType;
    private final UUID entityId;
    private final String description;
    private final Object before; // raw object, listener serializes to JSON
    private final Object after;

    public AuditMutationEvent(UUID actor, UUID targetUserId, LogType logType, ActionType actionType,
                              UUID entityId, String description, Object before, Object after) {
        this.actor = actor;
        this.targetUserId = targetUserId;
        this.logType = logType;
        this.actionType = actionType;
        this.entityId = entityId;
        this.description = description;
        this.before = before;
        this.after = after;
    }

    public UUID getActor() { return actor; }
    public UUID getTargetUserId() { return targetUserId; }
    public LogType getLogType() { return logType; }
    public ActionType getActionType() { return actionType; }
    public UUID getEntityId() { return entityId; }
    public String getDescription() { return description; }
    public Object getBefore() { return before; }
    public Object getAfter() { return after; }
}