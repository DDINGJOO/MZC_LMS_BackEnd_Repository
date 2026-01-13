package com.mzc.backend.lms.domains.course.course.application.port.out;

import com.mzc.backend.lms.domains.course.course.adapter.out.persistence.entity.Course;
import com.mzc.backend.lms.domains.academy.adapter.out.persistence.entity.AcademicTerm;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * 강의 영속성 Port
 */
public interface CourseRepositoryPort {

    /**
     * 강의 저장
     */
    Course save(Course course);

    /**
     * ID로 강의 조회
     */
    Optional<Course> findById(Long id);

    /**
     * ID로 강의 조회 (락 사용)
     */
    Optional<Course> findByIdWithLock(Long id);

    /**
     * 강의 삭제
     */
    void delete(Course course);

    /**
     * 학기 ID로 강의 목록 조회
     */
    List<Course> findByAcademicTermId(Long academicTermId);

    /**
     * 학과 ID로 강의 목록 조회
     */
    List<Course> findBySubjectDepartmentId(Long departmentId);

    /**
     * 교수 ID로 강의 목록 조회
     */
    List<Course> findByProfessorProfessorId(Long professorId);

    /**
     * 교수 ID와 학기 ID로 강의 목록 조회
     */
    List<Course> findByProfessorProfessorIdAndAcademicTermId(Long professorId, Long academicTermId);

    /**
     * 교수가 강의했던 학기 목록 조회
     */
    List<AcademicTerm> findDistinctAcademicTermsByProfessorId(Long professorId);

    /**
     * Course ID 목록으로 Subject 포함 조회
     */
    List<Course> findByIdInWithSubject(List<Long> ids);

    /**
     * 강의 중복 체크
     */
    boolean existsBySubjectIdAndAcademicTermIdAndSectionNumber(Long subjectId, Long academicTermId, String sectionNumber);

    /**
     * 시간표 충돌 체크
     */
    boolean existsByProfessorAndTimeConflict(Long professorId, Long academicTermId,
                                              DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime);

    /**
     * 시간표 충돌 체크 (특정 강의 제외)
     */
    boolean existsByProfessorAndTimeConflictExcludingCourse(Long professorId, Long academicTermId,
                                                             Long excludeCourseId, DayOfWeek dayOfWeek,
                                                             LocalTime startTime, LocalTime endTime);

    /**
     * 특정 과목 + 학기 기준 개설 강의 수
     */
    long countBySubjectIdAndAcademicTermId(Long subjectId, Long academicTermId);
}
