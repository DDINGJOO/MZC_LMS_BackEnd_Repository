package com.mzc.backend.lms.domains.enrollment.application.port.out;

import com.mzc.backend.lms.domains.enrollment.adapter.out.persistence.entity.CourseCart;

import java.util.List;
import java.util.Optional;

/**
 * 장바구니 영속성을 위한 Port
 */
public interface CartRepositoryPort {

    /**
     * 장바구니 항목 저장
     */
    CourseCart save(CourseCart cart);

    /**
     * 학생의 장바구니 목록 조회
     */
    List<CourseCart> findByStudentId(Long studentId);

    /**
     * 학생의 특정 강의 장바구니 조회
     */
    Optional<CourseCart> findByStudentIdAndCourseId(Long studentId, Long courseId);

    /**
     * 장바구니 항목 삭제
     */
    void delete(CourseCart cart);

    /**
     * ID로 장바구니 항목 삭제
     */
    void deleteById(Long id);

    /**
     * 학생의 장바구니 전체 삭제
     */
    void deleteAllByStudentId(Long studentId);
}
