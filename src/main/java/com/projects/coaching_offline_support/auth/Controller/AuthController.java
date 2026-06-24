package com.projects.coaching_offline_support.auth.Controller;

import com.projects.coaching_offline_support.auth.Services.AuthService;
import com.projects.coaching_offline_support.auth.dtos.SignInResponse;
import com.projects.coaching_offline_support.auth.dtos.SignInReuest;
import com.projects.coaching_offline_support.auth.dtos.SignupRequest;
import com.projects.coaching_offline_support.auth.dtos.SignupResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

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
        ResponseCookie accessToken = ResponseCookie.from("access_token",response.accessToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(15*60)
                .sameSite("Lax")
                .build();

        ResponseCookie refreshToken = ResponseCookie.from("refresh_token",response.refreshToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(7*24*60*60)
                .sameSite("Lax")
                .build();
        return  ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE,accessToken.toString())
                .header(HttpHeaders.SET_COOKIE ,refreshToken.toString())
                .body(response);
    }

    @PostMapping("/refresh")
    public  ResponseEntity<SignInResponse> refreshToken(HttpServletRequest request){
        String token = Arrays.stream(request.getCookies())
                .filter(cookie -> "refresh_token".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(()-> new AuthenticationServiceException("No token found."));

        SignInResponse response = authService.refreshToken(token);

        return  ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE,response.accessToken())
                .header(HttpHeaders.SET_COOKIE ,token)
                .body(response);
    }

}
