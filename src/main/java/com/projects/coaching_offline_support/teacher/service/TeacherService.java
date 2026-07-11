package com.projects.coaching_offline_support.teacher.service;

import com.projects.coaching_offline_support.teacher.dto.request.RegisterTeacherRequest;
import com.projects.coaching_offline_support.teacher.dto.response.TeacherResponse;
import jakarta.validation.Valid;

import java.util.UUID;

public interface TeacherService {
    void add(@Valid RegisterTeacherRequest request);

    TeacherResponse getTeacherById(UUID teacherId);
}
