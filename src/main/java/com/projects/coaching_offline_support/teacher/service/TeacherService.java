package com.projects.coaching_offline_support.teacher.service;

import com.projects.coaching_offline_support.teacher.dto.request.AddTeacherRequest;
import jakarta.validation.Valid;

public interface TeacherService {
    Void add(@Valid AddTeacherRequest request);
}
