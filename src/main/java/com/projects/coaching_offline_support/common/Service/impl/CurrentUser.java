package com.projects.coaching_offline_support.common.Service.impl;

import com.projects.coaching_offline_support.auth.dtos.UserDetailsRepsonse.AdminUserDetails;
import com.projects.coaching_offline_support.auth.dtos.UserDetailsRepsonse.UserDetail;
import com.projects.coaching_offline_support.user.User;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class CurrentUser {

  

    private CurrentUser() {
    }

    public static User get() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            throw new IllegalStateException("No authenticated user found");
        }

        return (User) authentication.getPrincipal();
    }

    public static UserDetail detail() {
        User user = get();

        return AdminUserDetails.from(user,null);

    }
}