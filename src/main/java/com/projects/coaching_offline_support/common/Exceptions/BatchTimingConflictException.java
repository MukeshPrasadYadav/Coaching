package com.projects.coaching_offline_support.common.Exceptions;

import com.projects.coaching_offline_support.batch.dto.response.BatchConflictResponse;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
@Getter
public class BatchTimingConflictException extends  RuntimeException{

    private  final List<BatchConflictResponse> coflicts;
    public BatchTimingConflictException(String message, List<BatchConflictResponse> conflicts){

        super(message);
        this.coflicts = conflicts != null ? conflicts : new ArrayList<>();
    }
}
