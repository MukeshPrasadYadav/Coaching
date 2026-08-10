package com.projects.coaching_offline_support.common.controller;


import com.projects.coaching_offline_support.common.Service.FileService;
import com.projects.coaching_offline_support.common.dtos.ApiResponse;
import com.projects.coaching_offline_support.common.dtos.requests.PreSignedUrlRequest;
import com.projects.coaching_offline_support.common.dtos.respnse.PreSignedUrlResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.boot.spi.AccessType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping("/getProfile-picture")
    public ResponseEntity<ApiResponse<String>> getUrl(String s3Key){
        return ResponseEntity.ok(ApiResponse.success(fileService.getProfilePicture(s3Key),"fetched profile picture url successfully"));

    }

    @PostMapping("/pre-signed-url")
    public ResponseEntity<ApiResponse<PreSignedUrlResponse>> generatePresignedUrl(@Valid @RequestBody PreSignedUrlRequest request){

        return ResponseEntity.ok(ApiResponse.success(fileService.generatePreSignedUrl(request),"Fetched pre-signed url successfully"));

    }
}
