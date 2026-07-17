package com.projects.coaching_offline_support.common.components;

import com.projects.coaching_offline_support.common.Exceptions.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public final class RepositoryUtils {

    private RepositoryUtils() {}

    public static <T, ID> T findOrThrow(JpaRepository<T, ID> repository, ID id, String entityName) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(entityName + " not found with id: " + id));
    }
}


