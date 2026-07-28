package com.projects.coaching_offline_support.student.entity;

import com.projects.coaching_offline_support.Coaching.entity.Coaching;
import com.projects.coaching_offline_support.batch.entity.Batch;
import com.projects.coaching_offline_support.common.entity.Address;
import com.projects.coaching_offline_support.common.entity.BaseEntity;
import com.projects.coaching_offline_support.parent.entity.Parent;
import com.projects.coaching_offline_support.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.net.StandardSocketOptions;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "student_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student extends BaseEntity {

    @Id
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false,cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @MapsId
    private User user;


//    @ManyToOne
//    @JoinColumn(name = "parent_id")
//    private Parent parent;

    private String  fatherName;

    private String motherName;


    private String parentName;

    private String parentNumber;

    private String parentEmail;

    @ManyToMany
    @JoinTable(
            name = "student_batch",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "batch_id")
    )
    @Builder.Default
    private List<Batch> batches = new ArrayList<>();

}
