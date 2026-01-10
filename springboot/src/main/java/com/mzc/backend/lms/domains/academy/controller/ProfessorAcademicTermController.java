package com.mzc.backend.lms.domains.academy.controller;

import com.mzc.backend.lms.common.response.ApiResponse;
import com.mzc.backend.lms.domains.academy.service.ProfessorAcademicTermService;
import com.mzc.backend.lms.domains.course.course.adapter.in.web.dto.AcademicTermDto;
import com.mzc.backend.lms.domains.user.auth.exception.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/professor/academic-terms")
@RequiredArgsConstructor
public class ProfessorAcademicTermController {

    private final ProfessorAcademicTermService professorAcademicTermService;

    /**
     * 교수 본인 담당 학기 목록 조회 (지난 학기 강의/성적 조회용 academicTermId 확인)
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<AcademicTermDto>>> listMyAcademicTerms(
            @AuthenticationPrincipal Long professorId) {
        if (professorId == null) {
            throw AuthException.unauthorized();
        }
        List<AcademicTermDto> data = professorAcademicTermService.listMyAcademicTerms(professorId);
        return ResponseEntity.ok(ApiResponse.success(data));
    }
}
