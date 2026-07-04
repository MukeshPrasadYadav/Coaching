package com.projects.coaching_offline_support.teacher.controller;

import com.projects.coaching_offline_support.common.dtos.ApiResponse;
import com.projects.coaching_offline_support.teacher.dto.request.AddTeacherRequest;
import com.projects.coaching_offline_support.teacher.service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    // Todo teacher add is dublicate add validation for adding teacher
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> addTeacher(@Valid @RequestBody AddTeacherRequest request){

        teacherService.add(request);
        return ResponseEntity.ok(ApiResponse.success("Added teacher successfully."));
    }
}
