package com.projects.coaching_offline_support.Coaching.impl;

import com.projects.coaching_offline_support.Coaching.dto.*;
import com.projects.coaching_offline_support.Coaching.entity.Coaching;
import com.projects.coaching_offline_support.Coaching.enums.CoachingStatus;
import com.projects.coaching_offline_support.Coaching.repository.CoachingRepository;
import com.projects.coaching_offline_support.Coaching.service.CoachingService;
import com.projects.coaching_offline_support.batch.dto.response.BatchInfo;
import com.projects.coaching_offline_support.batch.entity.Batch;
import com.projects.coaching_offline_support.batch.enums.BatchStatus;
import com.projects.coaching_offline_support.batch.repository.BatchRepository;
import com.projects.coaching_offline_support.common.Exceptions.ResourceNotFoundException;
import com.projects.coaching_offline_support.common.Exceptions.DuplicateException;
import com.projects.coaching_offline_support.common.Service.impl.CurrentUser;
import com.projects.coaching_offline_support.common.components.RepositoryUtils;
import com.projects.coaching_offline_support.common.entity.Address;
import com.projects.coaching_offline_support.teacher.entity.CoachingTeacher;
import com.projects.coaching_offline_support.teacher.entity.Teacher;
import com.projects.coaching_offline_support.teacher.events.TeacherAddedEvent;
import com.projects.coaching_offline_support.teacher.repository.TeacherRepository;
import com.projects.coaching_offline_support.user.User;
import com.projects.coaching_offline_support.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoachingServiceImpl implements CoachingService {


    private final CoachingRepository coachingRepository;
    private final TeacherRepository teacherRepository;
    private final BatchRepository batchRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher publisher;
    private final  String CACHE_NAME = "Coaching";

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ADD_COACHING')")
    @CachePut(cacheNames = CACHE_NAME, key = "#result.id")
    public AddCoachingResponse add(AddCoachingRequest request) {

        User user =userRepository.findByEmail(request.ownerEmail()).orElseThrow(() -> new ResourceNotFoundException("No user found"));

        log.info("Adding new Coaching");
        boolean isExist = coachingRepository.existsByCoachingName(request.name());
        if(isExist) throw new DuplicateException("Coaching already  exists");

        user.setContactNumber(request.ownerContactNumber());
        user.setAddress(request.address());
        user.setProfileCompleted(true);
        userRepository.save(user);

        Coaching addedCoaching = Coaching.builder()
                .coachingName(request.name())
                .user(user)
                .build();
        coachingRepository.save(addedCoaching);


        return new AddCoachingResponse(
                addedCoaching.getId(),request.name(),
                request.ownerName(),request.address(),
                request.ownerContactNumber(),request.ownerEmail());
    }

    
    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public CoachingResponse getCoaching() {
        log.info("Fetching coaching with user id : {}",CurrentUser.get().getId());
        Coaching coaching = coachingRepository.findByUserId(CurrentUser.get().getId());
        if(coaching == null) throw new ResourceNotFoundException("No coaching found");

        return CoachingResponse.fromEntity(coaching);
    }

    @Override
    @Transactional
    public void remove(UUID coachingId, RemoveCoachingRequest request) {
        log.info("Removing coaching ");
       Coaching coaching = coachingRepository.findById(coachingId).orElseThrow(() -> new ResourceNotFoundException("No coaching found."));

       if(coaching.getStatus().equals(CoachingStatus.CLOSED)){
           throw new RuntimeException("Coaching already closed.");
       }
       coaching.setStatus(CoachingStatus.CLOSED);

       // Todo remove students, remove teacher , remove parent , remove batch
    }



    @Override
    @Transactional
    @CachePut(cacheNames = CACHE_NAME, key = "#result.id")
    public CoachingResponse updateAddress(UUID coachingId, Address address) {
        Coaching coaching = coachingRepository.findById(coachingId)
                .orElseThrow(() -> new ResourceNotFoundException("No coaching found."));

        if(coaching.getUser().getAddress().equals(address)){
            throw  new DuplicateException("Address already same");
        }
        coaching.getUser().setAddress(address);
        coachingRepository.save(coaching);
        return CoachingResponse.fromEntity(coaching);
    }

    @Override
    @Transactional
    public CoachingResponse updateInfo(UUID coachingId, BasicCoachingInfo info) {

        Coaching coaching = coachingRepository.findById(coachingId)
                .orElseThrow(() -> new ResourceNotFoundException("No coaching found."));

        if(!coaching.getCoachingName().equals(info.name())){
            coaching.setCoachingName(info.name());
        }
        if(! coaching.getUser().getName().equals(info.ownerName())){
            coaching.getUser().setName(info.ownerName());
        }
        if(! coaching.getUser().getContactNumber().equals(info.ownerContactNumber())){
            coaching.getUser().setContactNumber(info.ownerContactNumber());
        }
        if(! coaching.getUser().getEmail().equals(info.ownerEmail())){
            coaching.getUser().setEmail(info.ownerEmail());
        }
        coachingRepository.save(coaching);


        return CoachingResponse.fromEntity(coaching);
    }





}

