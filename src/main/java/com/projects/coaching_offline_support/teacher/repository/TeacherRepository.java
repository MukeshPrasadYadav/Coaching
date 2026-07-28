package com.projects.coaching_offline_support.teacher.repository;

import com.projects.coaching_offline_support.teacher.dto.response.TeacherResponse;
import com.projects.coaching_offline_support.teacher.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, UUID>, JpaSpecificationExecutor<Teacher> {
    Teacher findByUserEmail(String email);


    @Query("""
    SELECT DISTINCT t
    FROM Teacher t
    JOIN t.coachingLinks ct
    WHERE ct.coaching.id = :coachingId
""")
    List<Teacher> findByCoachingId(UUID coachingId);
}
