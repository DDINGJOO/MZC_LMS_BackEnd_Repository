package com.mzc.backend.lms.domains.assessment.application.port.in;

import com.mzc.backend.lms.domains.assessment.adapter.in.web.dto.request.AssessmentCreateRequestDto;
import com.mzc.backend.lms.domains.assessment.adapter.in.web.dto.request.AssessmentUpdateRequestDto;
import com.mzc.backend.lms.domains.assessment.adapter.in.web.dto.request.AttemptGradeRequestDto;
import com.mzc.backend.lms.domains.assessment.adapter.in.web.dto.response.AssessmentDetailResponseDto;
import com.mzc.backend.lms.domains.assessment.adapter.in.web.dto.response.AssessmentListItemResponseDto;
import com.mzc.backend.lms.domains.assessment.adapter.in.web.dto.response.AttemptGradeResponseDto;
import com.mzc.backend.lms.domains.assessment.adapter.in.web.dto.response.ProfessorAttemptDetailResponseDto;
import com.mzc.backend.lms.domains.assessment.adapter.in.web.dto.response.ProfessorAttemptListItemResponseDto;
import com.mzc.backend.lms.domains.assessment.adapter.out.persistence.entity.enums.AssessmentType;
import com.mzc.backend.lms.domains.board.adapter.out.persistence.enums.BoardType;

import java.util.List;

/**
 * 교수용 Assessment UseCase (Inbound Port)
 * Controller에서 이 인터페이스를 호출
 */
public interface AssessmentProfessorUseCase {

    /**
     * 교수용 평가 목록 조회
     */
    List<AssessmentListItemResponseDto> listForProfessor(Long courseId, AssessmentType type, long professorId);

    /**
     * 교수용 평가 상세 조회
     */
    AssessmentDetailResponseDto getDetailForProfessor(Long assessmentId, long professorId);

    /**
     * 평가 생성
     */
    AssessmentDetailResponseDto create(BoardType boardType, AssessmentCreateRequestDto req, long professorId);

    /**
     * 평가 수정
     */
    AssessmentDetailResponseDto update(Long assessmentId, AssessmentUpdateRequestDto req, long professorId);

    /**
     * 평가 삭제
     */
    void delete(Long assessmentId, long professorId);

    /**
     * 응시자/응시 결과 목록 조회 (교수)
     */
    List<ProfessorAttemptListItemResponseDto> listAttemptsForProfessor(Long assessmentId, String status, long professorId);

    /**
     * 응시 결과 상세 조회(답안 포함) (교수)
     */
    ProfessorAttemptDetailResponseDto getAttemptDetailForProfessor(Long attemptId, long professorId);

    /**
     * 시험 채점 (교수)
     */
    AttemptGradeResponseDto gradeAttempt(Long attemptId, AttemptGradeRequestDto req, long professorId);
}
