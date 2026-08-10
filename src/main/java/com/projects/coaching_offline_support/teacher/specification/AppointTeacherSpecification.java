package com.projects.coaching_offline_support.teacher.specification;

import com.projects.coaching_offline_support.Coaching.entity.Coaching;
import com.projects.coaching_offline_support.common.Service.impl.CurrentUser;
import com.projects.coaching_offline_support.teacher.dto.request.AppointTeacherFilter;
import com.projects.coaching_offline_support.teacher.entity.CoachingTeacher;
import com.projects.coaching_offline_support.teacher.entity.Teacher;
import com.projects.coaching_offline_support.teacher.enums.Experience;
import com.projects.coaching_offline_support.user.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AppointTeacherSpecification {

    public static Specification<Teacher> filter(AppointTeacherFilter filter) {

        return (root, query, cb) -> {

            assert query != null;
            query.distinct(true);

            List<Predicate> predicates = new ArrayList<>();

            // Exclude teachers already appointed to the current coaching
            Subquery<CoachingTeacher> subQuery = query.subquery(CoachingTeacher.class);
            Root<CoachingTeacher> coachingTeacher = subQuery.from(CoachingTeacher.class);

            Join<CoachingTeacher, Coaching> coachingJoin = coachingTeacher.join("coaching");
            Join<Coaching, User> userJoin = coachingJoin.join("user");

            subQuery.select(coachingTeacher);

            subQuery.where(
                    cb.equal(coachingTeacher.get("teacher"), root),
                    cb.equal(userJoin.get("id"), CurrentUser.get().getId())
            );

            predicates.add(cb.not(cb.exists(subQuery)));

            // Degree filter
            if (filter.degree() != null && !filter.degree().isBlank()) {
                Join<Teacher, String> degreeJoin = root.join("degrees");
                predicates.add(
                        cb.equal(
                                cb.lower(degreeJoin),
                                filter.degree().toLowerCase()
                        )
                );
            }

            // Subject filter
            if (filter.subject() != null && !filter.subject().isBlank()) {
                Join<Teacher, String> subjectJoin = root.join("subjects");
                predicates.add(
                        cb.equal(
                                cb.lower(subjectJoin),
                                filter.subject().toLowerCase()
                        )
                );
            }

            // Experience filter

            if (filter.experience() != Experience.ALL) {
                switch (filter.experience()) {
                    case Experience.ONE_YEAR_PLUS ->
                            predicates.add(cb.greaterThanOrEqualTo(root.get("experience"), 1));

                    case Experience.TWO_YEAR_PLUS ->
                            predicates.add(cb.greaterThanOrEqualTo(root.get("experience"), 2));

                    case Experience.FIVE_YEAR_PLUS ->
                            predicates.add(cb.greaterThanOrEqualTo(root.get("experience"),  5));

                }
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}