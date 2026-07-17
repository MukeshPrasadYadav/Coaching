package com.projects.coaching_offline_support.Coaching.entity;


import com.projects.coaching_offline_support.Coaching.enums.CoachingStatus;
import com.projects.coaching_offline_support.batch.entity.Batch;
import com.projects.coaching_offline_support.common.entity.Address;
import com.projects.coaching_offline_support.common.entity.BaseEntity;
import com.projects.coaching_offline_support.student.entity.Student;
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
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false,length = 50)
    private String coachingName;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private CoachingStatus status = CoachingStatus.OPEN;

    @OneToOne(fetch = FetchType.LAZY, optional = false,cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;


    @ManyToMany(mappedBy = "coaching",fetch = FetchType.LAZY)
    private List<Student> students;

    @OneToMany(mappedBy = "coaching",fetch = FetchType.LAZY)
    private List<Batch> batches;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name ="coaching_teacher",
            joinColumns = @JoinColumn(name = "coaching_id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_id")
    )
    private List<Teacher> teachers;

    private String reasonForRemoving;
}
