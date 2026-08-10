package com.projects.coaching_offline_support.batch.dto.response;

import com.projects.coaching_offline_support.batch.entity.Batch;
import com.projects.coaching_offline_support.batch.entity.BatchSchedule;
import com.projects.coaching_offline_support.common.entity.Timing;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record BatchDetail(
        UUID id,
        String name,
        String coachingName,
        String teachers,
        String subjects,
        String roomNo,
        Integer totalStudent,
        LocalDate startDate,
        LocalDate endDate,
        Set<String> scheduled,
        String timing,
        BigDecimal fees
) {

    public static BatchDetail fromEntity(Batch batch){
        String teachers = batch.getSchedules().stream()
                .map(batchSchedule -> batchSchedule.getTeacher().getUser().getName())
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.joining(", "));

        String subjects = batch.getSubjects().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.joining(", "));
        Timing timing = batch.getSchedules().getFirst().getTiming();
        String timingString = String.join("-",timing.getStartTime().toString(),timing.getEndTime().toString());
        Set<String> daysOfClass = batch.getSchedules().stream()
                .map(BatchSchedule::getDay)
                .filter(Objects::nonNull)
                .map(Enum::name)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return new BatchDetail(
                batch.getId(),
                batch.getName(),
                batch.getCoaching().getCoachingName(),
                teachers,
                subjects,
                batch.getClassRoom(),
                batch.getTotalStudents(),
                batch.getStartDate(),
                batch.getEndDate(),
                daysOfClass,
                timingString,
                batch.getFee()
        );
    }
}
