package com.projects.coaching_offline_support.Coaching.repository;

import com.projects.coaching_offline_support.Coaching.entity.Coaching;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CoachingRepository extends JpaRepository<Coaching, UUID> {
    boolean existsByCoachingName(String name);

    Coaching findByUser_Email(String email);
}
