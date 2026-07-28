package com.projects.coaching_offline_support.teacher.service.impl;

import com.projects.coaching_offline_support.Coaching.entity.Coaching;
import com.projects.coaching_offline_support.Coaching.repository.CoachingRepository;
import com.projects.coaching_offline_support.audit.entity.Auditable;
import com.projects.coaching_offline_support.audit.enums.ActionType;
import com.projects.coaching_offline_support.audit.enums.LogType;
import com.projects.coaching_offline_support.batch.entity.Batch;
import com.projects.coaching_offline_support.batch.entity.BatchSchedule;
import com.projects.coaching_offline_support.common.Exceptions.DegreeNotFoundException;
import com.projects.coaching_offline_support.common.Exceptions.ResourceNotFoundException;
import com.projects.coaching_offline_support.common.Service.impl.CurrentUser;
import com.projects.coaching_offline_support.common.Service.impl.ExcelExportService;
import com.projects.coaching_offline_support.common.components.RepositoryUtils;
import com.projects.coaching_offline_support.common.enums.DaysOfWeek;
import com.projects.coaching_offline_support.common.enums.Role;
import com.projects.coaching_offline_support.student.dto.response.StudentBatchResponse;
import com.projects.coaching_offline_support.teacher.dto.request.AddTeacherRequest;
import com.projects.coaching_offline_support.teacher.dto.request.AppointTeacherFilter;
import com.projects.coaching_offline_support.teacher.dto.request.RegisterTeacherRequest;
import com.projects.coaching_offline_support.teacher.dto.request.TeacherFilter;
import com.projects.coaching_offline_support.teacher.dto.response.TeacherCoachingResponse;
import com.projects.coaching_offline_support.teacher.dto.response.TeacherResponse;
import com.projects.coaching_offline_support.teacher.entity.CoachingTeacher;
import com.projects.coaching_offline_support.teacher.entity.Teacher;
import com.projects.coaching_offline_support.teacher.repository.CoachingTeacherRepository;
import com.projects.coaching_offline_support.teacher.repository.TeacherRepository;
import com.projects.coaching_offline_support.teacher.service.TeacherService;
import com.projects.coaching_offline_support.teacher.specification.AppointTeacherSpecification;
import com.projects.coaching_offline_support.teacher.specification.TeacherSpecification;
import com.projects.coaching_offline_support.user.User;
import com.projects.coaching_offline_support.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
@Slf4j
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private  final PasswordEncoder passwordEncoder;
    private final CoachingRepository coachingRepository;
    private final ExcelExportService excelExportService;
    private final CoachingTeacherRepository coachingTeacherRepository;


    private static final LocalTime DEFAULT_START = LocalTime.of(9, 0);
    private static final LocalTime DEFAULT_END = LocalTime.of(21, 0);

    @Override
    @Transactional
    public TeacherResponse add(RegisterTeacherRequest request) {
        User user = userRepository.findByEmail(request.email()).orElseThrow(() -> new ResourceNotFoundException("No user found with email"+request.email()));

        user.setContactNumber(request.contactNumber());
        user.setProfileCompleted(true);
        user.setAddress(request.address());
      User savedUser =  userRepository.save(user);



        Teacher teacher = Teacher.builder()
                .fee(request.fee())
                .degrees(request.degrees())
                .subjects(request.subjects())
                .user(savedUser)
                .build();

        teacherRepository.save(teacher);

        return TeacherResponse.fromEntity(teacher);
    }

    @Override
    @Transactional(readOnly = true)
    public TeacherResponse getTeacherById(UUID teacherId) {
        Teacher teacher = RepositoryUtils.findOrThrowById(teacherRepository,teacherId,"Teacher");

        return TeacherResponse.fromEntity(teacher);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    @Auditable(
            logType = LogType.TEACHER,
            actionType = ActionType.CREATED,
            description = "Added teacher with mail #{#request.email} and name #{#request.name}"
    )
    public TeacherCoachingResponse addTeacherToCoaching(AddTeacherRequest request) {
        Coaching coaching = coachingRepository.findByUserId(CurrentUser.get().getId());
        if(coaching == null) throw  new ResourceNotFoundException("No coaching found.");
        Teacher teacher = teacherRepository.findByUserEmail(request.email());
        if(teacher != null){
           if( !teacher.getDegrees().equals(request.degrees())) throw  new DegreeNotFoundException("Degree mismatch");

           // Coaching owner can also add subject that this teacher don't teach add logic for that TODO

            // TODO also check for batch timing once batch api is ready
        }
        if(teacher == null){

            User user = User.builder()
                    .role(Role.TEACHER)
                    .name(request.name())
                    .contactNumber(request.contactNumber())
                    .email(request.email())
                    .address(request.address())
                    .hashedPassword(passwordEncoder.encode("Default_password"))
                    .build();

            User savedUser = userRepository.save(user);
            teacher = Teacher.builder()
                    .user(savedUser)
                    .degrees(request.degrees())
                    .subjects(request.subjects())
                    .experience(request.experience())
                    .fee(BigDecimal.valueOf(500.00)) // remove hard coded value of fee
                  //  .batches(List.of(request.batches())) TODO add batch api then get batch from here becuase request batch contains string currently but need Batch
                    .build();

            Teacher finalTeacher = teacher;
            teacherRepository.save(teacher);

        }else{



            teacher.getSubjects().addAll(request.subjects());
            teacher.getDegrees().addAll(request.degrees());
            teacher.setFee(BigDecimal.valueOf(500));
            teacher.setExperience(request.experience());
            if(! teacher.getUser().getAddress().equals(request.address())) teacher.getUser().setAddress(request.address());

            teacherRepository.save(teacher);

        }
        CoachingTeacher coachingTeacher = CoachingTeacher.builder()
                .coaching(coaching)
                .teacher(teacher)
                .build();

        coachingTeacherRepository.save(coachingTeacher);
        coachingTeacherRepository.save(coachingTeacher);
        return TeacherCoachingResponse.fromEntity(teacher);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public Page<TeacherResponse> getTeachers(TeacherFilter filter, Pageable pageable) {


        return  teacherRepository.findAll(
                TeacherSpecification.filter(filter),
                pageable
        ).map(TeacherResponse::fromEntity);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @Auditable(
            logType = LogType.TEACHER,
            actionType = ActionType.DOWNLOADED,
            description = "Downloaded teachers list."
    )
    public ByteArrayInputStream exportTeachers(TeacherFilter filter) throws IOException {
        Coaching coaching = coachingRepository.findByUserId(CurrentUser.get().getId());
        if(coaching == null) throw new ResourceNotFoundException("No coaching found");
        List<Teacher> teachers = teacherRepository.findAll(TeacherSpecification.filter(filter));


        List<String> headers = List.of(
                "Teacher Id",
                "Name",
                "Phone",
                "Email",
                "Degrees",
                "Subjects",
                "Batches",
                "Experience"
            //    "Joining date" TODO add logic for getting joining date
        );

        return excelExportService.export(
                "Teachers",
                headers,
                teachers,
                teacher -> List.of(
                        teacher.getId(),
                        teacher.getUser().getName(),
                        teacher.getUser().getContactNumber(),
                        teacher.getUser().getEmail(),
                        teacher.getDegrees(),
                        teacher.getSubjects(),
                        teacher.getSchedules()
                                .stream()
                                .map(BatchSchedule::getBatch)
                                .distinct()
                                .map(Batch::getName)
                                .collect(Collectors.joining(", ")),
                        teacher.getExperience()
                )
        );
    }

    @Override
    public Page<TeacherResponse> appointTeacher(AppointTeacherFilter filter, Pageable pageable) {
        return  teacherRepository.findAll(
                AppointTeacherSpecification.filter(filter),
                pageable
        ).map(TeacherResponse::fromEntity);
    }

    @Override
    public List<TeacherResponse> getTeacherByCoachingId(UUID coachingId) {
        RepositoryUtils.findOrThrowById(coachingRepository,coachingId,"coaching");
       List<Teacher> teachers = teacherRepository.findByCoachingId(coachingId);

       return teachers.stream().map(
               TeacherResponse::fromEntity
       ).toList();

    }


}
