package com.projects.coaching_offline_support.batch.controller;

import com.projects.coaching_offline_support.batch.dto.request.AddBatchRequest;
import com.projects.coaching_offline_support.batch.dto.request.BatchFilter;
import com.projects.coaching_offline_support.batch.dto.response.BatchInfo;
import com.projects.coaching_offline_support.batch.enums.BatchStatus;
import com.projects.coaching_offline_support.batch.service.BatchService;
import com.projects.coaching_offline_support.common.dtos.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/batch")
@RequiredArgsConstructor
@Slf4j
public class BatchController {

    private final BatchService batchService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> addNewBatch(@Valid @RequestBody AddBatchRequest request){
        batchService.addBatch((request));
        return ResponseEntity.ok(ApiResponse.success("Batch added successfully."));
    }

    @GetMapping("{coachingId}/{batchId}")
    public ResponseEntity<ApiResponse<BatchInfo>> getBatchById(@PathVariable UUID coachingId, @PathVariable UUID batchId){
        BatchInfo batchInfo = batchService.getBatchById(coachingId,batchId);
        return ResponseEntity.ok(ApiResponse.success(batchInfo,"Fetched batch information successfully."));
    }

    @GetMapping("{coachingId}")
    public ResponseEntity<ApiResponse<Page<BatchInfo>>> getBatch(
            @PathVariable UUID coachingId,

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "createdAt") String sortBy,

            @RequestParam(defaultValue = "desc") String direction,

            @RequestParam(required = false) String search,

            @RequestParam(required = false) BatchStatus status,

            @RequestParam(required = false) String coachingName,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDateTime fromDate,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDateTime toDate
    ){

        BatchFilter filter = new BatchFilter(
                coachingId,
                coachingName,
                search,
                fromDate,
                toDate,
                status

        );
        Page<BatchInfo> batchInfo = batchService.getBatch(filter,page,size);
        return ResponseEntity.ok(ApiResponse.success(batchInfo,"Fetched batch information successfully."));
    }
}
