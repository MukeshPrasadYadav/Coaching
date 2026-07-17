package com.projects.coaching_offline_support.user.dto.request;

import com.projects.coaching_offline_support.common.entity.Address;

public record UserUpdateRequest(
        String name,
        String email,
        String contactNumber,
        Address address
) {
}
