package com.projects.coaching_offline_support.auth.Services;

import com.projects.coaching_offline_support.auth.dtos.SignInResponse;
import com.projects.coaching_offline_support.auth.dtos.SignInReuest;
import com.projects.coaching_offline_support.auth.dtos.SignupRequest;
import com.projects.coaching_offline_support.auth.dtos.SignupResponse;
import com.projects.coaching_offline_support.user.User;

public interface AuthService {
    SignupResponse signUp(SignupRequest request);
    SignInResponse signin(SignInReuest request);
    SignInResponse refreshToken(String refreshToken);



}
