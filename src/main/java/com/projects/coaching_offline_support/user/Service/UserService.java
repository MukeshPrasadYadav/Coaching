package com.projects.coaching_offline_support.user.Service;

import com.projects.coaching_offline_support.user.dto.request.BasicInfoUpdateRequests;

import java.util.UUID;

public interface UserService {
    void updateBasicInfo(UUID userId, BasicInfoUpdateRequests request);
}
