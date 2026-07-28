package com.projects.coaching_offline_support.student.dto.request;

import com.projects.coaching_offline_support.common.entity.Address;
import com.projects.coaching_offline_support.common.enums.Gender;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AddStudent(
        @NotNull(message = "Student name is required")
        String  name,
        String contactNumber,
        @NotNull(message = "Email is required.")
        String email,
        UUID batch
) {
}
