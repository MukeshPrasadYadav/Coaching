package com.projects.coaching_offline_support.student.dto.request;

import com.projects.coaching_offline_support.common.entity.Address;
import com.projects.coaching_offline_support.common.enums.Gender;

import java.time.LocalDate;

public record CompleteStudentProfileRequest(
        String email,
        String name,
        String password,
        Address address,
        String contactNumber,
        LocalDate dob,
        String fatherName,
        String motherName,
        String parentName,
        String parentPhone,
        String parentEmail,
        String profilePic,
        Gender gender

) {
}
