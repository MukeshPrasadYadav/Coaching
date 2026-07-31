package com.projects.coaching_offline_support.Coaching.service;

import com.projects.coaching_offline_support.Coaching.dto.request.AddCoachingRequest;
import com.projects.coaching_offline_support.Coaching.dto.request.RemoveCoachingRequest;
import com.projects.coaching_offline_support.Coaching.dto.response.AddCoachingResponse;
import com.projects.coaching_offline_support.Coaching.dto.response.BasicCoachingInfo;
import com.projects.coaching_offline_support.Coaching.dto.response.CoachingDashboard;
import com.projects.coaching_offline_support.Coaching.dto.response.CoachingResponse;
import com.projects.coaching_offline_support.common.entity.Address;
import jakarta.validation.Valid;

import java.util.UUID;

public interface CoachingService {

    AddCoachingResponse add(@Valid AddCoachingRequest request);
    CoachingResponse getCoaching();

    void remove(UUID coachingId, @Valid RemoveCoachingRequest request);



    CoachingResponse updateAddress(UUID coachingID, Address address);

    CoachingResponse updateInfo(UUID coachingID, BasicCoachingInfo info);

    CoachingDashboard getDashboard();
}
