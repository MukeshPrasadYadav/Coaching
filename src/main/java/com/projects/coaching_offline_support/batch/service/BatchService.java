package com.projects.coaching_offline_support.batch.service;

import com.projects.coaching_offline_support.batch.dto.request.AddBatchRequest;
import com.projects.coaching_offline_support.batch.dto.request.BatchFilter;
import com.projects.coaching_offline_support.batch.dto.response.BatchInfo;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface BatchService {
    void addBatch(UUID coachingId,@Valid AddBatchRequest request);

    BatchInfo getBatchById(UUID coachingId, UUID batchId);

    Page<BatchInfo> getBatch(BatchFilter filter, Pageable pageable);

    ByteArrayInputStream exportBatches(BatchFilter filter) throws IOException;

    List<BatchInfo> getBatches();


}
