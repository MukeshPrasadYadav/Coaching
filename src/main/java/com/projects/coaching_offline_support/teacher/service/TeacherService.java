package com.projects.coaching_offline_support.teacher.service;

import com.projects.coaching_offline_support.teacher.dto.request.*;
import com.projects.coaching_offline_support.teacher.dto.response.AppointTeacherResponse;
import com.projects.coaching_offline_support.teacher.dto.response.TeacherCoachingResponse;
import com.projects.coaching_offline_support.teacher.dto.response.TeacherResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface TeacherService {
    TeacherResponse completeProfile(@Valid CompleteTeacherProfile request);

    TeacherResponse getTeacherById(UUID teacherId);

    TeacherCoachingResponse addTeacherToCoaching(@Valid AddTeacherRequest request);

    Page<TeacherResponse> getTeachers(TeacherFilter filter, Pageable pageable);

    ByteArrayInputStream exportTeachers(TeacherFilter filter) throws IOException;

    Page<AppointTeacherResponse> appointTeacher(AppointTeacherFilter filter, Pageable pageable);

    List<TeacherResponse> getTeacherByCoachingId(UUID coachingId);
}
