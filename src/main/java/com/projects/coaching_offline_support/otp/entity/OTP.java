package com.projects.coaching_offline_support.otp.entity;

import com.projects.coaching_offline_support.otp.enums.OtpPurpose;
import com.projects.coaching_offline_support.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;


@Entity
@Builder
@Table(name = "otp_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OTP {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private  String otpHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OtpPurpose otpPurpose;

    @Column(nullable = false)
    private  String email;

    @Builder.Default
    private Instant expirestAt = Instant.now().plus(5, ChronoUnit.MINUTES);

    @Builder.Default
    private boolean verified = false;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();


}
