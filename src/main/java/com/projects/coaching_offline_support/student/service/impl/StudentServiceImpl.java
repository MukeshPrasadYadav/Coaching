package com.projects.coaching_offline_support.student.service.impl;


import com.projects.coaching_offline_support.common.Service.impl.ExcelExportService;
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final ExcelExportService excelExportService;


    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public StudentResponse addStudent(AddStudent request) {
        Student student = Student.builder()
                .name(request.name())
                .email(request.email())
                .contactNumber(request.contactNumber())
                .batch(request.batch())
                .standard(request.class_std())
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


    @Override
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
                        student.getName(),
                        student.getContactNumber(),
                        student.getEmail(),
                        student.getStandard(),
                        student.getCreatedAt(),
                        student.getParentName(),
                        student.getParentNumber(),
                        student.getParentEmail()
                )
        );
    }


}
