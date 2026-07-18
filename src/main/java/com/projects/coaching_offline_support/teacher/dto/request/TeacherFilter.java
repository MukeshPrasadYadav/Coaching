package com.projects.coaching_offline_support.teacher.dto.request;

import java.time.LocalDateTime;

public record TeacherFilter(
        String name,
        String batch,
        String subject,
        String degree,
        LocalDateTime toDate,
        LocalDateTime fromDate,
        String search
) {
}
