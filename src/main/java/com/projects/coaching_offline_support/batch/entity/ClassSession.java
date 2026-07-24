package com.projects.coaching_offline_support.batch.entity;


import com.projects.coaching_offline_support.batch.enums.ClassSessionStatus;
import com.projects.coaching_offline_support.common.entity.Timing;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"batch_schedule_id","session_date"}))
public class ClassSession {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne @JoinColumn(name = "batch_schedule_id")
    private BatchSchedule batchSchedule;

    private LocalDate sessionDate;

    @Embedded
    private Timing timing;

    @Enumerated(EnumType.STRING)
    private ClassSessionStatus status;

    private String cancelReason;

    private LocalDateTime cancelledAt;
}
