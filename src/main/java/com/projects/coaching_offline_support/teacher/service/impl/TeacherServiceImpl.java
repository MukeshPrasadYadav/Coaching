package com.projects.coaching_offline_support.teacher.service.impl;

import com.projects.coaching_offline_support.common.Exceptions.ResourceNotFoundException;
import com.projects.coaching_offline_support.common.entity.Address;
import com.projects.coaching_offline_support.teacher.dto.request.RegisterTeacherRequest;
import com.projects.coaching_offline_support.teacher.dto.response.TeacherResponse;
import com.projects.coaching_offline_support.teacher.entity.Teacher;
import com.projects.coaching_offline_support.teacher.repository.TeacherRepository;
import com.projects.coaching_offline_support.teacher.service.TeacherService;
import com.projects.coaching_offline_support.user.User;
import com.projects.coaching_offline_support.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void add(RegisterTeacherRequest request) {
        User user = userRepository.findByEmail(request.email()).orElseThrow(() -> new ResourceNotFoundException("No user found."));

        user.setContactNumber(request.contactNumber());
        user.setProfileCompleted(true);
      User savedUser =  userRepository.save(user);

        Teacher teacher = Teacher.builder()
                .fee(request.fee())
                .degrees(request.degrees())
                .subjects(request.subjects())
                .address(request.address())
                .user(savedUser)
                .build();

        teacherRepository.save(teacher);
    }

    @Override
    public TeacherResponse getTeacherById(UUID teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found."));

        return new TeacherResponse(
                teacher.getDegrees(),
                teacher.getSubjects(),
                teacher.getFee(),
                Address.builder()
                        .country(teacher.getAddress().getCountry())
                        .state(teacher.getAddress().getState())
                        .area(teacher.getAddress().getArea())
                        .postOffice(teacher.getAddress().getPostOffice())
                        .pinCode(teacher.getAddress().getPinCode())
                        .houseNo(teacher.getAddress().getHouseNo())
                        .city(teacher.getAddress().getCity())
                        .building(teacher.getAddress().getBuilding())
                        .build());
    }


}