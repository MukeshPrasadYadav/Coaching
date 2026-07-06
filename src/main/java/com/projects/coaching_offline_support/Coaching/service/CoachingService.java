package com.projects.coaching_offline_support.Coaching.service;

import com.projects.coaching_offline_support.Coaching.dto.AddCoachingRequest;
import com.projects.coaching_offline_support.Coaching.dto.AddCoachingResponse;
import com.projects.coaching_offline_support.Coaching.dto.CoachingResponse;
import com.projects.coaching_offline_support.Coaching.dto.RemoveCoachingRequest;
import com.projects.coaching_offline_support.Coaching.entity.Coaching;
import jakarta.validation.Valid;

import java.util.Optional;
import java.util.UUID;

public interface CoachingService {

    AddCoachingResponse add(@Valid AddCoachingRequest request);

   Optional<CoachingResponse> getCoachingById(UUID coachingId);

    void remove(UUID coachingId, @Valid RemoveCoachingRequest request);

    void addTeacher(UUID teacherId,UUID coachingId);
}
