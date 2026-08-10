package com.projects.coaching_offline_support.common.Service.impl;


import com.projects.coaching_offline_support.common.Service.FileService;
import com.projects.coaching_offline_support.common.config.S3Config;
import com.projects.coaching_offline_support.common.dtos.requests.PreSignedUrlRequest;
import com.projects.coaching_offline_support.common.dtos.respnse.PreSignedUrlResponse;
import com.projects.coaching_offline_support.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.*;

import java.time.Duration;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileService {

    private final S3Config s3Config;

    @Value("${aws.bucketName}")
    private  String bucketName;



    @Override
    public String getProfilePicture(String s3Key) {

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(15))
                .getObjectRequest(getObjectRequest)
                .build();

        PresignedGetObjectRequest presignedRequest =
                s3Config.s3Presigner().presignGetObject(presignRequest);

        return presignedRequest.url().toString();
    }

    @Override
    public PreSignedUrlResponse generatePreSignedUrl(PreSignedUrlRequest request) {

        String subFolder = request.subFolder();
        String fileName = request.fileName();
        String contentType = request.contentType();

        String key = getUrlPath(subFolder,fileName);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(contentType)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(5))
                .putObjectRequest(putObjectRequest)
                .build();

        PresignedPutObjectRequest presignedPutObjectRequest = s3Config.s3Presigner().presignPutObject(presignRequest);
        String url = presignedPutObjectRequest.url().toString();
        return new PreSignedUrlResponse(key,url);
    }

    // Todo configure some way to upload not only profile picture but also different files
    private String getUrlPath(String subFolder, String fileName) {
        User user = CurrentUser.get();
        return user.getRole().name() + "/" + user.getId() + "/" + subFolder + "/" + fileName;
    }
}
