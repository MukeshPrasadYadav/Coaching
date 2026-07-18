package com.projects.coaching_offline_support.teacher.controller;

import com.projects.coaching_offline_support.common.dtos.ApiResponse;
import com.projects.coaching_offline_support.student.dto.request.StudentFilter;
import com.projects.coaching_offline_support.student.dto.response.StudentResponse;
import com.projects.coaching_offline_support.teacher.dto.request.AddTeacherRequest;
import com.projects.coaching_offline_support.teacher.dto.request.RegisterTeacherRequest;
import com.projects.coaching_offline_support.teacher.dto.request.TeacherFilter;
import com.projects.coaching_offline_support.teacher.dto.response.TeacherCoachingResponse;
import com.projects.coaching_offline_support.teacher.dto.response.TeacherResponse;
import com.projects.coaching_offline_support.teacher.service.TeacherService;
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
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    // Todo teacher add is dublicate add validation for adding teacher
    @PostMapping
    public ResponseEntity<ApiResponse<TeacherResponse>> addTeacher(@Valid @RequestBody RegisterTeacherRequest request){


        return ResponseEntity.ok(ApiResponse.success(teacherService.add(request),"Added teacher successfully."));
    }

    @PostMapping("/addTeacher")
    public ResponseEntity<ApiResponse<TeacherCoachingResponse>> addTeacher(@Valid @RequestBody AddTeacherRequest request){

        return ResponseEntity.ok(ApiResponse.success(teacherService.addTeacherToCoaching(request),"Teacher added successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<TeacherResponse>>> getTeachers(
            TeacherFilter filter,
            Pageable pageable
    ) {
        Page<TeacherResponse> result = teacherService.getTeachers(filter,pageable);
        return ResponseEntity.ok(ApiResponse.success(result,"Fetched students successfully"));
    }

    @GetMapping("/export")
    public ResponseEntity<InputStreamResource> exportTeachers(TeacherFilter filter) throws IOException {

        ByteArrayInputStream inputStream = teacherService.exportTeachers(filter);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=students.xlsx")
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(inputStream));
    }

    @GetMapping("/{teacherId}")
    public  ResponseEntity<ApiResponse<TeacherResponse>> getTeacher(@PathVariable UUID teacherId){

        return ResponseEntity.ok(ApiResponse.success(teacherService.getTeacherById(teacherId),"fetched teacher successfully"));
    }
}
