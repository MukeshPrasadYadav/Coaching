package com.projects.coaching_offline_support.otp.service.impl;


import com.projects.coaching_offline_support.audit.entity.Auditable;
import com.projects.coaching_offline_support.audit.enums.ActionType;
import com.projects.coaching_offline_support.audit.enums.LogType;
import com.projects.coaching_offline_support.common.Exceptions.ResourceNotFoundException;
import com.projects.coaching_offline_support.otp.dto.request.OtpRequest;
import com.projects.coaching_offline_support.otp.entity.OTP;
import com.projects.coaching_offline_support.otp.enums.OtpPurpose;
import com.projects.coaching_offline_support.otp.repository.OtpRepository;
import com.projects.coaching_offline_support.otp.service.OtpService;
import com.projects.coaching_offline_support.user.User;
import com.projects.coaching_offline_support.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.security.SecureRandom;


// change return type to be void


@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final UserRepository userRepository;
    private  final OtpRepository otpRepository;

    @Override
    @Auditable(
            logType = LogType.NOTIFICATION,
            actionType = ActionType.ASSIGNED,
            description = "Otp send to #{#request.email}."
    )
    public String generateAndSend(OtpRequest request) {

        User user = userRepository.findByEmail(request.email()).orElseThrow(() -> new ResourceNotFoundException("No user found"));

        // later hash otp
         SecureRandom secureRandom = new SecureRandom();
        int randomOTP = 100000 + secureRandom.nextInt(900000);
        String generatedOtp = String.valueOf(randomOTP);

        OTP otp = OTP.builder()
                .email(request.email())
                .otpPurpose(request.purpose())
                .otpHash(generatedOtp)
                .build();

        otpRepository.save(otp);


        return generatedOtp;
    }
}
