package com.projects.coaching_offline_support.Coaching.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.projects.coaching_offline_support.batch.dto.response.BatchInfo;
import com.projects.coaching_offline_support.batch.entity.Batch;
import com.projects.coaching_offline_support.common.entity.Address;
import lombok.Builder;

import java.util.List;
import java.util.UUID;


public record CoachingResponse(
        UUID id,
        String ownerName,
        String name,
        Address address,
        Integer noOfBatches,
        String ownerContactNumber,
        Integer noOfStudent,
        List<BatchInfo> batches,
        String ownerEmail
) {
}
