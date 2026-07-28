package com.projects.coaching_offline_support.batch.repository;

import com.projects.coaching_offline_support.batch.dto.request.BatchFilter;
import com.projects.coaching_offline_support.batch.dto.response.BatchInfo;
import com.projects.coaching_offline_support.batch.entity.Batch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BatchRepository extends JpaRepository<Batch, UUID> , JpaSpecificationExecutor<Batch> {

    List<Batch> findByCoaching_Id(UUID id);
}
