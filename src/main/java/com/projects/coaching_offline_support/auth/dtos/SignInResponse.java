package com.projects.coaching_offline_support.auth.dtos;

public record SignInResponse(
        String accessToken,
        String refreshToken) {
}
