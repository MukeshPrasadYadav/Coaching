package com.projects.coaching_offline_support.student.repository;

import com.projects.coaching_offline_support.student.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, UUID> , JpaSpecificationExecutor<Student> {
}
