package com.projects.coaching_offline_support.auth.dtos;

import com.projects.coaching_offline_support.common.enums.Permission;
import com.projects.coaching_offline_support.common.enums.Role;

import java.util.Set;
import java.util.UUID;

public record UserDetail(
        UUID id,
        String name,
        String email,
        String contactInfo,
        Set<Role> roles,
        Set<Permission> permissions
) {
}
