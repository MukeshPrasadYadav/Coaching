package com.projects.coaching_offline_support.student.service;

import com.projects.coaching_offline_support.student.dto.request.AddStudent;
import com.projects.coaching_offline_support.student.dto.request.CompleteStudentProfileRequest;
import com.projects.coaching_offline_support.student.dto.request.StudentFilter;
import com.projects.coaching_offline_support.student.dto.request.UpdateStudentDetailRequest;
import com.projects.coaching_offline_support.student.dto.response.StudentCoachingResponse;
import com.projects.coaching_offline_support.student.dto.response.StudentDetail;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface StudentService  {
    StudentCoachingResponse addStudent(AddStudent request);


    Page<StudentCoachingResponse> getStudents(StudentFilter filter, Pageable pageable);

    ByteArrayInputStream exportStudents(StudentFilter filter) throws IOException;

    void completeProfile(CompleteStudentProfileRequest request);


    StudentDetail getStudentById(UUID studentId);
}
