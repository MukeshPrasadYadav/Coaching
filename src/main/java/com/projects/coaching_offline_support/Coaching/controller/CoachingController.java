package com.projects.coaching_offline_support.Coaching.controller;


import com.projects.coaching_offline_support.Coaching.dto.request.AddCoachingRequest;
import com.projects.coaching_offline_support.Coaching.dto.request.RemoveCoachingRequest;
import com.projects.coaching_offline_support.Coaching.dto.response.AddCoachingResponse;
import com.projects.coaching_offline_support.Coaching.dto.response.BasicCoachingInfo;
import com.projects.coaching_offline_support.Coaching.dto.response.CoachingDashboard;
import com.projects.coaching_offline_support.Coaching.dto.response.CoachingResponse;
import com.projects.coaching_offline_support.Coaching.service.CoachingService;
import com.projects.coaching_offline_support.common.dtos.ApiResponse;
import com.projects.coaching_offline_support.common.entity.Address;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<ApiResponse<CoachingResponse>> getCoaching(){

        return ResponseEntity.ok(ApiResponse.success(coachingService.getCoaching(),"Fetched coaching successfully."));
    }

    @PostMapping("/{coachingId}")
    public ResponseEntity<ApiResponse<Void>> removeCoaching(@PathVariable UUID coachingId, @RequestBody @Valid RemoveCoachingRequest request){
        coachingService.remove(coachingId,request);
        return ResponseEntity.ok(ApiResponse.success("Coaching closed successfully."));
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

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<CoachingDashboard>> getDashboard(){

        CoachingDashboard response = coachingService.getDashboard();
        return ResponseEntity.ok(ApiResponse.success(response,"Fetched data successfully"));
    }


}
