package com.projects.coaching_offline_support.student.dto.response;

import com.projects.coaching_offline_support.batch.entity.Batch;

import java.util.UUID;

public record StudentBatchResponse(
        UUID batchId,
        String batchName
) {

    public static StudentBatchResponse fromEntity(Batch batch){
        return new StudentBatchResponse(
                batch.getId(),
                batch.getName()
        );
    }
}
