package com.projects.coaching_offline_support.batch.service.impl;

import com.projects.coaching_offline_support.Coaching.entity.Coaching;
import com.projects.coaching_offline_support.Coaching.repository.CoachingRepository;
import com.projects.coaching_offline_support.Coaching.service.CoachingService;
import com.projects.coaching_offline_support.batch.dto.request.AddBatchRequest;
import com.projects.coaching_offline_support.batch.dto.request.BatchFilter;
import com.projects.coaching_offline_support.batch.dto.response.BatchConflictResponse;
import com.projects.coaching_offline_support.batch.dto.response.BatchInfo;
import com.projects.coaching_offline_support.batch.entity.Batch;
import com.projects.coaching_offline_support.batch.entity.BatchSchedule;
import com.projects.coaching_offline_support.batch.repository.BatchRepository;
import com.projects.coaching_offline_support.batch.repository.BatchScheduleRepository;
import com.projects.coaching_offline_support.batch.service.BatchService;
import com.projects.coaching_offline_support.batch.specification.BatchSpecification;
import com.projects.coaching_offline_support.common.Exceptions.BatchTimingConflictException;
import com.projects.coaching_offline_support.common.Exceptions.ResourceNotFoundException;
import com.projects.coaching_offline_support.common.Exceptions.DuplicateException;
import com.projects.coaching_offline_support.common.Service.impl.CurrentUser;
import com.projects.coaching_offline_support.common.components.RepositoryUtils;
import com.projects.coaching_offline_support.common.entity.Timing;
import com.projects.coaching_offline_support.common.enums.DaysOfWeek;
import com.projects.coaching_offline_support.teacher.entity.Teacher;
import com.projects.coaching_offline_support.teacher.repository.TeacherRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
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
    private final BatchScheduleRepository scheduleRepository;

    public boolean isTeacherAvailable(UUID teacherId, DaysOfWeek day,
                                      LocalTime start, LocalTime end) {
        List<BatchSchedule> existing = scheduleRepository.findByTeacherAndDay(teacherId, day);

        return existing.stream().noneMatch(s ->
                start.isBefore(s.getTiming().getEndTime()) && s.getTiming().getStartTime().isBefore(end)
        );
    }


    @Transactional
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void addBatch(AddBatchRequest request) {
        UUID userId = CurrentUser.detail().id();
        if(userId == null) throw new ResourceNotFoundException("No user found");

        Coaching coaching = coachingRepository.findByUserId(userId);
        if(coaching == null) throw new ResourceNotFoundException("No coaching found");

        Batch batch = Batch.builder()
                .name(request.batchName())
                .coaching(coaching)
                .totalStudents(request.getTotalStudentOrDefault())
                .fees(request.fees())
                .startDate(request.startingDate())
                .endDate(request.endingDate())
                .build();
        batchRepository.save(batch);




    }

    @Override
    public BatchInfo getBatchById(UUID coachingId, UUID batchId) {

        Batch batch = RepositoryUtils.findOrThrowById(batchRepository,batchId,"batch");

        return BatchInfo.fromEntity(batch);

    }

    @Override
    public Page<BatchInfo> getBatch(BatchFilter filter,int page,int size) {

        Sort sort = Sort.by(Sort.Order.desc("createdAt"));
        Pageable pagable = PageRequest.of(page,size,sort);
        Page<Batch> info = batchRepository.findAll(
                BatchSpecification.filter(filter),pagable
        );

       return info.map(BatchInfo::fromEntity);
    }




}
