package com.projects.coaching_offline_support.batch.dto.request;

import com.projects.coaching_offline_support.common.entity.Timing;
import com.projects.coaching_offline_support.common.enums.DaysOfWeek;
import com.projects.coaching_offline_support.teacher.entity.Teacher;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import javax.security.auth.Subject;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record AddBatchRequest(



        Integer studentCapacity,
        @NotBlank(message = "Batch name is required")
        String name,
        @NotNull(message = "Provide subjects for the batch")
        List<String> subjects,
        List<UUID> teachers,
        String classRoom,
        BigDecimal fee,
        LocalDate startDate,
        LocalDate endDate,
        LocalTime startTime,
        LocalTime endTime
) {
        public  int getTotalStudentCapacityOrDefault(){
                return studentCapacity != null ? studentCapacity : 20; // add a variable to decide size of student for coaching owner
        }
}
