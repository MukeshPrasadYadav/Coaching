package com.projects.coaching_offline_support.user.dto.response;

public record UserSpecificDetail(
        String name,
        String contactNumber,
        String email,
        String parentName,
        String parentNumber,
        String parentEmail
) {
}
