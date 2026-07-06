package com.projects.coaching_offline_support.common.entity;

import com.projects.coaching_offline_support.common.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

    @Column(nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;
}
