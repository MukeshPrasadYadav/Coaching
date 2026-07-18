package com.projects.coaching_offline_support.teacher.dto.response;


import java.util.Set;


public record TeacherCoachingResponse(
        String name,
        String contactNumber,
        Set<String> degreess,
        Set<String> subjects,
        Set<String> batches

) {
}
