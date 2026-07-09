package com.projects.coaching_offline_support.teacher.service.impl;

import com.projects.coaching_offline_support.teacher.dto.request.AddTeacherRequest;
import com.projects.coaching_offline_support.teacher.entity.Teacher;
import com.projects.coaching_offline_support.teacher.repository.TeacherRepository;
import com.projects.coaching_offline_support.teacher.service.TeacherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;

    @Override
    public void add(AddTeacherRequest request) {

        Teacher teacher = Teacher.builder()
                .fee(request.fee())
                .degrees(request.degrees())
                .subjects(request.subjects()) // todo check for coaching add and availity if present
                .build();

        teacherRepository.save(teacher);
    }


    }