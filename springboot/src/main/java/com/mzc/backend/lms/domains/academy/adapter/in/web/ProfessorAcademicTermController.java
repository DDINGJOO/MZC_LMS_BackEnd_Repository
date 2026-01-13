package com.mzc.backend.lms.domains.academy.adapter.in.web;

import com.mzc.backend.lms.common.response.ApiResponse;
import com.mzc.backend.lms.domains.academy.adapter.in.web.dto.AcademicTermResponseDto;
import com.mzc.backend.lms.domains.academy.application.port.in.ProfessorAcademicTermQueryUseCase;
import com.mzc.backend.lms.domains.user.exception.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 교수 학기 조회 Controller
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/professor/academic-terms")
@RequiredArgsConstructor
public class ProfessorAcademicTermController {

    private final ProfessorAcademicTermQueryUseCase professorAcademicTermQueryUseCase;

    /**
     * 교수 본인 담당 학기 목록 조회 (지난 학기 강의/성적 조회용 academicTermId 확인)
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<AcademicTermResponseDto>>> listMyAcademicTerms(
            @AuthenticationPrincipal Long professorId) {
        if (professorId == null) {
            throw AuthException.unauthorized();
        }
        List<AcademicTermResponseDto> data = professorAcademicTermQueryUseCase.getMyAcademicTerms(professorId);
        return ResponseEntity.ok(ApiResponse.success(data));
    }
}
