package com.projects.coaching_offline_support.batch.controller;

import com.projects.coaching_offline_support.batch.dto.request.AddBatchRequest;
import com.projects.coaching_offline_support.batch.dto.response.BatchInfo;
import com.projects.coaching_offline_support.batch.service.BatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/batch")
@RequiredArgsConstructor
@Slf4j
public class BatchController {

    private final BatchService batchService;

    @PostMapping
    public ResponseEntity<Void> addNewBatch(@Valid @RequestBody AddBatchRequest request){
        log.info("Inside add batch controller");
        return ResponseEntity.status(HttpStatus.CREATED).body(batchService.addBatch(request));
    }

    @GetMapping("{coachingId}/{batchId}")
    public ResponseEntity<BatchInfo> getBatchById(@PathVariable UUID coachingId, @PathVariable UUID batchId){
        return ResponseEntity.status(HttpStatus.OK).body(batchService.getBatchById(coachingId,batchId));
    }
}
