package com.projects.coaching_offline_support.student.repository;

import com.projects.coaching_offline_support.student.dto.response.StudentCoachingResponse;
import com.projects.coaching_offline_support.student.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> , JpaSpecificationExecutor<Student> {


}
