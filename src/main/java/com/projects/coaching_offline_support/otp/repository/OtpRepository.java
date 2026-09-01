package com.projects.coaching_offline_support.otp.repository;

import com.projects.coaching_offline_support.otp.entity.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OtpRepository extends JpaRepository<OTP, UUID> {
}
