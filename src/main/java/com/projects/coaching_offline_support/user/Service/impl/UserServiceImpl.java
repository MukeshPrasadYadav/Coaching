package com.projects.coaching_offline_support.user.Service.impl;


import com.projects.coaching_offline_support.Coaching.repository.CoachingRepository;
import com.projects.coaching_offline_support.audit.entity.Auditable;
import com.projects.coaching_offline_support.audit.enums.ActionType;
import com.projects.coaching_offline_support.audit.enums.LogType;

import com.projects.coaching_offline_support.auth.dtos.UserDetailsRepsonse.*;
import com.projects.coaching_offline_support.common.Exceptions.ResourceNotFoundException;
import com.projects.coaching_offline_support.common.Service.FileService;
import com.projects.coaching_offline_support.common.Service.impl.CurrentUser;
import com.projects.coaching_offline_support.common.components.RepositoryUtils;
import com.projects.coaching_offline_support.common.enums.Role;
import com.projects.coaching_offline_support.student.entity.Student;
import com.projects.coaching_offline_support.student.repository.StudentRepository;
import com.projects.coaching_offline_support.teacher.entity.Teacher;
import com.projects.coaching_offline_support.teacher.repository.TeacherRepository;
import com.projects.coaching_offline_support.user.Service.UserService;
import com.projects.coaching_offline_support.user.User;
import com.projects.coaching_offline_support.user.UserRepository;
import com.projects.coaching_offline_support.user.dto.request.UpdateProfilPictureRequest;
import com.projects.coaching_offline_support.user.dto.request.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final FileService fileService;
    private CoachingRepository coachingRepository;

    // add method for student and parent

    @Override
    @Transactional
    @Auditable(
            logType = LogType.USER,
            actionType = ActionType.PROFILE_COMPLETED,
            description = "user #{#request.email} has completed profile."
    )
    public void updateUser(UUID userId, UserUpdateRequest request) {
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
        if( user.getAddress() != null && !user.getAddress().equals(request.address())){
            user.setAddress(request.address());
        }
        userRepository.save(user);

    }

    @Override
    @Transactional

    public UserDetail uploadProfile(UpdateProfilPictureRequest request) {
        User user = CurrentUser.get();
        Teacher teacher = null;
        Student student = null;
        if(user.getRole().equals(Role.TEACHER)){
            teacher = RepositoryUtils.findOrThrowById(teacherRepository,user.getId(),"Teacher");
        }
        if(user.getRole().equals(Role.STUDENT)){

          student = RepositoryUtils.findOrThrowById(studentRepository,user.getId(),"Student");
        }
        user.setProfilePic(request.s3Url());

        String profilePic = fileService.getProfilePicture(user.getProfilePic());
        userRepository.save(user);
        return switch (user.getRole()){
            case ADMIN -> AdminUserDetails.from(user,profilePic);
            case TEACHER -> TeacherUserDetails.from(teacher,profilePic);
            case STUDENT -> StudentUserDetails.from(student,profilePic);

            case PARENT -> null;
        };
    }
}
