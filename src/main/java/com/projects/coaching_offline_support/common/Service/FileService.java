package com.projects.coaching_offline_support.common.Service;

import com.projects.coaching_offline_support.common.dtos.requests.PreSignedUrlRequest;
import com.projects.coaching_offline_support.common.dtos.respnse.PreSignedUrlResponse;

import java.util.Map;

public interface FileService {
    String getProfilePicture(String s3Key);

    PreSignedUrlResponse generatePreSignedUrl(PreSignedUrlRequest request);
}
