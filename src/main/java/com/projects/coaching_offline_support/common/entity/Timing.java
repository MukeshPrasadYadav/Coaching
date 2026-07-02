package com.projects.coaching_offline_support.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.LocalDateTime;

@Embeddable
public class Timing {

    @Column(nullable = false)
    private LocalDateTime from;

    @Column(nullable = false)
    private LocalDateTime to;
}
