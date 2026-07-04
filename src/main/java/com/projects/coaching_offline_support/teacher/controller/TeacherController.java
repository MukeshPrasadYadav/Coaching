package com.projects.coaching_offline_support.teacher.controller;

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

    @PostMapping
    public ResponseEntity<Void> addTeacher(@Valid @RequestBody AddTeacherRequest request){

        return ResponseEntity.status(HttpStatus.CREATED).body(teacherService.add(request));
    }
}
