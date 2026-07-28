package com.projects.coaching_offline_support.student.specification;

import com.projects.coaching_offline_support.Coaching.entity.Coaching;
import com.projects.coaching_offline_support.batch.entity.Batch;
import com.projects.coaching_offline_support.common.Service.impl.CurrentUser;
import com.projects.coaching_offline_support.student.dto.request.StudentFilter;
import com.projects.coaching_offline_support.student.entity.Student;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StudentSpecification {

    public static Specification<Student> filter(StudentFilter filter) {

        return (root, query, cb) -> {

            UUID coachingId = CurrentUser.get().getId();

            assert query != null;
            query.distinct(true);

            List<Predicate> predicates = new ArrayList<>();

            // Student -> Batch
            Join<Student, Batch> batchJoin = root.join("batches");

            // Batch -> Coaching
            Join<Batch, Coaching> coachingJoin = batchJoin.join("coaching");

            // Only students of the logged-in coaching
            predicates.add(
                    cb.equal(coachingJoin.get("id"), coachingId)
            );

            // Search by student name
            if (filter.search() != null && !filter.search().trim().isEmpty()) {

                String keyword = "%" + filter.search().trim().toLowerCase() + "%";

                predicates.add(
                        cb.like(
                                cb.lower(root.get("name")),
                                keyword
                        )
                );
            }

            // From Date
            if (filter.fromDate() != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("createdAt"),
                                filter.fromDate()
                        )
                );
            }

            // To Date
            if (filter.toDate() != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(
                                root.get("createdAt"),
                                filter.toDate()
                        )
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}