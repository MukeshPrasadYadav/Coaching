package com.projects.coaching_offline_support.batch.specification;

import com.projects.coaching_offline_support.Coaching.entity.Coaching;
import com.projects.coaching_offline_support.batch.dto.request.BatchFilter;
import com.projects.coaching_offline_support.batch.entity.Batch;
import com.projects.coaching_offline_support.batch.entity.BatchSchedule;
import com.projects.coaching_offline_support.common.enums.Role;
import com.projects.coaching_offline_support.student.entity.Student;
import com.projects.coaching_offline_support.teacher.entity.Teacher;
import com.projects.coaching_offline_support.user.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class BatchSpecification {

    public static Specification<Batch> filter(BatchFilter filter, User user) {

        return (root, query, cb) -> {

            assert query != null;
            query.distinct(true);

            List<Predicate> predicates = new ArrayList<>();


            Join<Batch, Coaching> coachingJoin = root.join("coaching");

            // Role based visibility
            switch (user.getRole()) {

                case ADMIN -> {

                    predicates.add(
                            cb.equal(coachingJoin.get("id"), user.getId())
                    );
                }

                case STUDENT -> {
                    Join<Batch, Student> studentJoin = root.join("students");
                    predicates.add(
                            cb.equal(studentJoin.get("id"), user.getId())
                    );
                }

                case TEACHER -> {
                    Join<Batch, BatchSchedule> scheduleJoin = root.join("schedules", JoinType.LEFT);
                    Join<BatchSchedule, Teacher> teacherJoin = scheduleJoin.join("teacher", JoinType.LEFT);
                    predicates.add(
                            cb.equal(teacherJoin.get("id"), user.getId())
                    );
                }
            }

            // Search
            if (filter.search() != null && !filter.search().isBlank()) {
                String keyword = "%" + filter.search().toLowerCase() + "%";

                predicates.add(
                        cb.or(
                                cb.like(cb.lower(root.get("name")), keyword),
                                cb.like(cb.lower(coachingJoin.get("coachingName")), keyword)
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

            // Status
            if (filter.status() != null) {
                predicates.add(
                        cb.equal(root.get("status"), filter.status())
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}