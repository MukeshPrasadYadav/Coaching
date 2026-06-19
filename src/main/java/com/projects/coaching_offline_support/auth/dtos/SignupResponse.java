package com.projects.coaching_offline_support.auth.dtos;

import java.util.UUID;

public record SignupResponse(
        UUID userId,
        String name,
        String role
) {
}
