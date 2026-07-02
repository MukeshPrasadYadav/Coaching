package com.projects.coaching_offline_support.common.Service.impl;

import com.projects.coaching_offline_support.user.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

public class AuditorAwareImpl implements AuditorAware<UUID> {
    @Override
    public Optional<UUID> getCurrentAuditor() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        User user = (User) authentication.getPrincipal();

        return Optional.of(user.getId());
    }
}
