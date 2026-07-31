package com.projects.coaching_offline_support.Coaching.dto.response;

import com.projects.coaching_offline_support.Coaching.entity.Coaching;
import com.projects.coaching_offline_support.batch.dto.response.BatchInfo;
import com.projects.coaching_offline_support.common.entity.Address;
import com.projects.coaching_offline_support.student.entity.Student;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


public record CoachingResponse(
        UUID id,
        String ownerName,
        String name,
        String ownerContactNumber,
        Address address,
        Integer noOfBatches,
        Integer noOfStudent,
        List<BatchInfo> batches,
        String ownerEmail

) {

    public static CoachingResponse fromEntity(Coaching coaching){
        List<BatchInfo> batches = coaching.getBatches().stream()
                .map(BatchInfo::forAdmin)
                .collect(Collectors.toList());

        int totalStudents = (int) coaching.getBatches()
                .stream()
                .flatMap(batch -> batch.getStudents().stream())
                .map(Student::getId)
                .distinct()
                .count();


        return new CoachingResponse(
                coaching.getId(),
                coaching.getUser().getName(),
                coaching.getCoachingName(),
                coaching.getUser().getContactNumber(),
                coaching.getUser().getAddress(),
                coaching.getBatches().size(),
                totalStudents,
                batches,
                coaching.getUser().getEmail()

        );
    }
}
