package com.projects.coaching_offline_support.teacher.repository;

import com.projects.coaching_offline_support.teacher.entity.CoachingTeacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CoachingTeacherRepository extends JpaRepository<CoachingTeacher, UUID> {
}
