package com.mzc.backend.lms.domains.dashboard.student.adapter.out.persistence;

import com.mzc.backend.lms.domains.board.adapter.out.persistence.enums.BoardType;
import com.mzc.backend.lms.domains.dashboard.student.application.port.out.DashboardQueryPort;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 대시보드 조회 Adapter
 */
@Component
@RequiredArgsConstructor
public class DashboardQueryAdapter implements DashboardQueryPort {

    private final EntityManager em;

    @Override
    public List<PendingAssignmentInfo> findPendingAssignments(Long studentId, int withinDays) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime deadline = now.plusDays(withinDays);

        String jpql = """
            SELECT new com.mzc.backend.lms.domains.dashboard.student.application.port.out.DashboardQueryPort$PendingAssignmentInfo(
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
                SELECT e.courseId FROM Enrollment e WHERE e.student.studentId = :studentId
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

        TypedQuery<PendingAssignmentInfo> query = em.createQuery(jpql, PendingAssignmentInfo.class);
        query.setParameter("studentId", studentId);
        query.setParameter("now", now);
        query.setParameter("deadline", deadline);

        return query.getResultList();
    }

    @Override
    public List<TodayEnrollmentInfo> findTodayEnrollments(Long studentId) {
        DayOfWeek today = LocalDate.now().getDayOfWeek();

        String jpql = """
            SELECT DISTINCT new com.mzc.backend.lms.domains.dashboard.student.application.port.out.DashboardQueryPort$TodayEnrollmentInfo(
                e.id,
                e.courseId
            )
            FROM Enrollment e
            JOIN Course c ON e.courseId = c.id
            JOIN c.schedules cs
            WHERE e.student.studentId = :studentId
            AND cs.dayOfWeek = :today
            """;

        TypedQuery<TodayEnrollmentInfo> query = em.createQuery(jpql, TodayEnrollmentInfo.class);
        query.setParameter("studentId", studentId);
        query.setParameter("today", today);

        return query.getResultList();
    }

    @Override
    public List<NoticeInfo> findLatestNotices(int limit) {
        String jpql = """
            SELECT new com.mzc.backend.lms.domains.dashboard.student.application.port.out.DashboardQueryPort$NoticeInfo(
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

        TypedQuery<NoticeInfo> query = em.createQuery(jpql, NoticeInfo.class);
        query.setParameter("boardType", BoardType.NOTICE);
        query.setMaxResults(limit);

        return query.getResultList();
    }

    @Override
    public EnrollmentSummaryInfo findEnrollmentSummary(Long studentId) {
        String jpql = """
            SELECT new com.mzc.backend.lms.domains.dashboard.student.application.port.out.DashboardQueryPort$EnrollmentSummaryInfo(
                COUNT(e),
                COALESCE(SUM(s.credits), 0L)
            )
            FROM Enrollment e
            JOIN Course c ON e.courseId = c.id
            JOIN c.subject s
            WHERE e.student.studentId = :studentId
            """;

        TypedQuery<EnrollmentSummaryInfo> query = em.createQuery(jpql, EnrollmentSummaryInfo.class);
        query.setParameter("studentId", studentId);

        return query.getSingleResult();
    }
}
