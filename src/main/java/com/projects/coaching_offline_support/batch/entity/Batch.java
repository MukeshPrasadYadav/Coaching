package com.projects.coaching_offline_support.batch.entity;


import com.projects.coaching_offline_support.Coaching.entity.Coaching;
import com.projects.coaching_offline_support.Coaching.enums.ReasonToRemoveCoaching;
import com.projects.coaching_offline_support.batch.enums.BatchStatus;
import com.projects.coaching_offline_support.common.entity.BaseEntity;
import com.projects.coaching_offline_support.common.entity.Timing;
import com.projects.coaching_offline_support.common.enums.DaysOfWeek;
import com.projects.coaching_offline_support.student.entity.Student;
import com.projects.coaching_offline_support.teacher.entity.Teacher;
import jakarta.persistence.*;
import lombok.*;
import org.w3c.dom.stylesheets.LinkStyle;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.*;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coaching_id")
    private Coaching coaching;

   @Builder.Default
    private Integer totalStudents = 0;

   private Integer totalCapacity ;

    @OneToMany(
            mappedBy = "batch",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<BatchSchedule> schedules = new ArrayList<>();

   @Column(nullable = false)
    private BigDecimal fee;

   @Column(nullable = false)
   @Enumerated(EnumType.STRING)
   @Builder.Default
    private BatchStatus status = BatchStatus.TO_BE_LAUNCHED;

   @Column(nullable = false)
   private List<String> subjects;  // this will be later in batachSchedule
   
    private  String classRoom;

    @ManyToMany(mappedBy = "batches")
    private List<Student> students = new ArrayList<>();


    private LocalDate startDate;

    private LocalDate endDate;

    private String reasonToClose;

    @Builder.Default
    private boolean active = true;
}
