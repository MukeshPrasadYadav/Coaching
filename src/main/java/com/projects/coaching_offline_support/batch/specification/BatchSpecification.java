package com.projects.coaching_offline_support.batch.specification;

import com.projects.coaching_offline_support.Coaching.entity.Coaching;
import com.projects.coaching_offline_support.batch.dto.request.BatchFilter;
import com.projects.coaching_offline_support.batch.entity.Batch;
import com.projects.coaching_offline_support.common.Service.impl.CurrentUser;
import com.projects.coaching_offline_support.student.entity.Student;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BatchSpecification {

    public static Specification<Batch> filter(BatchFilter filter){

        UUID coachingId = CurrentUser.get().getId();
        return ((root, query, cb) -> {


            assert query != null;
            query.distinct(true);

            List<Predicate> predicates = new ArrayList<>();

            Join<Batch, Coaching> coachingJoin = root.join("coaching");

            predicates.add(
                    cb.equal(coachingJoin.get("id"), coachingId)
            );
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





            if(filter.status() != null){
                predicates.add(
                        cb.equal(root.get("status"),filter.status())
                );
            }

            return cb.and(predicates.toArray(new Predicate[0])) ;
        });
    }
}
