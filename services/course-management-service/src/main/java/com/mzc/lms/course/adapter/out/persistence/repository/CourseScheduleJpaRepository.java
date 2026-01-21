package com.mzc.lms.course.adapter.out.persistence.repository;

import com.mzc.lms.course.adapter.out.persistence.entity.CourseScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

public interface CourseScheduleJpaRepository extends JpaRepository<CourseScheduleEntity, Long> {

    List<CourseScheduleEntity> findByCourseId(Long courseId);

    List<CourseScheduleEntity> findByRoom(String room);

    @Query("SELECT s FROM CourseScheduleEntity s WHERE s.dayOfWeek = :dayOfWeek " +
           "AND :time >= s.startTime AND :time < s.endTime")
    List<CourseScheduleEntity> findByDayOfWeekAndTimeBetween(
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("time") LocalTime time
    );

    @Query("SELECT s FROM CourseScheduleEntity s WHERE s.courseId != :excludeCourseId " +
           "AND s.dayOfWeek = :dayOfWeek AND s.room = :room " +
           "AND NOT (s.endTime <= :startTime OR s.startTime >= :endTime)")
    List<CourseScheduleEntity> findConflictingSchedules(
            @Param("excludeCourseId") Long excludeCourseId,
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("room") String room
    );

    void deleteByCourseId(Long courseId);
}
