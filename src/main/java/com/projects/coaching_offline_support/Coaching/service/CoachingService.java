package com.projects.coaching_offline_support.Coaching.service;

import com.projects.coaching_offline_support.Coaching.dto.*;
import com.projects.coaching_offline_support.Coaching.entity.Coaching;
import com.projects.coaching_offline_support.common.entity.Address;
import jakarta.validation.Valid;

import java.util.Optional;
import java.util.UUID;

public interface CoachingService {

    AddCoachingResponse add(@Valid AddCoachingRequest request);
    CoachingResponse getCoaching();

    void remove(UUID coachingId, @Valid RemoveCoachingRequest request);



    CoachingResponse updateAddress(UUID coachingID, Address address);

    CoachingResponse updateInfo(UUID coachingID, BasicCoachingInfo info);
}
