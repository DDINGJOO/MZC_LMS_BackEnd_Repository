package com.mzc.backend.lms.domains.assessment.application.port.in;

import com.mzc.backend.lms.domains.assessment.adapter.in.web.dto.request.AttemptSubmitRequestDto;
import com.mzc.backend.lms.domains.assessment.adapter.in.web.dto.response.AssessmentDetailResponseDto;
import com.mzc.backend.lms.domains.assessment.adapter.in.web.dto.response.AssessmentListItemResponseDto;
import com.mzc.backend.lms.domains.assessment.adapter.in.web.dto.response.AttemptStartResponseDto;
import com.mzc.backend.lms.domains.assessment.adapter.in.web.dto.response.AttemptSubmitResponseDto;
import com.mzc.backend.lms.domains.assessment.adapter.out.persistence.entity.enums.AssessmentType;

import java.util.List;

/**
 * 학생용 Assessment UseCase (Inbound Port)
 * Controller에서 이 인터페이스를 호출
 */
public interface AssessmentStudentUseCase {

    /**
     * 학생용 평가 목록 조회
     */
    List<AssessmentListItemResponseDto> listForStudent(Long courseId, AssessmentType type, long studentId);

    /**
     * 학생용 평가 상세 조회
     */
    AssessmentDetailResponseDto getDetailForStudent(Long assessmentId, long studentId);

    /**
     * 응시 시작
     */
    AttemptStartResponseDto startAttempt(Long assessmentId, long studentId);

    /**
     * 응시 제출
     */
    AttemptSubmitResponseDto submitAttempt(Long attemptId, AttemptSubmitRequestDto req, long studentId);
}
