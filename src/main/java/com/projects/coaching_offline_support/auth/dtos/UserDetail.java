package com.projects.coaching_offline_support.auth.dtos;

import com.projects.coaching_offline_support.common.entity.Address;
import com.projects.coaching_offline_support.common.enums.Permission;
import com.projects.coaching_offline_support.common.enums.Role;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public record UserDetail(
        UUID id,
        String name,
        String email,
        String contactNumber,
        Role role,
        List<UUID>coachingIds,
        List<UUID> batchIds,
        boolean isProfileCompleted
) {
}
