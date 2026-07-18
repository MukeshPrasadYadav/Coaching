package com.projects.coaching_offline_support.teacher.dto.request;

import com.projects.coaching_offline_support.common.entity.Address;
import com.projects.coaching_offline_support.common.entity.Timing;
import com.projects.coaching_offline_support.common.enums.DaysOfWeek;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record RegisterTeacherRequest(
        @NotNull(message = "Name is required.")
        String name,
        @NotNull(message = "Provide subject for teacher")
        HashSet<String> subjects,
        UUID coachingId,
        @NotNull(message = "Provide Contact number")
        String contactNumber,
        @NotNull(message = "Provide fees for teacher")
        BigDecimal fee,
        @NotNull(message = "Provide degree for teacher")
        HashSet<String> degrees,

        @NotNull(message = "Enter email.")
        String email,

        @NotNull(message = "provide address.")
        Address address
) {
}
