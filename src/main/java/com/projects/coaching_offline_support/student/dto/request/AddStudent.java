package com.projects.coaching_offline_support.student.dto.request;

import com.projects.coaching_offline_support.common.entity.Address;
import jakarta.validation.constraints.NotNull;

public record AddStudent(
        @NotNull(message = "Student name is required")
        String  name,
        String contactNumber,
        @NotNull(message = "Email is required.")
        String email,
        @NotNull(message = "Class is required")
        String class_std,
        String batch,
        String parentName,
        String parentNumber,
        String parentEmail,
        Address address
) {
}
