package com.projects.coaching_offline_support.Coaching.dto;

import com.projects.coaching_offline_support.common.entity.Address;

import java.util.UUID;

public record AddCoachingResponse(
        UUID id,
        String name,
        String ownerName,
        Address address,
        String ownerContactNumber,
        String ownerEmail
) {
}
