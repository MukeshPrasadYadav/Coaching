package com.projects.coaching_offline_support.batch.dto.response;

import com.projects.coaching_offline_support.batch.enums.BatchStatus;
import com.projects.coaching_offline_support.common.entity.Timing;
import com.projects.coaching_offline_support.common.enums.DaysOfWeek;

import java.util.Map;
import java.util.UUID;

public record BatchInfo(
        UUID id,
        String batchName,
        String teacherName,
        Map<DaysOfWeek,Timing> timing,
        String coachingName,
        BatchStatus status
) {
}
