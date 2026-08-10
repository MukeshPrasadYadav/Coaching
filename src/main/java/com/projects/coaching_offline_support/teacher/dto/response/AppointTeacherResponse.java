package com.projects.coaching_offline_support.teacher.dto.response;

import com.projects.coaching_offline_support.teacher.entity.Teacher;

import java.util.Set;
import java.util.UUID;

public record AppointTeacherResponse(
        UUID id,
        String name,
        Integer experience,
        Set<String> degrees,
        Set<String> subjects,
        String profilePicture
) {

    public  static  AppointTeacherResponse fromEntity(Teacher teacher,String profilePicture){

        return new AppointTeacherResponse(
                teacher.getUser().getId(),
                teacher.getUser().getName(),
                teacher.getExperience(),
                teacher.getDegrees(),
                teacher.getSubjects(),
                profilePicture
        );
    }
}
