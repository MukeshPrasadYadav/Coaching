package com.projects.coaching_offline_support.student.service.impl;


import com.projects.coaching_offline_support.audit.entity.Auditable;
import com.projects.coaching_offline_support.audit.enums.ActionType;
import com.projects.coaching_offline_support.audit.enums.LogType;
import com.projects.coaching_offline_support.batch.entity.Batch;
import com.projects.coaching_offline_support.batch.repository.BatchRepository;
import com.projects.coaching_offline_support.common.Service.FileService;
import com.projects.coaching_offline_support.common.Service.impl.CurrentUser;
import com.projects.coaching_offline_support.common.Service.impl.ExcelExportService;
import com.projects.coaching_offline_support.common.components.RepositoryUtils;
import com.projects.coaching_offline_support.common.enums.Role;
import com.projects.coaching_offline_support.student.dto.request.AddStudent;
import com.projects.coaching_offline_support.student.dto.request.CompleteStudentProfileRequest;
import com.projects.coaching_offline_support.student.dto.request.StudentFilter;
import com.projects.coaching_offline_support.student.dto.response.StudentCoachingResponse;
import com.projects.coaching_offline_support.student.dto.response.StudentDetail;
import com.projects.coaching_offline_support.student.entity.Student;
import com.projects.coaching_offline_support.student.repository.StudentRepository;
import com.projects.coaching_offline_support.student.service.StudentService;
import com.projects.coaching_offline_support.student.specification.StudentSpecification;
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
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final ExcelExportService excelExportService;
    private final UserRepository userRepository;
    private final BatchRepository batchRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileService fileService;


    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    @Auditable(
           logType = LogType.STUDENT,
            actionType = ActionType.CREATED,
             description = " #{#request.email} in  batch #{#request.batch}"
    )
    public StudentCoachingResponse addStudent(AddStudent request) {

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .contactNumber(request.contactNumber())
                .hashedPassword(passwordEncoder.encode("Default_password"))
                .role(Role.STUDENT)
                .build();
        userRepository.save(user);

        Batch batch = RepositoryUtils.findOrThrowById(batchRepository,request.batch(),"Batch");
        batch.setTotalStudents(batch.getTotalStudents()+1);

        Student student = Student.builder()
                .user(user)
                .build();
        

        studentRepository.save(student);
        student.getBatches().add(batch);
        
        return StudentCoachingResponse.fromEntity(student);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public Page<StudentCoachingResponse> getStudents(StudentFilter filter, Pageable pageable) {

        return studentRepository.findAll(
                StudentSpecification.filter(filter),
                pageable
        ).map(StudentCoachingResponse::fromEntity);
    }


    @Auditable(
            logType = LogType.STUDENT,
            actionType = ActionType.DOWNLOADED,
            description = "Downloaded students list."
    )
    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public ByteArrayInputStream exportStudents(StudentFilter filter) throws IOException {

        List<Student> students = studentRepository.findAll(StudentSpecification.filter(filter));


        List<String> headers = List.of(
                "Student Id",
                "Name",
                "Phone",
                "Email",
                "Course",
                "Admission Date",
                "Parent Name",
                "Parent Number",
                "Prent email"
        );


        return excelExportService.export(
                "Students",
                headers,
                students,
                student -> List.of(
                        student.getId(),
                        student.getUser().getName(),
                        student.getUser().getContactNumber(),
                        student.getUser().getEmail(),
                        student.getBatches().stream().map(Batch::getName),
                        student.getCreatedAt(),
                        safe(student.getParentName()),
                        safe(student.getParentNumber()),
                        safe(student.getParentEmail())
                )
        );
    }


    @Transactional
    @Auditable(
            logType = LogType.STUDENT,
            actionType = ActionType.PROFILE_COMPLETED,
            description = "Student #{#request.email} completed profile."
    )
    @Override
    public void completeProfile(CompleteStudentProfileRequest request) {

        System.out.println("Request studnet complete profile"+request);

        User user = RepositoryUtils.findOrThrowById(userRepository,CurrentUser.get().getId(), "User");


        user.setContactNumber(request.contactNumber());
        user.setAddress(request.address());
        user.setDob(request.dob());
        user.setGender(request.gender());
        user.setProfilePic(request.profilePic());


        user.setProfileCompleted(true);
        user.setAddress(request.address());
        User savedUser =  userRepository.save(user);

        Student student = studentRepository.findById(user.getId()).orElseGet(() -> Student.builder().user(savedUser).build());

        student.setFatherName(request.fatherName());
        student.setUser(savedUser);
        student.setMotherName(request.motherName());
        student.setParentName(request.parentName());
        student.setParentNumber(request.parentPhone());
        student.setParentEmail(request.parentEmail());


        studentRepository.save(student);

    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public StudentDetail getStudentById(UUID studentId) {
        User user = CurrentUser.get();
        Student student = RepositoryUtils.findOrThrowById(studentRepository,studentId,"Student");

        String profilePic = null;
        if(student.getUser().getProfilePic() != null){
            profilePic = fileService.getProfilePicture(student.getUser().getProfilePic());
        }
        return StudentDetail.forAdmin(student,profilePic);

    }


    private String safe(String value) {
        return value == null || value.isBlank() ? "-" : value;
    }


}
