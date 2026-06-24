package com.projects.coaching_offline_support.Coaching.service;

import com.projects.coaching_offline_support.Coaching.dto.AddCoachingRequest;
import com.projects.coaching_offline_support.Coaching.dto.CoachingResponse;
import jakarta.validation.Valid;

public interface CoachingService {

    CoachingResponse add(@Valid AddCoachingRequest request);
}
