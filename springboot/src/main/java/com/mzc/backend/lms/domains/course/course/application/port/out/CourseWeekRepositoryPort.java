package com.mzc.backend.lms.domains.course.course.application.port.out;

import com.mzc.backend.lms.domains.course.course.adapter.out.persistence.entity.CourseWeek;

import java.util.List;
import java.util.Optional;

/**
 * 강의 주차 영속성 Port
 */
public interface CourseWeekRepositoryPort {

    /**
     * 주차 저장
     */
    CourseWeek save(CourseWeek week);

    /**
     * ID로 주차 조회
     */
    Optional<CourseWeek> findById(Long id);

    /**
     * 강의 ID로 주차 목록 조회
     */
    List<CourseWeek> findByCourseId(Long courseId);

    /**
     * 주차 삭제
     */
    void delete(CourseWeek week);

    /**
     * 강의 ID와 주차 번호로 존재 여부 확인
     */
    boolean existsByCourseIdAndWeekNumber(Long courseId, Integer weekNumber);

    /**
     * 강의 ID로 주차 수 조회
     */
    long countByCourseId(Long courseId);
}
