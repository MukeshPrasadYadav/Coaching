package com.projects.coaching_offline_support.Coaching.entity;


import com.projects.coaching_offline_support.Coaching.enums.CoachingStatus;
import com.projects.coaching_offline_support.batch.entity.Batch;
import com.projects.coaching_offline_support.common.entity.Address;
import com.projects.coaching_offline_support.common.entity.BaseEntity;
import com.projects.coaching_offline_support.student.entity.Student;
import com.projects.coaching_offline_support.teacher.entity.CoachingTeacher;
import com.projects.coaching_offline_support.teacher.entity.Teacher;
import com.projects.coaching_offline_support.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "coaching_info")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Coaching extends BaseEntity {
    @Id
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false,cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @MapsId

    private User user;
    @Column(nullable = false,length = 50)
    private String coachingName;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private CoachingStatus status = CoachingStatus.OPEN;



    @OneToMany(mappedBy = "coaching",fetch = FetchType.LAZY)
    private List<Batch> batches;

    @OneToMany(mappedBy = "coaching", fetch = FetchType.LAZY)
    private List<CoachingTeacher> teacherLinks;

    private String reasonForRemoving;
}
