package com.projects.coaching_offline_support.student.dto.request;

import com.projects.coaching_offline_support.common.entity.Address;

public record UpdateStudentDetailRequest(
        String contactNumber,
        String parentNumber,
        String profilePic,
        String parentEmail,
        Address address

) {
}
