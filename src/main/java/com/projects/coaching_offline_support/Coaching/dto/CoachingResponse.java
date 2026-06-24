package com.projects.coaching_offline_support.Coaching.dto;

import java.util.UUID;

public record CoachingResponse(
        UUID id,
        String name,
        String ownerName,
        String address,
        String ownerContactNumber,
        String ownerEmail
) {
}
