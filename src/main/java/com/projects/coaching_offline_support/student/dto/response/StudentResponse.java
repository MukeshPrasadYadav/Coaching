package com.projects.coaching_offline_support.student.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record StudentResponse(
        UUID id,
        String name,
        String class_std,
        String batch,
        LocalDateTime joiningDate
) {
}
