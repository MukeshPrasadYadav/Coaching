package com.projects.coaching_offline_support.auth.dtos;

public record SignupRequest(
         String password,
         String name,
         String contactNumber,
         String role

) {
}
