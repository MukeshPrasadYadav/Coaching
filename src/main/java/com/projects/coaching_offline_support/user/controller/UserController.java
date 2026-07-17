package com.projects.coaching_offline_support.user.controller;

import com.projects.coaching_offline_support.common.dtos.ApiResponse;
import com.projects.coaching_offline_support.user.Service.UserService;
import com.projects.coaching_offline_support.user.dto.request.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PatchMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> updateBasicInfo(@PathVariable UUID userId, @RequestBody UserUpdateRequest request){
         userService.updateUser(userId,request);
        return ResponseEntity.ok(ApiResponse.success("User updated successfully"));
    }
}
