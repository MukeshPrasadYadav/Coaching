package com.projects.coaching_offline_support.student.controller;


import com.projects.coaching_offline_support.common.dtos.ApiResponse;
import com.projects.coaching_offline_support.student.dto.request.AddStudent;
import com.projects.coaching_offline_support.student.dto.request.StudentFilter;
import com.projects.coaching_offline_support.student.dto.response.StudentResponse;

import com.projects.coaching_offline_support.student.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<ApiResponse<StudentResponse>> addStudent(@Valid @RequestBody AddStudent request){

        return ResponseEntity.ok(ApiResponse.success(studentService.addStudent(request),"Student added successfully"));
    }

    @GetMapping
    public ResponseEntity<Page<StudentResponse>> getStudents(
            StudentFilter filter,
            Pageable pageable
    ) {
        return ResponseEntity.ok(studentService.getStudents(filter, pageable));
    }

}
