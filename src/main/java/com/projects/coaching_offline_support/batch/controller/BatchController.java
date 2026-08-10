package com.projects.coaching_offline_support.batch.controller;

import com.projects.coaching_offline_support.batch.dto.request.AddBatchRequest;
import com.projects.coaching_offline_support.batch.dto.request.BatchFilter;
import com.projects.coaching_offline_support.batch.dto.response.BatchDetail;
import com.projects.coaching_offline_support.batch.dto.response.BatchInfo;
import com.projects.coaching_offline_support.batch.enums.BatchStatus;
import com.projects.coaching_offline_support.batch.service.BatchService;
import com.projects.coaching_offline_support.common.dtos.ApiResponse;
import com.projects.coaching_offline_support.student.dto.request.StudentFilter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/batch")
@RequiredArgsConstructor
@Slf4j
public class BatchController {

    private final BatchService batchService;

    @PostMapping("/{coachingId}")
    public ResponseEntity<ApiResponse<Void>> addNewBatch(@PathVariable UUID coachingId ,@Valid @RequestBody AddBatchRequest request){
        batchService.addBatch(coachingId,request);
        return ResponseEntity.ok(ApiResponse.success("Batch added successfully."));
    }

    @GetMapping("/{batchId}")
    public ResponseEntity<ApiResponse<BatchDetail>> getBatchById(@PathVariable UUID batchId){
        com.projects.coaching_offline_support.batch.dto.response.BatchDetail batch = batchService.getBatchById(batchId);
        return ResponseEntity.ok(ApiResponse.success(batch,"Fetched batch information successfully."));
    }

    @GetMapping("/getBatchForEnroll")
    public ResponseEntity<ApiResponse<List<BatchInfo>>> getBatchInfoForEnrollingStudnet(){
        List<BatchInfo> batchInfos = batchService.getBatches();
        return ResponseEntity.ok(ApiResponse.success(batchInfos,"Fetched batches successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<BatchInfo>>> getBatch(
            BatchFilter filter,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int  pageSize,
            @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Sort sort

    ) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<BatchInfo> batchInfo = batchService.getBatch(filter,pageable);
        return ResponseEntity.ok(ApiResponse.success(batchInfo,"Fetched batch information successfully."));
    }


    @GetMapping("/export")
    public ResponseEntity<InputStreamResource> exportBatches(BatchFilter filter) throws IOException {

        ByteArrayInputStream inputStream = batchService.exportBatches(filter);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=batches.xlsx")
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(inputStream));
    }
}
