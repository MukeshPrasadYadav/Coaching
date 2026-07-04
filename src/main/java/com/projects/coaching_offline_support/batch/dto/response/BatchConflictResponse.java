package com.projects.coaching_offline_support.batch.dto.response;

import com.projects.coaching_offline_support.common.entity.Timing;
import com.projects.coaching_offline_support.common.enums.DaysOfWeek;

public record BatchConflictResponse(
        String batchName,
        String day,
        String timing
) {
}
