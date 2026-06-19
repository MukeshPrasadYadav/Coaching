package com.projects.coaching_offline_support.common.dtos;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ErrorResponse(
         String message,
         HttpStatus status,
         LocalDateTime timeStamp
) {
}
