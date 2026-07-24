package com.projects.coaching_offline_support.audit.controller;


import com.projects.coaching_offline_support.audit.entity.AuditLog;
import com.projects.coaching_offline_support.audit.service.AuditLogService;
import com.projects.coaching_offline_support.common.dtos.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/logs")
@RequiredArgsConstructor
public class AuditController {
    private final AuditLogService auditLogService;

//    @GetMapping
//    public ResponseEntity<ApiResponse<Page<AuditLog>>> getAllAudits(){
//        return ResponseEntity.of();
//    }
}
