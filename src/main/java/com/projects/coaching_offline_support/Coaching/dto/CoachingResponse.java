package com.projects.coaching_offline_support.Coaching.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.projects.coaching_offline_support.Coaching.entity.Coaching;
import com.projects.coaching_offline_support.batch.dto.response.BatchInfo;
import com.projects.coaching_offline_support.batch.entity.Batch;
import com.projects.coaching_offline_support.common.entity.Address;
import com.projects.coaching_offline_support.common.entity.UserInfo;
import lombok.Builder;

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
                .map(batch ->
                        new BatchInfo(
                                batch.getId(),batch.getName(),
                                batch.getStatus()))
                .collect(Collectors.toList());



        return new CoachingResponse(
                coaching.getId(),
                coaching.getUser().getName(),
                coaching.getCoachingName(),
                coaching.getUser().getContactNumber(),
                coaching.getUser().getAddress(),
                coaching.getBatches().size(),
                coaching.getStudents().size(),
                batches,
                coaching.getUser().getEmail()

        );
    }
}
