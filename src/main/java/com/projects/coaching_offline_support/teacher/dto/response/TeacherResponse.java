package com.projects.coaching_offline_support.teacher.dto.response;

import com.projects.coaching_offline_support.common.entity.Address;

import java.math.BigDecimal;
import java.util.List;

public record TeacherResponse(
        List<String> degrees,
        List<String> subjects,
        BigDecimal fee) {
}
