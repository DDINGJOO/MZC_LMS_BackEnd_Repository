package com.mzc.backend.lms.domains.academy.adapter.in.web;

import com.mzc.backend.lms.common.response.ApiResponse;
import com.mzc.backend.lms.domains.academy.adapter.in.web.dto.AcademicTermResponseDto;
import com.mzc.backend.lms.domains.academy.application.port.in.AcademicTermQueryUseCase;
import com.mzc.backend.lms.domains.academy.application.port.in.StudentAcademicTermQueryUseCase;
import com.mzc.backend.lms.common.exceptions.application.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * 학생 학기 조회 Controller
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/student/academic-terms")
@RequiredArgsConstructor
public class StudentAcademicTermController {

    private final StudentAcademicTermQueryUseCase studentAcademicTermQueryUseCase;
    private final AcademicTermQueryUseCase academicTermQueryUseCase;

    /**
     * 학생 본인 수강 학기 목록 조회 (성적 조회용 academicTermId 확인)
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<AcademicTermResponseDto>>> listMyAcademicTerms(
            @AuthenticationPrincipal Long studentId) {
        if (studentId == null) {
            throw new UnauthorizedException();
        }
        List<AcademicTermResponseDto> data = studentAcademicTermQueryUseCase.getMyAcademicTerms(studentId);
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    /**
     * 현재 학기 조회
     */
    @GetMapping("/current")
    public ResponseEntity<ApiResponse<AcademicTermResponseDto>> getCurrentAcademicTerm(
            @AuthenticationPrincipal Long studentId) {
        if (studentId == null) {
            throw new UnauthorizedException();
        }
        AcademicTermResponseDto data = academicTermQueryUseCase.getCurrentAcademicTerm(LocalDate.now());
        return ResponseEntity.ok(ApiResponse.success(data));
    }
}
