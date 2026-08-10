package com.projects.coaching_offline_support.auth.dtos.UserDetailsRepsonse;

import com.projects.coaching_offline_support.student.dto.response.StudentDetail;
import com.projects.coaching_offline_support.student.entity.Student;

public record StudentUserDetails(
        UserInfo user,
        String motherName,
        String fatherName,
        String guardianName,
        String guardianPhone,
        String guardianEmail
) implements UserDetail {

    public static StudentUserDetails from(Student student, String profilePic){

        return new StudentUserDetails(
                UserInfo.from(student.getUser(),profilePic),
                student.getMotherName(),
                student.getFatherName(),
                student.getParentName(),
                student.getParentNumber(),
                student.getParentEmail()
        );
    }
}
