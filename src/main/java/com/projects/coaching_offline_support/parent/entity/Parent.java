package com.projects.coaching_offline_support.parent.entity;


import com.projects.coaching_offline_support.common.entity.BaseEntity;
import com.projects.coaching_offline_support.student.entity.Student;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "parent_info")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Parent extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToMany
    @JoinColumn(name = "parent_id")
    private List<Student> student;

    @Column(nullable = false,length = 10)
    private String contactInfo;

    @Column(nullable = false,length = 20)
    private String name;
}
