package com.projects.coaching_offline_support.student.dto.request;

import java.time.LocalDateTime;

public record StudentFilter(
        String name,
        String batch,
        String class_Std,
        LocalDateTime toDate,
        LocalDateTime fromDate,
        String search
) {
}
