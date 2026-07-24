package com.projects.coaching_offline_support.teacher.entity;

import com.projects.coaching_offline_support.Coaching.entity.Coaching;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"teacher_id", "coaching_id"}))

public class CoachingTeacher {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne @JoinColumn(name = "coaching_id")
    private Coaching coaching;

    @Builder.Default
    private LocalDate joinedOn = LocalDate.now();
    @Builder.Default
    private boolean active = true;
}
