package com.projects.coaching_offline_support.batch.service;

import com.projects.coaching_offline_support.batch.dto.request.AddBatchRequest;
import com.projects.coaching_offline_support.batch.dto.request.BatchFilter;
import com.projects.coaching_offline_support.batch.dto.response.BatchInfo;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface BatchService {
    void addBatch(@Valid AddBatchRequest request);

    BatchInfo getBatchById(UUID coachingId, UUID batchId);

    Page<BatchInfo> getBatch(BatchFilter filter,int page,int size);
}
