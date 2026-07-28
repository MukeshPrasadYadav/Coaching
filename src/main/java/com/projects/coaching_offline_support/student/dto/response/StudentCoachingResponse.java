package com.projects.coaching_offline_support.student.dto.response;

import com.projects.coaching_offline_support.batch.dto.response.BatchInfo;
import com.projects.coaching_offline_support.batch.entity.Batch;
import com.projects.coaching_offline_support.common.Service.impl.CurrentUser;
import com.projects.coaching_offline_support.student.entity.Student;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record StudentCoachingResponse(
        UUID id,
        String name,
        List<StudentBatchResponse> batches,
        LocalDateTime joiningDate
) {
    public  static StudentCoachingResponse fromEntity(Student student){
     return  new StudentCoachingResponse(
             student.getId(),
             student.getUser().getName(),
             student.getBatches().stream().map(
                     StudentBatchResponse::fromEntity
             ).toList(),
             student.getCreatedAt()
     );
    }
}
