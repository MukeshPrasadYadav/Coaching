package com.projects.coaching_offline_support.Coaching.entity;


import com.projects.coaching_offline_support.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

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
}
