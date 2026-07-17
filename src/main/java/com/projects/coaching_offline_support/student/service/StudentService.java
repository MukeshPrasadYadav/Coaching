package com.projects.coaching_offline_support.student.service;

import com.projects.coaching_offline_support.student.dto.request.AddStudent;
import com.projects.coaching_offline_support.student.dto.request.StudentFilter;
import com.projects.coaching_offline_support.student.dto.response.StudentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudentService  {
    StudentResponse addStudent(AddStudent request);


    Page<StudentResponse> getStudents(StudentFilter filter, Pageable pageable);
}
