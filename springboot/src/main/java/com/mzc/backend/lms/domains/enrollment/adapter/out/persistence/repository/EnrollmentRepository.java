package com.mzc.backend.lms.domains.enrollment.adapter.out.persistence.repository;

import com.mzc.backend.lms.domains.enrollment.adapter.out.persistence.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 수강신청 Repository
 */

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    /**
     * 학생 ID와 강의 ID로 수강신청 조회
     */
    Optional<Enrollment> findByStudentIdAndCourseId(Long studentId, Long courseId);

    /**
     * 학생 ID와 강의 ID로 수강신청 존재 여부 확인
     */
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);

    /**
     * 학생 ID로 수강신청 목록 조회
     */
    List<Enrollment> findByStudentId(Long studentId);

    /**
     * 강의 ID로 수강신청 목록 조회
     */
    List<Enrollment> findByCourseId(Long courseId);

    /**
     * N+1 방지: 강의별 수강신청 목록 + 학생 함께 로딩
     */
    @Query("SELECT e FROM Enrollment e JOIN FETCH e.student s WHERE e.courseId = :courseId")
    List<Enrollment> findByCourseIdWithStudent(@Param("courseId") Long courseId);

    /**
     * 학생이 수강한 강의 ID 목록 조회
     */
    @Query("SELECT e.courseId FROM Enrollment e WHERE e.student.studentId = :studentId")
    List<Long> findCourseIdsByStudentId(@Param("studentId") Long studentId);

    /**
     * 강의 ID로 수강생 ID 목록 조회
     */
    @Query("SELECT e.student.studentId FROM Enrollment e WHERE e.courseId = :courseId")
    List<Long> findStudentIdsByCourseId(@Param("courseId") Long courseId);

    /**
     * 학생 ID로 해당 학생이 수강한 강의들의 학기 정보 조회 (중복 제거)
     * academy 도메인을 위한 쿼리
     */
    @Query("SELECT DISTINCT c.academicTerm FROM Enrollment e " +
           "JOIN Course c ON e.courseId = c.id " +
           "WHERE e.student.studentId = :studentId")
    List<com.mzc.backend.lms.domains.academy.adapter.out.persistence.entity.AcademicTerm> findDistinctAcademicTermsByStudentId(@Param("studentId") Long studentId);

    /**
     * 학생 ID와 학기 ID로 수강신청 목록 조회
     */
    @Query("SELECT e FROM Enrollment e " +
           "JOIN Course c ON e.courseId = c.id " +
           "WHERE e.student.studentId = :studentId AND c.academicTerm.id = :academicTermId")
    List<Enrollment> findByStudentIdAndAcademicTermId(@Param("studentId") Long studentId, @Param("academicTermId") Long academicTermId);
}
