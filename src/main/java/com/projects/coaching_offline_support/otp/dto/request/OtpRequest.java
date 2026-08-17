package com.projects.coaching_offline_support.otp.dto.request;

import com.projects.coaching_offline_support.otp.enums.OtpPurpose;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OtpRequest(
        @NotBlank(message = "Email for sending otp not found")
        String email,
        @NotNull(message = "Puropse of otp not provided" )
        OtpPurpose purpose
) {
}
