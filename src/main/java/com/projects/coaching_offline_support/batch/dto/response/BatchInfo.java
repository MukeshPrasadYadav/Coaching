package com.projects.coaching_offline_support.batch.dto.response;

import com.projects.coaching_offline_support.batch.entity.Batch;
import com.projects.coaching_offline_support.batch.entity.BatchSchedule;
import com.projects.coaching_offline_support.batch.enums.BatchStatus;
import com.projects.coaching_offline_support.teacher.repository.TeacherRepository;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record BatchInfo(
        UUID id,

     //   String teacherName,
//Map<DaysOfWeek,Timing> timing,
        String coachingName,
        BatchStatus status,
        Integer totalStudents,
        String name,
        List<String> teachers,
        LocalTime startTime,
        LocalTime endTime,
        List<String> subjects  // this will be later in schedule event
) {
    public static BatchInfo fromEntity(Batch batch){
        java.util.List<BatchSchedule> batchSchedules = batch.getSchedules();
        return new BatchInfo(
                batch.getId(),
                batch.getCoaching().getCoachingName(),
                batch.getStatus(),
                batch.getStudents().size(),
                batch.getName(),
                batchSchedules.stream().map( batchSchedule -> batchSchedule.getTeacher().getUser().getName()).toList(),
                batchSchedules.getFirst().getTiming().getStartTime(),
                batchSchedules.getFirst().getTiming().getEndTime(),
                batch.getSubjects()

        );
    }
}
