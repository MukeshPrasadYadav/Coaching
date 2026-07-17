package com.projects.coaching_offline_support.student.controller;

import com.projects.coaching_offline_support.common.dtos.ApiResponse;
import com.projects.coaching_offline_support.common.dtos.ExcelColumn;
import com.projects.coaching_offline_support.student.dto.request.AddStudent;
import com.projects.coaching_offline_support.student.dto.request.StudentFilter;
import com.projects.coaching_offline_support.student.dto.response.StudentResponse;
import com.projects.coaching_offline_support.student.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/v1/students")
public class StudentController {

    private final StudentService studentService;

//    @PostMapping
//    public ResponseEntity<ApiResponse<Void>> addStudent(){
//
//    }

    @PostMapping
    public ResponseEntity<ApiResponse<StudentResponse>> addStudent(@Valid @RequestBody AddStudent request){

        return ResponseEntity.ok(ApiResponse.success(studentService.addStudent(request) ,"Added student sucess fully"));
    }

    @GetMapping
    public  ResponseEntity<ApiResponse<Page<StudentResponse>>> getAllStudent(StudentFilter filter, Pageable pageable){
        Page<StudentResponse> students = studentService.getStudents(filter,pageable);
        return ResponseEntity.ok(ApiResponse.success(students,"Fetched students successfully"));
    }

//    @GetMapping("/export")
//    public ResponseEntity<InputStreamResource> exportStudents(
//            StudentFilter filter
//    ) throws IOException {
//
//        List<StudentResponse> students = studentService.getStudentsForExport(filter);
//
//        ByteArrayInputStream stream = excelExportService.export(
//                "Students",
//                List.of(
//                        new ExcelColumn<>("Name", StudentResponse::getName),
//                        new ExcelColumn<>("Class", StudentResponse::getClassStd),
//                        new ExcelColumn<>("Batch", StudentResponse::getBatch),
//                        new ExcelColumn<>("Phone", StudentResponse::getPhone)
//                ),
//                students,
//                true
//        );
//
//        return fileDownloadService.excel("students", stream);
//    }
}
