package com.projects.coaching_offline_support.batch.entity;


import com.projects.coaching_offline_support.Coaching.entity.Coaching;
import com.projects.coaching_offline_support.batch.enums.BatchStatus;
import com.projects.coaching_offline_support.common.entity.BaseEntity;
import com.projects.coaching_offline_support.common.entity.Timing;
import com.projects.coaching_offline_support.common.enums.DaysOfWeek;
import com.projects.coaching_offline_support.teacher.entity.Teacher;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;
import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "batch_info")
public class Batch extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false,length = 20)
    private String name;

    @ManyToOne
    @JoinColumn(name = "coaching_id")
    private Coaching coaching;

   @OneToOne
    private Teacher teacher;

   @Builder.Default
    private Integer totalStudents = 0;

   @ElementCollection
   @CollectionTable(name = "batch_timings")
   @MapKeyEnumerated(EnumType.STRING)
   private Map<DaysOfWeek, Timing> timings = new EnumMap<>(DaysOfWeek.class);

   @Column(nullable = false)
    private BigInteger fees;

   @Column(nullable = false)
   @Builder.Default
    private BatchStatus status = BatchStatus.TO_BE_LAUNCHED;
}
