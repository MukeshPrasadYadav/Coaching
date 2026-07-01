package com.projects.coaching_offline_support.Coaching.entity;


import com.projects.coaching_offline_support.batch.entity.Batch;
import com.projects.coaching_offline_support.common.entity.BaseEntity;
import com.projects.coaching_offline_support.student.entity.Student;
import com.projects.coaching_offline_support.teacher.entity.Teacher;
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
    private String name;

    @Column(nullable = false,length = 20)
    private String ownerName;

    @Column(nullable = false,length = 100)
    private String address;

    @Column(nullable = false,length = 10)
    private String ownerContactNumber;

    @Column(nullable = false)
    private String ownerEmail;

    @ManyToMany(mappedBy = "coaching")
    private List<Student> students;

    @OneToMany(mappedBy = "coaching")
    private List<Batch> batches;

    @ManyToMany(mappedBy = "coaching")
    private List<Teacher> teachers;
}
