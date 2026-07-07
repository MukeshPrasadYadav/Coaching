package com.projects.coaching_offline_support.auth.dtos;

import com.projects.coaching_offline_support.common.enums.Permission;
import com.projects.coaching_offline_support.common.enums.Role;

import java.util.Set;

public record SignupRequest(
        String name,
        String email,
        String password,
        Role role
) {
}
