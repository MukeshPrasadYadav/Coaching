package com.projects.coaching_offline_support.teacher.repository;

import com.projects.coaching_offline_support.teacher.entity.CoachingTeacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CoachingTeacherRepository extends JpaRepository<CoachingTeacher, UUID> {

    @Query("""
SELECT COUNT(ct)
FROM CoachingTeacher ct
WHERE ct.coaching.id = :coachingId
AND ct.active = true
""")
    long countActiveTeachers(UUID coachingId);
}
