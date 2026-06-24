package com.projects.coaching_offline_support.Coaching.controller;


import com.projects.coaching_offline_support.Coaching.dto.AddCoachingRequest;
import com.projects.coaching_offline_support.Coaching.dto.CoachingResponse;
import com.projects.coaching_offline_support.Coaching.service.CoachingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/coaching")
@RequiredArgsConstructor
@Slf4j
public class CoachingController {

    private final CoachingService coachingService;

    @PostMapping
    public ResponseEntity<CoachingResponse> addCoaching(@RequestBody @Valid AddCoachingRequest request){
        log.info("=== CONTROLLER HIT ===");
        return ResponseEntity.ok(coachingService.add(request));
    }

}
