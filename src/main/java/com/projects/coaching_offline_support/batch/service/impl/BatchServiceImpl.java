package com.projects.coaching_offline_support.batch.service.impl;

import com.projects.coaching_offline_support.Coaching.entity.Coaching;
import com.projects.coaching_offline_support.Coaching.repository.CoachingRepository;
import com.projects.coaching_offline_support.Coaching.service.CoachingService;
import com.projects.coaching_offline_support.batch.dto.request.AddBatchRequest;
import com.projects.coaching_offline_support.batch.dto.request.BatchFilter;
import com.projects.coaching_offline_support.batch.dto.response.BatchConflictResponse;
import com.projects.coaching_offline_support.batch.dto.response.BatchInfo;
import com.projects.coaching_offline_support.batch.entity.Batch;
import com.projects.coaching_offline_support.batch.repository.BatchRepository;
import com.projects.coaching_offline_support.batch.service.BatchService;
import com.projects.coaching_offline_support.batch.specification.BatchSpecification;
import com.projects.coaching_offline_support.common.Exceptions.BatchTimingConflictException;
import com.projects.coaching_offline_support.common.Exceptions.ResourceNotFoundException;
import com.projects.coaching_offline_support.common.entity.Timing;
import com.projects.coaching_offline_support.common.enums.DaysOfWeek;
import com.projects.coaching_offline_support.teacher.entity.Teacher;
import com.projects.coaching_offline_support.teacher.repository.TeacherRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BatchServiceImpl implements BatchService {

    private final BatchRepository batchRepository;
    private final CoachingService coachingService;
    private final CoachingRepository coachingRepository;
    private final TeacherRepository teacherRepository;

    @Transactional
    @Override
    public void addBatch(AddBatchRequest request) {

       Coaching coaching =coachingRepository.findById(request.coachingId())
                .orElseThrow(() -> new ResourceNotFoundException("Coaching does not exist with the id: "+request.coachingId()));

        Teacher teacher = teacherRepository.findById(request.teacher())
                .orElseThrow(()-> new ResourceNotFoundException("Teacher not found"));

        // check if batch exists for same coaching for same time and same classroom
        List<Batch> batches = coaching.getBatches();
        List<BatchConflictResponse> conflictResponses = new ArrayList<>();

        for(Batch batch : batches){
            if( !batch.getClassRoom().equals(request.classRoom())) continue;
            for(Map.Entry<DaysOfWeek, Timing> entry : request.timings().entrySet()){
                DaysOfWeek day = entry.getKey();

                if(!batch.getTimings().containsKey(day)) continue;
                Timing existingTiming = batch.getTimings().get(day);
                Timing requestedTiming = entry.getValue();

                if(isOverLap(existingTiming,requestedTiming)){
                    conflictResponses.add(
                            new BatchConflictResponse(batch.getName(),day.toString(),existingTiming.getStartTime().toString() + " and " + existingTiming.getEndTime().toString())
                    );
                }
            }
        }

        if(!conflictResponses.isEmpty()) throw  new BatchTimingConflictException("Batch timings conflict",conflictResponses);


        Batch batch = Batch.builder()
                .fees(request.fees())
                .coaching(coaching)
                .name(request.batchName())
                .classRoom(request.classRoom())
                //.teacher(teacher)
                .timings(request.timings())
                .totalStudents(request.getTotalStudentOrDefault())       // todo add dynamic student capacity in request
                .build();

        batchRepository.save(batch);
        System.out.println(coaching);


    }

    @Override
    public BatchInfo getBatchById(UUID coachingId, UUID batchId) {
        coachingRepository.findById(coachingId).orElseThrow(()-> new ResourceNotFoundException("Coaching not found"));

        Batch batch = batchRepository.findById(batchId)
                .orElseThrow(() -> new ResourceNotFoundException("Batch not found"));

        return new BatchInfo(
                batchId,batch.getName()
                , batch.getTimings(),
                batch.getCoaching().getCoachingName(),batch.getStatus());


    }

    @Override
    public Page<BatchInfo> getBatch(BatchFilter filter,int page,int size) {

        Sort sort = Sort.by(Sort.Order.desc("createdAt"));
        Pageable pagable = PageRequest.of(page,size,sort);
        Page<Batch> info = batchRepository.findAll(
                BatchSpecification.filter(filter),pagable
        );

       return info.map(batch -> new BatchInfo(
               batch.getId(),batch.getName(),batch.getTimings(),
               batch.getCoaching().getCoachingName(),batch.getStatus()
       ));
    }

    private boolean isOverLap(Timing existing, Timing requested) {
        return existing.getStartTime().isBefore(requested.getEndTime())
                && requested.getStartTime().isBefore(existing.getEndTime());
    }


}
