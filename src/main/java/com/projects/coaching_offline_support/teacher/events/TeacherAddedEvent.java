package com.projects.coaching_offline_support.teacher.events;

import com.projects.coaching_offline_support.Coaching.entity.Coaching;
import com.projects.coaching_offline_support.teacher.entity.CoachingTeacher;
import com.projects.coaching_offline_support.teacher.entity.Teacher;

import java.util.UUID;

public record TeacherAddedEvent(
        UUID teacherId,
        Coaching coaching
) {
    public static TeacherAddedEvent fromEntity(CoachingTeacher coachingTeacher){
        return new TeacherAddedEvent(
                coachingTeacher.getTeacher().getId(),
                coachingTeacher.getCoaching()
        );
    }
}
