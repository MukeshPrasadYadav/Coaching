package com.projects.coaching_offline_support.batch.specification;

import com.projects.coaching_offline_support.batch.dto.request.BatchFilter;
import com.projects.coaching_offline_support.batch.entity.Batch;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class BatchSpecification {

    public static Specification<Batch> filter(BatchFilter filter){

        return ((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            // search
            if(filter.search() != null  && !filter.search().isBlank()){
                String keyWord = "%" + filter.search().toLowerCase() + "%";

                predicates.add(
                        cb.or(
                                cb.like(cb.lower(root.get("name")),keyWord),
                                cb.like(cb.lower(root.join("teacher").get("name")),keyWord),
                                cb.like(cb.lower(root.get("subject")),keyWord),
                                cb.like(cb.lower(root.join("coaching").get("name")),keyWord)

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

            if(filter.coachingId() != null ){
                predicates.add(
                        cb.equal(root.get("coaching").get("id"),filter.coachingId())
                );
            }

            if(filter.coachingName() != null ){
                predicates.add(
                        cb.equal(root.get("coaching").get("name"),filter.coachingId())
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
