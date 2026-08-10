package com.projects.coaching_offline_support.student.dto.response;

import com.projects.coaching_offline_support.auth.dtos.UserDetailsRepsonse.UserInfo;
import com.projects.coaching_offline_support.common.Service.impl.FileUploadServiceImpl;
import com.projects.coaching_offline_support.common.entity.Address;
import com.projects.coaching_offline_support.student.entity.Student;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.time.LocalDate;

public record StudentDetail(
        String name,
        String profile_picture,
        String contactNumber,
        String email,
        String guardianPhone,
        String guardianEmail,
        LocalDate dob,
        String address
      //  LocalDate admissionDate
) {



    public static StudentDetail forAdmin(Student student, String profile_picture){

        Address address = student.getUser().getAddress();
        String addressString = String.join(", ",
                address.getCity(),
                address.getState(),
                address.getCountry()
        );

        return new StudentDetail(
                student.getUser().getName(),
                profile_picture,
                student.getUser().getContactNumber(),
                student.getUser().getEmail(),
                student.getParentNumber(),
                student.getParentEmail(),
                student.getUser().getDob(),
                addressString
        );

    }
}
