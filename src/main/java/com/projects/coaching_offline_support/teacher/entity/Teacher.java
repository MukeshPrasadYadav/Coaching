package com.projects.coaching_offline_support.teacher.entity;

import com.projects.coaching_offline_support.Coaching.entity.Coaching;
import com.projects.coaching_offline_support.common.entity.Address;
import com.projects.coaching_offline_support.common.entity.BaseEntity;
import com.projects.coaching_offline_support.common.entity.Timing;
import com.projects.coaching_offline_support.common.enums.DaysOfWeek;
import com.projects.coaching_offline_support.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;


@Entity
@Table(name = "teacher_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Teacher extends BaseEntity {

    @Id
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false,cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @MapsId
    private User user;

    @ElementCollection
    @CollectionTable(
            name = "teacher_subjects",
            joinColumns = @JoinColumn(name = "teacher_id")
    )
    @Column(name = "subject")
    @Builder.Default
    private List<String> subjects = new ArrayList<>();


    @ElementCollection
    @CollectionTable(
            name = "teacher_degrees",
            joinColumns = @JoinColumn(name = "teacher_id")
    )
    @Column(name = "degree")
    @Builder.Default
    private List<String> degrees = new ArrayList<>();


    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal fee;

    @ManyToMany(mappedBy = "teachers")
    @Builder.Default
    private List<Coaching> coachings = new ArrayList<>();




    public void addCoaching(Coaching coaching) {
        coachings.add(coaching);
        coaching.getTeachers().add(this);
    }

    public void removeCoaching(Coaching coaching) {
        coachings.remove(coaching);
        coaching.getTeachers().remove(this);
    }
}
