package com.projects.coaching_offline_support.Coaching.service;

import com.projects.coaching_offline_support.Coaching.dto.AddCoachingRequest;
import com.projects.coaching_offline_support.Coaching.dto.AddCoachingResponse;
import com.projects.coaching_offline_support.Coaching.dto.CoachingResponse;
import jakarta.validation.Valid;

import java.util.Optional;
import java.util.UUID;

public interface CoachingService {

    AddCoachingResponse add(@Valid AddCoachingRequest request);

   Optional<CoachingResponse> getCoachingById(UUID coachingId);
}
