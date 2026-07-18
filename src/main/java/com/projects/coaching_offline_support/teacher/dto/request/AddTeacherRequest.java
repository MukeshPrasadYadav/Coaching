package com.projects.coaching_offline_support.teacher.dto.request;

import com.projects.coaching_offline_support.common.entity.Address;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public record AddTeacherRequest(
        String name,
        String email,
        String contactNumber,
        Address address,
        Set<String> batches,
        Set<String> subjects,
        Set<String> degrees,
        Integer experience
        
) {
}
