package com.projects.coaching_offline_support.common.dtos;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ApiResponse<T>(
        boolean success,
        String message,
        T data,
        ErrorResponse error,
        LocalDateTime timeStamp
) {
    public  static <T> ApiResponse<T> success (T data, String message){

        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timeStamp(LocalDateTime.now())
                .build();

    }

    public static <T> ApiResponse<T> error(ErrorResponse error){
        return ApiResponse.<T>builder()
                .message(error.message())
                .error(error)
                .timeStamp(LocalDateTime.now())
                .build();
    }

    public static ApiResponse<Void> success(String message){
        return ApiResponse.<Void>builder()
                .success(true)
                .message(message)
                .timeStamp(LocalDateTime.now())
                .build();
    }
}
