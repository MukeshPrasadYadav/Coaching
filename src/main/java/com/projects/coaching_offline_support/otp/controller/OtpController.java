package com.projects.coaching_offline_support.otp.controller;


import com.projects.coaching_offline_support.common.dtos.ApiResponse;
import com.projects.coaching_offline_support.otp.dto.request.OtpRequest;
import com.projects.coaching_offline_support.otp.enums.OtpPurpose;
import com.projects.coaching_offline_support.otp.service.OtpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/otp")
@RequiredArgsConstructor
public class OtpController {

    private final OtpService otpService;


    @PostMapping()
    public ResponseEntity<ApiResponse<String>> sendOtp(@Valid @RequestBody OtpRequest request){

        return  ResponseEntity.ok(ApiResponse.success(otpService.generateAndSend(request),"Otp send for ${purpose} successfully"));
    }
}
