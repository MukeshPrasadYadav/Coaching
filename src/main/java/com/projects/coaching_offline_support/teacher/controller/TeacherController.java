package com.projects.coaching_offline_support.teacher.controller;

import com.projects.coaching_offline_support.common.dtos.ApiResponse;
import com.projects.coaching_offline_support.student.dto.request.StudentFilter;

import com.projects.coaching_offline_support.teacher.dto.request.*;
import com.projects.coaching_offline_support.teacher.dto.response.TeacherCoachingResponse;
import com.projects.coaching_offline_support.teacher.dto.response.TeacherResponse;
import com.projects.coaching_offline_support.teacher.service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    // Todo teacher add is dublicate add validation for adding teacher
    @PostMapping("/completeProfile")
    public ResponseEntity<ApiResponse<TeacherResponse>> completeProfile(@Valid @RequestBody CompleteTeacherProfile request){


        return ResponseEntity.ok(ApiResponse.success(teacherService.completeProfile(request),"Added teacher successfully."));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TeacherCoachingResponse>> addTeacher(@Valid @RequestBody AddTeacherRequest request){

        return ResponseEntity.ok(ApiResponse.success(teacherService.addTeacherToCoaching(request),"Teacher added successfully"));
    }

    @GetMapping("/appoint")
    public ResponseEntity<ApiResponse<Page<TeacherResponse>>> appointTeacher(
            AppointTeacherFilter filter,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int  pageSize,
            @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Sort sort
    ){
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<TeacherResponse> result = teacherService.appointTeacher(filter,pageable);
        return ResponseEntity.ok(ApiResponse.success(result,"Fetched students successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<TeacherResponse>>> getTeachers(
            TeacherFilter filter,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int  pageSize,
            @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Sort sort
    ) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<TeacherResponse> result = teacherService.getTeachers(filter,pageable);
        return ResponseEntity.ok(ApiResponse.success(result,"Fetched teachers successfully"));
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
    @GetMapping("/coaching/{coachingId}")
    public ResponseEntity<ApiResponse<List<TeacherResponse>>> getTeacherByCoaching(@PathVariable UUID coachingId){
        return ResponseEntity.ok(ApiResponse.success(teacherService.getTeacherByCoachingId(coachingId) , "Teacher fetched successfully"));
    }
}
