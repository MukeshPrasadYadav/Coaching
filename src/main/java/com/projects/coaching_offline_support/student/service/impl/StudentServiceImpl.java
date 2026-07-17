package com.projects.coaching_offline_support.student.service.impl;


import com.projects.coaching_offline_support.common.dtos.ApiResponse;
import com.projects.coaching_offline_support.student.dto.request.AddStudent;
import com.projects.coaching_offline_support.student.dto.request.StudentFilter;
import com.projects.coaching_offline_support.student.dto.response.StudentResponse;
import com.projects.coaching_offline_support.student.entity.Student;
import com.projects.coaching_offline_support.student.repository.StudentRepository;
import com.projects.coaching_offline_support.student.service.StudentService;
import com.projects.coaching_offline_support.student.specification.StudentSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;


    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public StudentResponse addStudent(AddStudent request) {
        Student student = Student.builder()
                .name(request.name())
                .email(request.email())
                .contactNumber(request.contactNumber())
                .address(request.address())
                .parentNumber(request.parentNumber())
                .parentEmail(request.parentEmail())
                .parentName(request.parentName())
                .build();

        studentRepository.save(student);
        return new StudentResponse(student.getId(),student.getName(),student.getStandard(),student.getBatch(),student.getCreatedAt());

    }

    @Override
    public Page<StudentResponse> getStudents(StudentFilter filter, Pageable pageable) {

        return studentRepository.findAll(
                StudentSpecification.filter(filter),
                pageable
        ).map(student -> new StudentResponse(student.getId(),student.getName(),student.getStandard(),student.getBatch(),student.getCreatedAt()));
    }
}
