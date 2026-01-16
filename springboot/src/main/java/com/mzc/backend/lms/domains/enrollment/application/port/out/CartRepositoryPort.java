package com.mzc.backend.lms.domains.enrollment.application.port.out;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 장바구니 영속성을 위한 Port
 */
public interface CartRepositoryPort {

    /**
     * 장바구니 정보 DTO
     */
    record CartInfo(
        Long id,
        Long studentId,
        Long courseId,
        LocalDateTime addedAt
    ) {}

    /**
     * 장바구니 항목 저장 (ID 기반 - Student 엔티티 불필요)
     * @return 저장된 장바구니 항목의 ID
     */
    Long saveWithStudentId(Long studentId, Long courseId, LocalDateTime addedAt);

    /**
     * 학생의 장바구니 목록 조회
     */
    List<CartInfo> findByStudentId(Long studentId);

    /**
     * 학생의 특정 강의 장바구니 조회
     */
    Optional<CartInfo> findByStudentIdAndCourseId(Long studentId, Long courseId);

    /**
     * 학생의 특정 강의 장바구니 존재 여부
     */
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);

    /**
     * 여러 ID로 장바구니 항목 조회
     */
    List<CartInfo> findAllById(List<Long> ids);

    /**
     * 장바구니 항목 삭제
     */
    void delete(CartInfo cart);

    /**
     * 여러 장바구니 항목 삭제
     */
    void deleteAll(List<CartInfo> carts);

    /**
     * ID로 장바구니 항목 삭제
     */
    void deleteById(Long id);

    /**
     * 학생의 장바구니 전체 삭제
     */
    void deleteAllByStudentId(Long studentId);
}
