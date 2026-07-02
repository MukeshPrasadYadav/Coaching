package com.projects.coaching_offline_support.Coaching.dto;

import com.projects.coaching_offline_support.common.entity.Address;
import jakarta.persistence.Embedded;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AddCoachingRequest(
        @NotNull(message = "Name of coaching is required")
        @Size(min = 5,max = 50, message = "Coaching name must be between 5 to 50 characters")
        String name,

        @NotNull(message = "Name is required")
        @Size(min = 5,max = 20, message = "Name must be between 5 to 20 characters")
        String ownerName,

        @NotNull(message = "Address of coaching is required")
        @Valid
        Address address,

        @NotNull(message = "Contact number is required")
        @Size(min = 10,max = 10, message = "Contact number must be 10 digit")
        String ownerContactNumber,

        @NotNull(message = "Email is required")
        @Email
        String ownerEmail
) {
}
