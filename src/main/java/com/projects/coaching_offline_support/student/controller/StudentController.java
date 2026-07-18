package com.projects.coaching_offline_support.student.controller;


import com.projects.coaching_offline_support.common.dtos.ApiResponse;
import com.projects.coaching_offline_support.student.dto.request.AddStudent;
import com.projects.coaching_offline_support.student.dto.request.StudentFilter;
import com.projects.coaching_offline_support.student.dto.response.StudentResponse;

import com.projects.coaching_offline_support.student.service.StudentService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

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
    public ResponseEntity<ApiResponse<Page<StudentResponse>>> getStudents(
            StudentFilter filter,
            Pageable pageable
    ) {
        Page<StudentResponse> result = studentService.getStudents(filter,pageable);
        return ResponseEntity.ok(ApiResponse.success(result,"Fetched students successfully"));
    }

    @GetMapping("/export")
    public ResponseEntity<InputStreamResource> exportStudents( StudentFilter filter) throws IOException {

        ByteArrayInputStream inputStream = studentService.exportStudents(filter);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=students.xlsx")
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(inputStream));
    }

}
