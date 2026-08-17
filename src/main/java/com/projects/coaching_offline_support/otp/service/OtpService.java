package com.projects.coaching_offline_support.otp.service;

import com.projects.coaching_offline_support.otp.dto.request.OtpRequest;
import com.projects.coaching_offline_support.otp.enums.OtpPurpose;

public interface OtpService {
    String generateAndSend(OtpRequest request  );
}
