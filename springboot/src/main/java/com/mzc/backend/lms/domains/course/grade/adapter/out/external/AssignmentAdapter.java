package com.mzc.backend.lms.domains.course.grade.adapter.out.external;

import com.mzc.backend.lms.domains.course.grade.application.port.out.AssignmentPort;
import com.mzc.backend.lms.domains.board.assignment.entity.Assignment;
import com.mzc.backend.lms.domains.board.assignment.repository.AssignmentRepository;
import com.mzc.backend.lms.domains.board.assignment.repository.AssignmentSubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 과제 외부 Adapter (board 도메인)
 */
@Component
@RequiredArgsConstructor
public class AssignmentAdapter implements AssignmentPort {

    private final AssignmentRepository assignmentRepository;
    private final AssignmentSubmissionRepository assignmentSubmissionRepository;

    @Override
    public List<Assignment> findByCourseId(Long courseId) {
        return assignmentRepository.findByCourseId(courseId);
    }

    @Override
    public boolean existsPendingGradingByAssignmentIds(List<Long> assignmentIds) {
        return assignmentSubmissionRepository.existsPendingGradingByAssignmentIds(assignmentIds);
    }

    @Override
    public Map<Long, BigDecimal> sumGradedScoreByUserGroupByUserId(List<Long> assignmentIds) {
        Map<Long, BigDecimal> result = new HashMap<>();
        List<Object[]> rows = assignmentSubmissionRepository.sumGradedScoreByUserGroupByUserId(assignmentIds);
        for (Object[] r : rows) {
            Long userId = (Long) r[0];
            BigDecimal sum = (BigDecimal) r[1];
            result.put(userId, sum != null ? sum : BigDecimal.ZERO);
        }
        return result;
    }
}
