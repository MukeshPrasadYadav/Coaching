package com.projects.coaching_offline_support.Coaching.controller;


import com.projects.coaching_offline_support.Coaching.dto.*;
import com.projects.coaching_offline_support.Coaching.service.CoachingService;
import com.projects.coaching_offline_support.common.dtos.ApiResponse;
import com.projects.coaching_offline_support.common.entity.Address;
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

    @PostMapping("/{coachingId}")
    public ResponseEntity<ApiResponse<Void>> removeCoaching(@PathVariable UUID coachingId, @RequestBody @Valid RemoveCoachingRequest request){
        coachingService.remove(coachingId,request);
        return ResponseEntity.ok(ApiResponse.success("Coaching closed successfully."));
    }

    @PatchMapping("/{coachingId}/appoint/teacher")
    public ResponseEntity<ApiResponse<Void>> appointTeacher( @PathVariable UUID coachingId,@RequestBody UUID teacherId){
        coachingService.addTeacher(coachingId,teacherId);
        return ResponseEntity.ok(ApiResponse.success("Added teacher successfully"));
    }

    @PatchMapping("/{coachingId}/updateAddress")
    public ResponseEntity<ApiResponse<CoachingResponse>> updateAddress(@PathVariable UUID coachingId, @RequestBody Address address){
       CoachingResponse response = coachingService.updateAddress(coachingId,address);
        return ResponseEntity.ok(ApiResponse.success(response,"Address updated successfully"));
    }

    @PatchMapping("/{coachingId}/updateInfo")
    public ResponseEntity<ApiResponse<Void>> updateInfo(@PathVariable UUID coachingId, @RequestBody BasicCoachingInfo info){
       CoachingResponse response = coachingService.updateInfo(coachingId,info);
        return ResponseEntity.ok(ApiResponse.success("Address updated successfully"));
    }


}
