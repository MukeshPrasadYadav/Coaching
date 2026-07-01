package com.projects.coaching_offline_support.student.entity;

import com.projects.coaching_offline_support.Coaching.entity.Coaching;
import com.projects.coaching_offline_support.parent.entity.Parent;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "student_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false,length = 20)
    private String name;

    @ManyToMany
    @JoinTable(
            name = "student_coaching",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "coaching_id")
    )
    private List<Coaching> coaching;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Parent parent;


    @Column(nullable = false)
    private String standard;

    @Column(nullable = false)
    private String contactInfo;

}
