package com.projects.coaching_offline_support.batch.repository;

import com.projects.coaching_offline_support.batch.entity.BatchSchedule;
import com.projects.coaching_offline_support.common.enums.DaysOfWeek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BatchScheduleRepository extends JpaRepository<BatchSchedule, UUID> {

//    @Query("""
//SELECT bs FROM BatchSchedule bs
//WHERE bs.teacher.id =:teacherId
//AND bs.day =:dayOfWeek
//""")
//    List<BatchSchedule> findByTeacherAndDay(
//            @Param("teacherId") UUID teacherId,
//            @Param("dayOfWeek")DaysOfWeek day
//            );
}
