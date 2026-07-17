package com.projects.coaching_offline_support.user.Service;

import com.projects.coaching_offline_support.user.dto.request.UserUpdateRequest;

import java.util.UUID;

public interface UserService {
    void updateUser(UUID userId, UserUpdateRequest request);
}
