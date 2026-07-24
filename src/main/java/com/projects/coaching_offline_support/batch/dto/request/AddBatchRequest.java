package com.projects.coaching_offline_support.batch.dto.request;

import com.projects.coaching_offline_support.common.entity.Timing;
import com.projects.coaching_offline_support.common.enums.DaysOfWeek;
import com.projects.coaching_offline_support.teacher.entity.Teacher;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import javax.security.auth.Subject;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record AddBatchRequest(



        Integer studentCapacity,
        @NotBlank(message = "Batch name is required")
        String batchName,
        @NotNull(message = "Provide subjects for the batch")
        List<String> subjects,
        String classRoom,
        BigDecimal fees,
        LocalDate startingDate,
        LocalDate endingDate
) {
        public  int getTotalStudentOrDefault(){
                return studentCapacity != null ? studentCapacity : 20; // add a variable to decide size of student for coaching owner
        }
}
