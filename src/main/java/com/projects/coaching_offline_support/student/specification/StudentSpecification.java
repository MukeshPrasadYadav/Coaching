package com.projects.coaching_offline_support.student.specification;

import com.projects.coaching_offline_support.student.dto.request.StudentFilter;
import com.projects.coaching_offline_support.student.entity.Student;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class StudentSpecification {

    public static Specification<Student> filter(StudentFilter filter){

        return ((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            // search
            if(filter.search() != null  && !filter.search().isBlank()){
                String keyWord = "%" + filter.search().toLowerCase() + "%";

                predicates.add(
                        cb.or(
                                cb.like(cb.lower(root.get("name")),keyWord)

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


            return cb.and(predicates.toArray(new Predicate[0])) ;
        });
    }
}
