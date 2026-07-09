package com.projects.coaching_offline_support.user.Service.impl;


import com.projects.coaching_offline_support.Coaching.entity.Coaching;
import com.projects.coaching_offline_support.Coaching.repository.CoachingRepository;
import com.projects.coaching_offline_support.common.Exceptions.ResourceNotFoundException;
import com.projects.coaching_offline_support.common.enums.Role;
import com.projects.coaching_offline_support.teacher.entity.Teacher;
import com.projects.coaching_offline_support.teacher.repository.TeacherRepository;
import com.projects.coaching_offline_support.user.Service.UserService;
import com.projects.coaching_offline_support.user.User;
import com.projects.coaching_offline_support.user.UserRepository;
import com.projects.coaching_offline_support.user.dto.request.BasicInfoUpdateRequests;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private CoachingRepository coachingRepository;
    // add method for student and parent

    @Override
    @Transactional
    public void updateBasicInfo(UUID userId, BasicInfoUpdateRequests request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("No user found."));
        if(! user.getName().equals(request.name())){
            user.setName(request.name());
        }
        if(! user.getEmail().equals(request.email())){
            user.setEmail(request.email());
        }
        if( user.getContactNumber() == null ||! user.getContactNumber().equals(request.contactNumber())){
            user.setContactNumber(request.contactNumber());
        }
        userRepository.save(user);
        if(user.getRole().equals(Role.ADMIN)){
            Coaching coaching = coachingRepository.findByOwnerEmail(request.email());
            // Add logic here change ownerName, ownerEmail,ownerContactNumber
        }
        if(user.getRole().equals(Role.TEACHER)){
           Teacher teacher = teacherRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("No teacher found"));
           teacher.setUser(user);
           teacherRepository.save(teacher);
        }
    }
}
