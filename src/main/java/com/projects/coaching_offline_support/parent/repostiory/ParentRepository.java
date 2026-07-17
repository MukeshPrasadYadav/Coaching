package com.projects.coaching_offline_support.parent.repostiory;

import com.projects.coaching_offline_support.parent.entity.Parent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ParentRepository extends JpaRepository<Parent, UUID> {
}
