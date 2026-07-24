package com.projects.coaching_offline_support.teacher.dto.response;


import com.projects.coaching_offline_support.common.entity.Timing;
import com.projects.coaching_offline_support.common.enums.DaysOfWeek;
import com.projects.coaching_offline_support.teacher.entity.Teacher;

import java.util.List;
import java.util.Map;
import java.util.Set;


public record TeacherCoachingResponse(
        String name,
        String contactNumber,
        Set<String> degreess,
        Set<String> subjects
//        List<Map<DaysOfWeek,Timing>> availability

) {

    public static TeacherCoachingResponse fromEntity(Teacher teacher){


//        List<Map<DaysOfWeek,Timing>> availability = teacher.getAvailability();
        return new TeacherCoachingResponse(
                teacher.getUser().getName(),
                teacher.getUser().getContactNumber(),
                teacher.getDegrees(),
                teacher.getSubjects()
        );
    }
}
