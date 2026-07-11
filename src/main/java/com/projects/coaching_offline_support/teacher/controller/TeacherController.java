package com.projects.coaching_offline_support.teacher.controller;

import com.projects.coaching_offline_support.common.dtos.ApiResponse;
import com.projects.coaching_offline_support.teacher.dto.request.RegisterTeacherRequest;
import com.projects.coaching_offline_support.teacher.dto.response.TeacherResponse;
import com.projects.coaching_offline_support.teacher.service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    // Todo teacher add is dublicate add validation for adding teacher
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> addTeacher(@Valid @RequestBody RegisterTeacherRequest request){

        teacherService.add(request);
        return ResponseEntity.ok(ApiResponse.success("Added teacher successfully."));
    }

    @GetMapping("/{teacherId}")
    public  ResponseEntity<ApiResponse<TeacherResponse>> getTeacher(@PathVariable UUID teacherId){

        return ResponseEntity.ok(ApiResponse.success(teacherService.getTeacherById(teacherId),"fetched teacher successfully"));
    }
}
