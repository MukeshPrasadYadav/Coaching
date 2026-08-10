package com.projects.coaching_offline_support.audit.entity;


import com.graphbuilder.curve.NURBSpline;
import com.projects.coaching_offline_support.audit.enums.ActionType;
import com.projects.coaching_offline_support.audit.enums.LogType;
import com.projects.coaching_offline_support.auth.dtos.SignupResponse;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "audit_info", indexes = {
        @Index(name = "idx_audit_actor", columnList = "actor"),
        @Index(name = "idx_audit_entity", columnList = "logType")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class    AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID actor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LogType logType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActionType actionType;

    private String description;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name ="old_Value", columnDefinition = "jsonb")
    private String oldValue;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "new_vale",columnDefinition = "jsonb")
    private String newValue;

    @Builder.Default
    @Column(nullable = false,updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();


}
