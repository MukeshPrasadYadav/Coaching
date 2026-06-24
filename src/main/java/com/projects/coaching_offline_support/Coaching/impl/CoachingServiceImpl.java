package com.projects.coaching_offline_support.Coaching.impl;

import com.projects.coaching_offline_support.Coaching.dto.AddCoachingRequest;
import com.projects.coaching_offline_support.Coaching.dto.CoachingResponse;
import com.projects.coaching_offline_support.Coaching.entity.Coaching;
import com.projects.coaching_offline_support.Coaching.repository.CoachingRepository;
import com.projects.coaching_offline_support.Coaching.service.CoachingService;
import com.projects.coaching_offline_support.common.Exceptions.UserAlreadyExistsException;
import com.projects.coaching_offline_support.common.Exceptions.UserNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoachingServiceImpl implements CoachingService {


    private final CoachingRepository coachingRepository;

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ADD_COACHING')")
    public CoachingResponse add(AddCoachingRequest request) {
        boolean isExist = coachingRepository.existsByName(request.name());
        if(isExist) throw new UserAlreadyExistsException("Coaching already  exists");

        Coaching addedCoaching = Coaching.builder()
                .name(request.name())
                .address(request.address())
                .ownerName(request.ownerName())
                .ownerEmail(request.ownerEmail())
                .ownerContactNumber(request.ownerContactNumber())
                .build();
        coachingRepository.save(addedCoaching);

        return new CoachingResponse(addedCoaching.getId(),request.name(),request.ownerName(),request.address(),request.ownerContactNumber(),request.ownerEmail());
    }
}
