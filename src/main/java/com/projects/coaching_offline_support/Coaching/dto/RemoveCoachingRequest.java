package com.projects.coaching_offline_support.Coaching.dto;

import com.projects.coaching_offline_support.Coaching.enums.ReasonToRemoveCoaching;
import jakarta.validation.constraints.NotBlank;

public record RemoveCoachingRequest (
        ReasonToRemoveCoaching reason,
        @NotBlank(message = "Provide reason for closing coaching.")
        String description
) {
}
