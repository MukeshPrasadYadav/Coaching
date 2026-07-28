package com.projects.coaching_offline_support.batch.entity;

import com.projects.coaching_offline_support.common.entity.Timing;
import com.projects.coaching_offline_support.common.enums.DaysOfWeek;
import com.projects.coaching_offline_support.teacher.entity.Teacher;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "coaching_batch")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BatchSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "batch_id")
    private Batch batch;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @Enumerated(EnumType.STRING)
    private DaysOfWeek day;

    @Embedded
    private Timing timing;


}
