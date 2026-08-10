package com.projects.coaching_offline_support.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateProfilPictureRequest(
        @NotBlank
        String s3Url
) {
}
