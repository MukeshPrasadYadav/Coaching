package com.projects.coaching_offline_support.batch.dto.request;

import com.projects.coaching_offline_support.batch.enums.BatchStatus;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public record BatchFilter(
        UUID coachingId,
        String coachingName,
        String search,
        LocalDateTime fromDate,
        LocalDateTime toDate,
        BatchStatus status
) {
}
