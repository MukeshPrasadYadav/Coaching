package com.projects.coaching_offline_support.batch.service.impl;

import com.projects.coaching_offline_support.Coaching.entity.Coaching;
import com.projects.coaching_offline_support.Coaching.repository.CoachingRepository;
import com.projects.coaching_offline_support.Coaching.service.CoachingService;
import com.projects.coaching_offline_support.audit.entity.Auditable;
import com.projects.coaching_offline_support.audit.enums.ActionType;
import com.projects.coaching_offline_support.audit.enums.LogType;
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
import com.projects.coaching_offline_support.common.Service.impl.ExcelExportService;
import com.projects.coaching_offline_support.common.components.RepositoryUtils;
import com.projects.coaching_offline_support.common.entity.Timing;
import com.projects.coaching_offline_support.common.enums.DaysOfWeek;
import com.projects.coaching_offline_support.student.dto.request.StudentFilter;
import com.projects.coaching_offline_support.student.entity.Student;
import com.projects.coaching_offline_support.student.specification.StudentSpecification;
import com.projects.coaching_offline_support.teacher.entity.Teacher;
import com.projects.coaching_offline_support.teacher.repository.TeacherRepository;
import com.projects.coaching_offline_support.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
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
    private final BatchScheduleRepository batchScheduleRepository;
    private final ExcelExportService excelExportService;

//    public boolean isTeacherAvailable(UUID teacherId, DaysOfWeek day,
//                                      LocalTime start, LocalTime end) {
//        List<BatchSchedule> existing = scheduleRepository.findByTeacherAndDay(teacherId, day);
//
//        return existing.stream().noneMatch(s ->
//                start.isBefore(s.getTiming().getEndTime()) && s.getTiming().getStartTime().isBefore(end)
//        );
//    }


    @Transactional
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Auditable(
            logType = LogType.BATCH,
            actionType = ActionType.CREATED,
            description = "Batch added by #{#coachingId}"
    )
    public void addBatch(UUID coachingId,AddBatchRequest request) {


        Coaching coaching = RepositoryUtils.findOrThrowById(coachingRepository,coachingId,"Coaching");

        List<Teacher> teachers = teacherRepository.findByCoachingId(coachingId)
                .stream()
                .filter(teacher ->
                        request.teachers().contains(teacher.getId())
                )
                .toList();


        Batch batch = Batch.builder()
                .name(request.name())
                .coaching(coaching)
                .totalCapacity(request.getTotalStudentCapacityOrDefault())
                .fee(request.fee())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .subjects(request.subjects())
                .build();
        batchRepository.save(batch);

        coaching.getBatches().add(batch);
        coachingRepository.save(coaching);

        for (Teacher teacher : teachers) {

            BatchSchedule schedule = BatchSchedule.builder()
                    .batch(batch)
                    .teacher(teacher)
                    .timing(Timing.builder()
                            .startTime(request.startTime())
                            .endTime(request.endTime())
                            .build())
                    .build();

            batch.getSchedules().add(schedule);
            teacher.getSchedules().add(schedule);

            batchScheduleRepository.save(schedule);
        }



    }

    @Override
    public BatchInfo getBatchById(UUID coachingId, UUID batchId) {

        Batch batch = RepositoryUtils.findOrThrowById(batchRepository,batchId,"batch");

        return switch (CurrentUser.get().getRole()){
            case ADMIN -> BatchInfo.forAdmin(batch);
            case STUDENT -> BatchInfo.forStudent(batch);
            case TEACHER -> BatchInfo.forTeacher(batch);

            case PARENT -> null;
        };

    }

    @Override
    public Page<BatchInfo> getBatch(BatchFilter filter,Pageable pageable) {
        User user = CurrentUser.get();

        Sort sort = Sort.by(Sort.Order.desc("createdAt"));

        Page<Batch> info = batchRepository.findAll(
                BatchSpecification.filter(filter,user),pageable
        );

       return switch (user.getRole()){
           case ADMIN -> info.map(BatchInfo::forAdmin);
           case STUDENT -> info.map(BatchInfo::forStudent);
           case TEACHER -> info.map(BatchInfo::forTeacher);

           case PARENT -> null;
       };
    }

    @Auditable(
            logType = LogType.BATCH,
            actionType = ActionType.DOWNLOADED,
            description = "Downloaded batch list."
    )
    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public ByteArrayInputStream exportBatches(BatchFilter filter) throws IOException {

        List<Batch> batches = batchRepository.findAll(BatchSpecification.filter(filter,CurrentUser.get()));

        List<String> headers = List.of(
                "Batch Id",
                "Name",
                "Total student",
                "Total teacher",
                "Fee",
                "Total Fee collection",
                "Teachers",
                "Start time",
                "End time",
                "Start Date",
                "End Date",
                "Subjects"
        );

        return excelExportService.export(
                "batches",
                headers,
                batches,
                batch -> List.of(
                        batch.getId(),
                        batch.getName(),
                        batch.getStudents().size(),
                        batch.getSchedules().size() , // to be changed with total teachers
                        batch.getFee(),
                        batch.getFee().multiply(BigDecimal.valueOf(batch.getStudents().size())),                        batch.isActive() ,  // to be replaced by teachers
                        batch.getSchedules().getFirst().getTiming().getStartTime(),
                        batch.getSchedules().getFirst().getTiming().getEndTime(),
                        batch.getStartDate(),
                        batch.getEndDate(),
                        batch.getSubjects()
                )
        );
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<BatchInfo> getBatches() {

       List<Batch> batches = batchRepository.findByCoaching_Id(CurrentUser.get().getId());
       return batches.stream().map(
               BatchInfo::forAdmin
       ).toList();
    }






}
