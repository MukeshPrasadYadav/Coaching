package com.projects.coaching_offline_support.Coaching.impl;

import com.projects.coaching_offline_support.Coaching.dto.AddCoachingRequest;
import com.projects.coaching_offline_support.Coaching.dto.AddCoachingResponse;
import com.projects.coaching_offline_support.Coaching.dto.CoachingResponse;
import com.projects.coaching_offline_support.Coaching.dto.RemoveCoachingRequest;
import com.projects.coaching_offline_support.Coaching.entity.Coaching;
import com.projects.coaching_offline_support.Coaching.enums.CoachingStatus;
import com.projects.coaching_offline_support.Coaching.repository.CoachingRepository;
import com.projects.coaching_offline_support.Coaching.service.CoachingService;
import com.projects.coaching_offline_support.batch.dto.response.BatchInfo;
import com.projects.coaching_offline_support.batch.entity.Batch;
import com.projects.coaching_offline_support.batch.enums.BatchStatus;
import com.projects.coaching_offline_support.batch.repository.BatchRepository;
import com.projects.coaching_offline_support.common.Exceptions.ResourceNotFoundException;
import com.projects.coaching_offline_support.common.Exceptions.UserAlreadyExistsException;
import com.projects.coaching_offline_support.teacher.entity.Teacher;
import com.projects.coaching_offline_support.teacher.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

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
                coachingId, coaching.getOwnerName(),
                 coaching.getName(), coaching.getAddress(),
                 coaching.getBatches().size(),
                coaching.getOwnerContactNumber(),
                coaching.getStudents().size(),
                coaching.getBatches().stream()
                        .map(batch ->
                                new BatchInfo(batch.getId(),batch.getName(),batch.getTeacher().getName(),batch.getTimings(),batch.getCoaching().getName(),batch.getStatus()))
                        .collect(Collectors.toList()));

        return Optional.of(response);
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
       UnlinkStudents(coaching);
       UnlinkTeachers(coaching);
       UnlinkBatches(coaching);
       // Todo remove students, remove teacher , remove parent , remove batch
    }

    @Override
    @Transactional
    public void addTeacher( UUID coachingId, UUID teacherId){

        Coaching coaching = coachingRepository.findById(coachingId)
                .orElseThrow(() -> new ResourceNotFoundException("No coaching found."));

        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found."));

        coaching.getTeachers().add(teacher);
        teacher.addCoaching(coaching);
        coachingRepository.save(coaching);
    }

    private void UnlinkBatches(Coaching coaching){
        List<Batch> activeBatches = coaching.getBatches()
                .stream().filter(batch -> batch.getStatus() != BatchStatus.CLOSED).toList();

        activeBatches.forEach(batch -> {
            batch.setStatus(BatchStatus.CLOSED);
            batch.setReasonToClose("Coaching closed.");
        });

        batchRepository.saveAll(activeBatches);

    }

    private void UnlinkTeachers(Coaching coaching){
        List<Teacher> associatedTeachers = coaching.getTeachers();

        associatedTeachers.forEach(teacher -> {
            teacher.removeCoaching(coaching);
        });

        teacherRepository.saveAll(associatedTeachers);
    }

    private void UnlinkStudents(Coaching coaching){

        // Todo add messeging queue to send them notification

        System.out.println("Removed student successfully");

    }

}

