package com.projects.coaching_offline_support.teacher.dto.request;

import com.projects.coaching_offline_support.teacher.enums.Experience;

public record AppointTeacherFilter(
        String search,
        Experience experience,
        String degree,
        String subject
) {
}
