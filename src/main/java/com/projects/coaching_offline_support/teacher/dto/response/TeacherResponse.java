package com.projects.coaching_offline_support.teacher.dto.response;

import com.projects.coaching_offline_support.batch.entity.BatchSchedule;
import com.projects.coaching_offline_support.common.entity.Address;
import com.projects.coaching_offline_support.student.dto.response.StudentBatchResponse;
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
        Set<String> degrees,
        Set<String> subjects,
        BigDecimal fee,
        List<StudentBatchResponse> batches
        ) {

    public static TeacherResponse fromEntity(Teacher teacher){
        List<StudentBatchResponse> batches = teacher.getSchedules()
                .stream()
                .map(BatchSchedule::getBatch)
                .distinct()
                .map(StudentBatchResponse::fromEntity)
                .toList();
        return new TeacherResponse(
                teacher.getId(),
                teacher.getUser().getName(),
                teacher.getExperience(),
                teacher.getDegrees(),
                teacher.getSubjects(),
                teacher.getFee(),
                batches
        );
    }
}
