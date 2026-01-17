package com.mzc.backend.lms.domains.dashboard.student.application.port.out;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 대시보드 조회를 위한 Port
 */
public interface DashboardQueryPort {

    /**
     * 미제출 과제 목록 조회
     */
    List<PendingAssignmentInfo> findPendingAssignments(Long studentId, int withinDays);

    /**
     * 오늘의 수강신청 목록 조회 (오늘 강의가 있는)
     */
    List<TodayEnrollmentInfo> findTodayEnrollments(Long studentId);

    /**
     * 최신 공지사항 목록 조회
     */
    List<NoticeInfo> findLatestNotices(int limit);

    /**
     * 수강 현황 요약 조회
     */
    EnrollmentSummaryInfo findEnrollmentSummary(Long studentId);

    // DTO Records

    record PendingAssignmentInfo(
        Long assignmentId,
        Long postId,
        String title,
        Long courseId,
        String courseFullName,
        String courseName,
        LocalDateTime dueDate,
        boolean lateSubmissionAllowed
    ) {}

    record TodayEnrollmentInfo(
        Long enrollmentId,
        Long courseId
    ) {}

    record NoticeInfo(
        Long postId,
        String title,
        LocalDateTime createdAt,
        int viewCount
    ) {}

    record EnrollmentSummaryInfo(
        long courseCount,
        long totalCredits
    ) {}
}
