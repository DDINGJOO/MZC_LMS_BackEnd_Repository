package com.mzc.backend.lms.domains.course.grade.application.port.out;

import com.mzc.backend.lms.domains.board.assignment.entity.Assignment;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 과제 외부 Port (board 도메인)
 */
public interface AssignmentPort {

    /**
     * 강의 ID로 과제 목록 조회
     */
    List<Assignment> findByCourseId(Long courseId);

    /**
     * 과제 ID 목록에 채점 대기 중인 제출이 있는지 확인
     */
    boolean existsPendingGradingByAssignmentIds(List<Long> assignmentIds);

    /**
     * 과제 ID 목록으로 사용자별 채점된 점수 합계 조회
     */
    Map<Long, BigDecimal> sumGradedScoreByUserGroupByUserId(List<Long> assignmentIds);
}
