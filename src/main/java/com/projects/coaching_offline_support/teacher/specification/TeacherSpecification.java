package com.projects.coaching_offline_support.teacher.specification;

import com.projects.coaching_offline_support.student.dto.request.StudentFilter;
import com.projects.coaching_offline_support.student.entity.Student;
import com.projects.coaching_offline_support.teacher.dto.request.TeacherFilter;
import com.projects.coaching_offline_support.teacher.entity.Teacher;
import com.projects.coaching_offline_support.user.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TeacherSpecification {

    public static Specification<Teacher> filter(TeacherFilter filter){

        return ((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            // search
            if (filter.search() != null && !filter.search().isBlank()) {

                String keyword = "%" + filter.search().toLowerCase() + "%";

                Join<Teacher, User> userJoin = root.join("user");

                predicates.add(
                        cb.like(
                                cb.lower(userJoin.get("name")),
                                keyword
                        )
                );
            }



            if(filter.toDate() != null){
                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("createdAt"),
                                filter.toDate()
                        )
                );

            }
            if(filter.fromDate() != null){
                predicates.add(
                        cb.lessThanOrEqualTo(
                                root.get("createdAt"),
                                filter.toDate()
                        )
                );
            }
            if(filter.degree() != null && ! filter.degree().isBlank()){
                Join<Teacher,String> degreeJoin = root.join("degrees");
                predicates.add(
                        cb.equal(
                                cb.lower(degreeJoin),
                                filter.degree().toLowerCase()

                        )
                );
            }

            if(filter.subject() != null && ! filter.subject().isBlank()){
                Join<Teacher,String> subjectJoin = root.join(("subjects"));
                predicates.add(
                        cb.equal(
                                cb.lower(subjectJoin),
                                filter.subject().toLowerCase()
                        )

                );
            }


            return cb.and(predicates.toArray(new Predicate[0])) ;
        });
    }
}
