package com.projects.coaching_offline_support.teacher.entity;

import com.projects.coaching_offline_support.Coaching.entity.Coaching;
import com.projects.coaching_offline_support.common.entity.BaseEntity;
import com.projects.coaching_offline_support.common.entity.Timing;
import com.projects.coaching_offline_support.common.enums.DaysOfWeek;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Entity
@Table(name = "teacher_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Teacher extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false,length = 20)
    private String name;

    @Column(nullable = false)
    private List<String> subjects;

    @ManyToMany
    @JoinTable(
            name = "teacher_coaching",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "coaching_id")
    )
    private List<Coaching> coaching;

    @Column(nullable = false,length = 10)
    private String contactInfo;

    @Column(nullable = false)
    private BigDecimal fee;

    @Column(nullable = false)
    private List<String> degrees;

    public  void addCoaching(Coaching coaching){
        this.coaching.add(coaching);
        coaching.getTeachers().add(this);
    }

    public void removeCoaching(Coaching coaching){
        this.coaching.remove(coaching);
        coaching.getTeachers().remove(this);
    }

}
