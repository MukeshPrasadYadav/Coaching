package com.projects.coaching_offline_support.common.dtos;

import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ErrorResponse(
         String message,
         HttpStatus status,
         LocalDateTime timeStamp,
         List<String> subErrors
) {
}
