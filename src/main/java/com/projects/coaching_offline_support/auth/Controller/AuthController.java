package com.projects.coaching_offline_support.auth.Controller;

import com.projects.coaching_offline_support.auth.Services.AuthService;
import com.projects.coaching_offline_support.auth.dtos.*;
import com.projects.coaching_offline_support.common.dtos.ApiResponse;
import com.projects.coaching_offline_support.common.utils.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignupResponse>> signup(@RequestBody SignupRequest request){
        return ResponseEntity.ok(ApiResponse.success(authService.signUp(request),"Registered successfully."));
    }

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<Void>> signin(@RequestBody SignInReuest request,HttpServletResponse servletResponse) throws BadRequestException {
        SignInResponse response = authService.signin(request);

        return  ResponseEntity.ok()
                .header(
                        HttpHeaders.SET_COOKIE,
                        CookieUtils.createAccessTokenCookie(response.accessToken()).toString()

                )
                .header(
                        HttpHeaders.SET_COOKIE,
                        CookieUtils.createRefreshTokenCookie(response.refreshToken()).toString()
                )
                .body(ApiResponse.success("Logged in successfully."));
    }

    @PostMapping("/refresh")
    public  ResponseEntity<ApiResponse<SignInResponse>> refreshToken(HttpServletRequest request){


        String token = Arrays.stream(request.getCookies())
                .filter(cookie -> "refresh_token".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(()-> new AuthenticationServiceException("No token found."));
        SignInResponse response = authService.refreshToken(token);
        return ResponseEntity.noContent()
                .header(
                        HttpHeaders.SET_COOKIE,
                        CookieUtils.createAccessTokenCookie(response.accessToken()).toString()
                )
                .build();
    }

    @GetMapping("/get/me")
    public ResponseEntity<ApiResponse<UserDetail>> getMe(){

        return  ResponseEntity.ok(ApiResponse.success(authService.getMe(),"Refetched successfully."));
    }

    @PostMapping("/signout")
    public  ResponseEntity<ApiResponse<Void>> signOut(HttpServletRequest request, HttpServletResponse response){
        authService.signOut(request,response);
        return  ResponseEntity.ok(ApiResponse.success("Logged out successfully.")) ;
    }

}
