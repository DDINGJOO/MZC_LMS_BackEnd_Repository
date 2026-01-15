package com.mzc.backend.lms.domains.attendance.application.port.out;

import com.mzc.backend.lms.domains.attendance.domain.model.StudentContentProgressDomain;

import java.util.List;
import java.util.Optional;

/**
 * 학생 콘텐츠 진행 상황 영속성을 위한 Port
 */
public interface StudentContentProgressRepositoryPort {

    /**
     * 진행 상황 저장
     */
    StudentContentProgressDomain save(StudentContentProgressDomain progress);

    /**
     * ID로 진행 상황 조회
     */
    Optional<StudentContentProgressDomain> findById(Long id);

    /**
     * 학생 ID와 콘텐츠 ID로 진행 상황 조회
     */
    Optional<StudentContentProgressDomain> findByStudentIdAndContentId(Long studentId, Long contentId);

    /**
     * 학생 ID와 콘텐츠 ID 목록으로 진행 상황 조회
     */
    List<StudentContentProgressDomain> findByStudentIdAndContentIdIn(Long studentId, List<Long> contentIds);

    /**
     * 학생 ID와 콘텐츠 ID 목록 중 완료된 것만 조회
     */
    List<StudentContentProgressDomain> findCompletedByStudentAndContentIds(Long studentId, List<Long> contentIds);

    /**
     * 학생이 특정 주차에서 완료한 VIDEO 콘텐츠 수 조회
     */
    int countCompletedVideosByStudentAndWeek(Long studentId, Long weekId);

    /**
     * 학생의 특정 강의 전체 진행 상황 조회
     */
    List<StudentContentProgressDomain> findByStudentAndCourse(Long studentId, Long courseId);
}
