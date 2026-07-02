package com.projects.coaching_offline_support.Coaching.impl;

import com.projects.coaching_offline_support.Coaching.dto.AddCoachingRequest;
import com.projects.coaching_offline_support.Coaching.dto.AddCoachingResponse;
import com.projects.coaching_offline_support.Coaching.dto.CoachingResponse;
import com.projects.coaching_offline_support.Coaching.entity.Coaching;
import com.projects.coaching_offline_support.Coaching.repository.CoachingRepository;
import com.projects.coaching_offline_support.Coaching.service.CoachingService;
import com.projects.coaching_offline_support.common.Exceptions.ResourceNotFoundException;
import com.projects.coaching_offline_support.common.Exceptions.UserAlreadyExistsException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoachingServiceImpl implements CoachingService {


    private final CoachingRepository coachingRepository;
    private final  String CACHE_NAME = "Coaching";

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ADD_COACHING')")
    @CachePut(cacheNames = CACHE_NAME, key = "#result.id")
    public AddCoachingResponse add(AddCoachingRequest request) {
        log.info("Adding new Coaching");
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

        return new AddCoachingResponse(
                addedCoaching.getId(),request.name(),
                request.ownerName(),request.address(),
                request.ownerContactNumber(),request.ownerEmail());
    }

    @Override
    @Cacheable(cacheNames = CACHE_NAME, key = "#coachingId")
    public Optional<CoachingResponse> getCoachingById(UUID coachingId) {
        log.info("Fetching coaching with id : {}",coachingId);
        Coaching coaching = coachingRepository.findById(coachingId)
                .orElseThrow(() -> new ResourceNotFoundException("Coaching not found with id :" + coachingId));

        CoachingResponse response = new CoachingResponse(
                coachingId, coaching.getOwnerName()
                , coaching.getName(), coaching.getAddress()
                , coaching.getBatches().size(),
                coaching.getOwnerContactNumber(),
                coaching.getStudents().size());

        return Optional.of(response);
    }

}

