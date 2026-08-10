package com.projects.coaching_offline_support.auth.dtos.UserDetailsRepsonse;

import com.projects.coaching_offline_support.common.entity.Address;
import com.projects.coaching_offline_support.common.enums.Gender;
import com.projects.coaching_offline_support.common.enums.Role;
import com.projects.coaching_offline_support.user.User;

import java.time.LocalDate;
import java.util.UUID;

public record UserInfo(
        UUID id,
        String name,
        String email,
        String contactNumber,
        Role role,
        boolean isProfileCompleted,
        Address address,
        LocalDate dob,
        Gender gender,
        String profile_picture
        ) {

    public static UserInfo from (User user , String profile_picture){
        return new UserInfo(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getContactNumber(),
                user.getRole(),
                user.isProfileCompleted(),
                user.getAddress(),
                user.getDob(),
                user.getGender(),
                profile_picture
        );

    }
}
