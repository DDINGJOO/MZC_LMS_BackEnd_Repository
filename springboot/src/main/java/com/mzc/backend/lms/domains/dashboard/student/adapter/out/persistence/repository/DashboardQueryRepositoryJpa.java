package com.mzc.backend.lms.domains.dashboard.student.adapter.out.persistence.repository;

import com.mzc.backend.lms.common.repository.BaseCustomRepository;
import com.mzc.backend.lms.domains.board.adapter.out.persistence.enums.BoardType;
import com.mzc.backend.lms.domains.dashboard.student.adapter.in.web.dto.EnrollmentSummaryDto;
import com.mzc.backend.lms.domains.dashboard.student.adapter.in.web.dto.NoticeDto;
import com.mzc.backend.lms.domains.dashboard.student.adapter.in.web.dto.PendingAssignmentDto;
import com.mzc.backend.lms.domains.enrollment.adapter.out.persistence.entity.Enrollment;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 대시보드 전용 조회 Repository
 * BaseCustomRepository 상속으로 EntityManager 설정 및 공통 유틸리티 활용
 */
@Repository
public class DashboardQueryRepositoryJpa extends BaseCustomRepository<Void> {

    public DashboardQueryRepositoryJpa() {
        super();
    }

    /**
     * 미제출 과제 목록 조회
     */
    public List<PendingAssignmentDto> findPendingAssignments(Long studentId, int withinDays) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime deadline = now.plusDays(withinDays);

        String jpql = """
            SELECT new com.mzc.backend.lms.domains.dashboard.student.dto.PendingAssignmentDto(
                a.id,
                p.id,
                p.title,
                c.id,
                CONCAT(s.subjectName, ' - ', c.sectionNumber),
                s.subjectName,
                a.dueDate,
                a.lateSubmissionAllowed
            )
            FROM Assignment a
            JOIN a.post p
            JOIN Course c ON a.courseId = c.id
            JOIN c.subject s
            WHERE a.courseId IN (
                SELECT e.course.id FROM Enrollment e WHERE e.student.studentId = :studentId
            )
            AND a.dueDate > :now
            AND a.dueDate <= :deadline
            AND a.isDeleted = false
            AND NOT EXISTS (
                SELECT 1 FROM AssignmentSubmission sub
                WHERE sub.assignment.id = a.id
                AND sub.userId = :studentId
                AND sub.isDeleted = false
            )
            ORDER BY a.dueDate ASC
            """;

        TypedQuery<PendingAssignmentDto> query = createQuery(jpql, PendingAssignmentDto.class);
        query.setParameter("studentId", studentId);
        query.setParameter("now", now);
        query.setParameter("deadline", deadline);

        return query.getResultList();
    }

    /**
     * 오늘의 강의 목록 조회
     */
    public List<Enrollment> findTodayEnrollments(Long studentId) {
        DayOfWeek today = LocalDate.now().getDayOfWeek();

        String jpql = """
            SELECT DISTINCT e
            FROM Enrollment e
            JOIN FETCH e.course c
            JOIN FETCH c.subject s
            JOIN FETCH c.professor p
            JOIN FETCH p.user u
            LEFT JOIN FETCH u.userProfile up
            JOIN FETCH c.schedules cs
            WHERE e.student.studentId = :studentId
            AND cs.dayOfWeek = :today
            """;

        TypedQuery<Enrollment> query = createQuery(jpql, Enrollment.class);
        query.setParameter("studentId", studentId);
        query.setParameter("today", today);

        return query.getResultList();
    }

    /**
     * 최신 공지사항 목록 조회
     */
    public List<NoticeDto> findLatestNotices(int limit) {
        String jpql = """
            SELECT new com.mzc.backend.lms.domains.dashboard.student.dto.NoticeDto(
                p.id,
                p.title,
                p.createdAt,
                p.viewCount
            )
            FROM Post p
            JOIN p.category c
            WHERE c.boardType = :boardType
            AND p.isDeleted = false
            ORDER BY p.createdAt DESC
            """;

        TypedQuery<NoticeDto> query = createQuery(jpql, NoticeDto.class);
        query.setParameter("boardType", BoardType.NOTICE);
        applyLimit(query, limit);

        return query.getResultList();
    }

    /**
     * 수강 현황 요약 조회
     */
    public EnrollmentSummaryDto findEnrollmentSummary(Long studentId) {
        String jpql = """
            SELECT new com.mzc.backend.lms.domains.dashboard.student.dto.EnrollmentSummaryDto(
                COUNT(e),
                COALESCE(SUM(s.credits), 0)
            )
            FROM Enrollment e
            JOIN e.course c
            JOIN c.subject s
            WHERE e.student.studentId = :studentId
            """;

        TypedQuery<EnrollmentSummaryDto> query = createQuery(jpql, EnrollmentSummaryDto.class);
        query.setParameter("studentId", studentId);

        return query.getSingleResult();
    }
}
