package com.projects.coaching_offline_support.auth.dtos.UserDetailsRepsonse;

import com.projects.coaching_offline_support.teacher.entity.Teacher;

import java.util.Set;

public record TeacherUserDetails(
        UserInfo user,
        Set<String> degress,
        Integer experience,
        Set<String> subjects
) implements UserDetail {

    public static TeacherUserDetails from (Teacher teacher,String profilePicture){

        return new TeacherUserDetails(
                UserInfo.from(teacher.getUser(),profilePicture),
                teacher.getDegrees(),
                teacher.getExperience(),
                teacher.getSubjects()
        );
    }
}
