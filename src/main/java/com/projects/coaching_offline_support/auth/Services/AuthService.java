package com.projects.coaching_offline_support.auth.Services;

import com.projects.coaching_offline_support.auth.dtos.*;
import com.projects.coaching_offline_support.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;

public interface AuthService {
    SignupResponse signUp(SignupRequest request);
    SignInResponse signin(SignInReuest request);
    SignInResponse refreshToken(String refreshToken);
    UserDetail getMe();

    void signOut(HttpServletRequest request, HttpServletResponse response);
}
