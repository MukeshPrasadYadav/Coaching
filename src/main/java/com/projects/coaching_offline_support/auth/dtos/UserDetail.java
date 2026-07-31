package com.projects.coaching_offline_support.auth.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.projects.coaching_offline_support.common.components.RepositoryUtils;
import com.projects.coaching_offline_support.common.entity.Address;
import com.projects.coaching_offline_support.common.enums.Gender;
import com.projects.coaching_offline_support.common.enums.Permission;
import com.projects.coaching_offline_support.common.enums.Role;
import com.projects.coaching_offline_support.student.entity.Student;
import com.projects.coaching_offline_support.student.repository.StudentRepository;
import com.projects.coaching_offline_support.teacher.dto.response.TeacherResponse;
import com.projects.coaching_offline_support.teacher.entity.Teacher;
import com.projects.coaching_offline_support.user.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserDetail(
        UUID id,
        String name,
        String email,
        String contactNumber,
        Role role,
        boolean isProfileCompleted,
        Address address,
        String motherName,
        String fatherName,
        String guardianName,
        String guardianPhone,
        String guardianEmail,
        LocalDate dob,
        Gender gender,
        Set<String> degress,
        Integer experience
) {


    public static UserDetail forAdmin(User user){
        return new UserDetail(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getContactNumber(),
                user.getRole(),
                user.isProfileCompleted(),
                user.getAddress(),
                null,
                null,
                null,
                null,
                null,
                user.getDob(),
                user.getGender(),
                null,
                null

        );
    }

    public static UserDetail forStudent(Student student){

        return new UserDetail(
               student.getUser().getId(),
               student.getUser().getName(),
                student.getUser().getEmail(),
                student.getUser().getContactNumber(),
                Role.STUDENT,
                student.getUser().isProfileCompleted(),
                student.getUser().getAddress(),
                student.getMotherName(),
                student.getFatherName(),
                student.getParentName(),
                student.getParentNumber(),
                student.getParentEmail(),
                student.getUser().getDob(),
                student.getUser().getGender(),
                null,
                null
        );
    }

    public static UserDetail forTeacher(Teacher teacher){
        return new UserDetail(
                teacher.getId(),
                teacher.getUser().getName(),
                teacher.getUser().getEmail(),
                teacher.getUser().getContactNumber(),
                Role.TEACHER,
                teacher.getUser().isProfileCompleted(),
                teacher.getUser().getAddress(),
                null,
                null,
                null,
                null,
                null,
                teacher.getUser().getDob(),
                teacher.getUser().getGender(),
                teacher.getDegrees(),
                teacher.getExperience()
        );
    }
}
