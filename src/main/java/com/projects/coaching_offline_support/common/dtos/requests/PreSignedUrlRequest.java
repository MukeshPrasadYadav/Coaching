package com.projects.coaching_offline_support.common.dtos.requests;

import jakarta.validation.constraints.NotBlank;

public record PreSignedUrlRequest(
        @NotBlank(message = "subFolder is required")
        String subFolder,
        @NotBlank(message = "file name is required")
        String fileName,
        @NotBlank(message = "content Type is required")
        String contentType
) {
}
