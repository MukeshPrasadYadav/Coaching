package com.projects.coaching_offline_support.auth.Controller;

import com.projects.coaching_offline_support.auth.Services.AuthService;
import com.projects.coaching_offline_support.auth.dtos.SignInResponse;
import com.projects.coaching_offline_support.auth.dtos.SignInReuest;
import com.projects.coaching_offline_support.auth.dtos.SignupRequest;
import com.projects.coaching_offline_support.auth.dtos.SignupResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest request){
        return ResponseEntity.ok(authService.signUp(request));
    }

    @PostMapping("/signin")
    public ResponseEntity<SignInResponse> signin(@RequestBody SignInReuest request){
        SignInResponse response = authService.signin(request);
        ResponseCookie accessToken = ResponseCookie.from("access_token",response.accessToken()).build();

        ResponseCookie refreshToken = ResponseCookie.from("refresh_token",response.refreshToken()).build();
        return  ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE,accessToken.toString())
                .header(HttpHeaders.SET_COOKIE ,refreshToken.toString())
                .body(response);
    }
}
