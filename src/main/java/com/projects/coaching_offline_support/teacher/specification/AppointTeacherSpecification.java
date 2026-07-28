package com.projects.coaching_offline_support.teacher.specification;

import com.projects.coaching_offline_support.Coaching.entity.Coaching;
import com.projects.coaching_offline_support.common.Service.impl.CurrentUser;
import com.projects.coaching_offline_support.teacher.dto.request.AppointTeacherFilter;
import com.projects.coaching_offline_support.teacher.entity.CoachingTeacher;
import com.projects.coaching_offline_support.teacher.entity.Teacher;
import com.projects.coaching_offline_support.user.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AppointTeacherSpecification {
    public  static Specification<Teacher> filter (AppointTeacherFilter filter){
        return ((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            // only admin can see multiple teachers



            // search
//            if (filter.search() != null && !filter.search().isBlank()) {
//
//                String keyword = "%" + filter.search().toLowerCase() + "%";
//
//
//                predicates.add(
//                        cb.like(
//                                cb.lower(Teacher. .get("name")),
//                                keyword
//                        )
//                );
//            }




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
