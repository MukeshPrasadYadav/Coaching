package com.projects.coaching_offline_support.teacher.dto.request;

import com.projects.coaching_offline_support.common.entity.Timing;
import com.projects.coaching_offline_support.common.enums.DaysOfWeek;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record AddTeacherRequest(
        @NotNull(message = "Name is required.")
        String name,
        @NotNull(message = "Provide subject for teacher")
        List<String> subjects,
        UUID coachingId,
        @NotNull(message = "Provide coaching id")
        String contactInfo,
        @NotNull(message = "Provide fees for teacher")
        BigDecimal fee,
        @NotNull(message = "Provide degree for teacher")
        List<String> degrees,
        Map<DaysOfWeek, Timing> availability // configure teacher timings availibilty
) {
}
