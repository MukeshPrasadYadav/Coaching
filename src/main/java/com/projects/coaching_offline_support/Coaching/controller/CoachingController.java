package com.projects.coaching_offline_support.Coaching.controller;


import com.projects.coaching_offline_support.Coaching.dto.AddCoachingRequest;
import com.projects.coaching_offline_support.Coaching.dto.AddCoachingResponse;
import com.projects.coaching_offline_support.Coaching.dto.CoachingResponse;
import com.projects.coaching_offline_support.Coaching.service.CoachingService;
import com.projects.coaching_offline_support.common.dtos.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/coaching")
@RequiredArgsConstructor
@Slf4j
public class CoachingController {

    private final CoachingService coachingService;

    @PostMapping
    public ResponseEntity<ApiResponse<AddCoachingResponse>> addCoaching(@RequestBody @Valid AddCoachingRequest request){
        AddCoachingResponse response = coachingService.add(request);
        return ResponseEntity.ok(ApiResponse.success(response,"Added coaching successfully."));
    }

    @GetMapping("/{coachingId}")
    public ResponseEntity<ApiResponse<Optional<CoachingResponse>>> getCoachingById(@PathVariable UUID coachingId){
        Optional<CoachingResponse>  response = coachingService.getCoachingById(coachingId);
        return ResponseEntity.ok(ApiResponse.success(response,"Fetched coaching successfully."));

    }

}
