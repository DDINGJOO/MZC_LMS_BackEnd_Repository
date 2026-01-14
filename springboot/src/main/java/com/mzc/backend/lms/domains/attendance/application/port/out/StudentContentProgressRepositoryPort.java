package com.mzc.backend.lms.domains.attendance.application.port.out;

import com.mzc.backend.lms.domains.attendance.adapter.out.persistence.entity.StudentContentProgress;

import java.util.List;
import java.util.Optional;

/**
 * 학생 콘텐츠 진행 상황 영속성을 위한 Port
 */
public interface StudentContentProgressRepositoryPort {

    /**
     * 진행 상황 저장
     */
    StudentContentProgress save(StudentContentProgress progress);

    /**
     * ID로 진행 상황 조회
     */
    Optional<StudentContentProgress> findById(Long id);

    /**
     * 학생 ID와 콘텐츠 ID로 진행 상황 조회
     */
    Optional<StudentContentProgress> findByStudentStudentIdAndContent_Id(Long studentId, Long contentId);

    /**
     * 학생 ID와 콘텐츠 ID 목록으로 진행 상황 조회
     */
    List<StudentContentProgress> findByStudentStudentIdAndContent_IdIn(Long studentId, List<Long> contentIds);

    /**
     * 학생 ID와 콘텐츠 ID 목록 중 완료된 것만 조회
     */
    List<StudentContentProgress> findCompletedByStudentAndContentIds(Long studentId, List<Long> contentIds);

    /**
     * 학생이 특정 주차에서 완료한 VIDEO 콘텐츠 수 조회
     */
    int countCompletedVideosByStudentAndWeek(Long studentId, Long weekId);

    /**
     * 학생의 특정 강의 전체 진행 상황 조회
     */
    List<StudentContentProgress> findByStudentAndCourse(Long studentId, Long courseId);
}
