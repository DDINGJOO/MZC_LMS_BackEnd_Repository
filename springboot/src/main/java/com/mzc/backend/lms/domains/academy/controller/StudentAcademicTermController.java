package com.mzc.backend.lms.domains.academy.controller;

import com.mzc.backend.lms.common.response.ApiResponse;
import com.mzc.backend.lms.domains.academy.service.StudentAcademicTermService;
import com.mzc.backend.lms.domains.course.course.adapter.in.web.dto.AcademicTermDto;
import com.mzc.backend.lms.domains.user.exception.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/student/academic-terms")
@RequiredArgsConstructor
public class StudentAcademicTermController {

    private final StudentAcademicTermService studentAcademicTermService;

    /**
     * 학생 본인 수강 학기 목록 조회 (성적 조회용 academicTermId 확인)
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<AcademicTermDto>>> listMyAcademicTerms(
            @AuthenticationPrincipal Long studentId) {
        if (studentId == null) {
            throw AuthException.unauthorized();
        }
        List<AcademicTermDto> data = studentAcademicTermService.listMyAcademicTerms(studentId);
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    /**
     * 현재 학기 조회 (활성화된 수강신청 기간 ENROLLMENT 기준)
     */
    @GetMapping("/current")
    public ResponseEntity<ApiResponse<AcademicTermDto>> getCurrentAcademicTerm(
            @AuthenticationPrincipal Long studentId) {
        if (studentId == null) {
            throw AuthException.unauthorized();
        }
        AcademicTermDto data = studentAcademicTermService.getCurrentAcademicTerm(LocalDateTime.now());
        return ResponseEntity.ok(ApiResponse.success(data));
    }
}
