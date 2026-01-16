package com.mzc.backend.lms.domains.attendance.adapter.out.persistence.repository;

import com.mzc.backend.lms.domains.attendance.adapter.out.persistence.entity.StudentContentProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 학생 콘텐츠 진행 상황 Repository
 */
@Repository
public interface StudentContentProgressJpaRepository extends JpaRepository<StudentContentProgress, Long> {

    /**
     * 학생 ID와 콘텐츠 ID로 진행 상황 조회
     */
    Optional<StudentContentProgress> findByStudentStudentIdAndContentId(Long studentId, Long contentId);

    /**
     * 학생 ID와 콘텐츠 ID 목록으로 진행 상황 조회
     */
    List<StudentContentProgress> findByStudentStudentIdAndContentIdIn(Long studentId, List<Long> contentIds);

    /**
     * 학생 ID와 콘텐츠 ID 목록 중 완료된 것만 조회
     */
    @Query("SELECT scp FROM StudentContentProgress scp " +
            "WHERE scp.student.studentId = :studentId " +
            "AND scp.contentId IN :contentIds " +
            "AND scp.isCompleted = true")
    List<StudentContentProgress> findCompletedByStudentAndContentIds(
            @Param("studentId") Long studentId,
            @Param("contentIds") List<Long> contentIds);

    /**
     * 학생이 특정 주차에서 완료한 VIDEO 콘텐츠 수 조회
     * Note: weekId와 contentType 필터링은 contentIds를 미리 조회하여 전달해야 함
     */
    @Query(value = "SELECT COUNT(scp.id) FROM student_content_progress scp " +
            "JOIN week_contents wc ON scp.content_id = wc.id " +
            "WHERE scp.student_id = :studentId " +
            "AND wc.week_id = :weekId " +
            "AND wc.content_type = 'VIDEO' " +
            "AND scp.is_completed = true", nativeQuery = true)
    int countCompletedVideosByStudentAndWeek(
            @Param("studentId") Long studentId,
            @Param("weekId") Long weekId);

    /**
     * 학생의 특정 강의 전체 진행 상황 조회
     */
    @Query(value = "SELECT scp.* FROM student_content_progress scp " +
            "JOIN week_contents wc ON scp.content_id = wc.id " +
            "JOIN course_weeks cw ON wc.week_id = cw.id " +
            "WHERE scp.student_id = :studentId " +
            "AND cw.course_id = :courseId", nativeQuery = true)
    List<StudentContentProgress> findByStudentAndCourse(
            @Param("studentId") Long studentId,
            @Param("courseId") Long courseId);
}
