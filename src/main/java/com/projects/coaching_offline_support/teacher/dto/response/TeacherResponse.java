package com.projects.coaching_offline_support.teacher.dto.response;

import com.projects.coaching_offline_support.common.entity.Address;
import com.projects.coaching_offline_support.teacher.entity.Teacher;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public record TeacherResponse(
        UUID id,
        String name,
        Integer experience,
        Set<String> batches,
        Set<String> degrees,
        Set<String> subjects,
        BigDecimal fee) {

    public static TeacherResponse fromEntity(Teacher teacher){
        return new TeacherResponse(
                teacher.getId(),
                teacher.getUser().getName(),
                teacher.getExperience(),
                teacher.getBatches(),
                teacher.getDegrees(),
                teacher.getSubjects(),
                teacher.getFee()
        );
    }
}
