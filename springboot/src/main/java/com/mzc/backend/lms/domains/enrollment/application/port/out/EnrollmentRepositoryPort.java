package com.mzc.backend.lms.domains.enrollment.application.port.out;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Enrollment 영속성을 위한 Port
 */
public interface EnrollmentRepositoryPort {

    /**
     * 수강신청 정보 DTO
     */
    record EnrollmentInfo(
        Long id,
        Long studentId,
        Long courseId,
        LocalDateTime enrolledAt
    ) {}

    /**
     * 수강신청 저장
     * @return 저장된 수강신청의 ID
     */
    Long save(EnrollmentInfo enrollment);

    /**
     * 수강신청 저장 (ID 기반 - Student 엔티티 불필요)
     * @return 저장된 수강신청의 ID
     */
    Long saveWithStudentId(Long studentId, Long courseId, LocalDateTime enrolledAt);

    /**
     * ID로 수강신청 조회
     */
    Optional<EnrollmentInfo> findById(Long id);

    /**
     * 학생 ID로 수강신청 목록 조회
     */
    List<EnrollmentInfo> findByStudentId(Long studentId);

    /**
     * 강의 ID로 수강신청 목록 조회
     */
    List<EnrollmentInfo> findByCourseId(Long courseId);

    /**
     * 학생 ID와 학기 ID로 수강신청 목록 조회
     */
    List<EnrollmentInfo> findByStudentIdAndAcademicTermId(Long studentId, Long academicTermId);

    /**
     * 학생이 특정 강의에 수강신청했는지 확인
     */
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);

    /**
     * 수강신청 삭제
     */
    void delete(EnrollmentInfo enrollment);

    /**
     * ID로 수강신청 삭제
     */
    void deleteById(Long id);
}
