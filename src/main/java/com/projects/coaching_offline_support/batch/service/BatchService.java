package com.projects.coaching_offline_support.batch.service;

import com.projects.coaching_offline_support.batch.dto.request.AddBatchRequest;
import com.projects.coaching_offline_support.batch.dto.response.BatchInfo;
import jakarta.validation.Valid;

import java.util.UUID;

public interface BatchService {
    Void addBatch(@Valid AddBatchRequest request);

    BatchInfo getBatchById(UUID coachingId, UUID batchId);
}
