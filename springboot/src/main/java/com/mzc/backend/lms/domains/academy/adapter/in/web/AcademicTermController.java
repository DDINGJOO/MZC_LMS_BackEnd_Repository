package com.mzc.backend.lms.domains.academy.adapter.in.web;

import com.mzc.backend.lms.common.response.ApiResponse;
import com.mzc.backend.lms.domains.academy.adapter.in.web.dto.AcademicTermResponseDto;
import com.mzc.backend.lms.domains.academy.application.port.in.AcademicTermQueryUseCase;
import com.mzc.backend.lms.common.exceptions.application.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * 현재 학기 조회 (공통)
 * - academic_terms의 start_date/end_date 범위로 현재 날짜에 해당하는 학기 반환
 * - 학생/교수 모두 사용 가능 (인증만 필요)
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/academic-terms")
@RequiredArgsConstructor
public class AcademicTermController {

    private final AcademicTermQueryUseCase academicTermQueryUseCase;

    @GetMapping("/current")
    public ResponseEntity<ApiResponse<AcademicTermResponseDto>> getCurrentAcademicTerm(
            @AuthenticationPrincipal Long userId) {
        if (userId == null) {
            throw new UnauthorizedException();
        }

        AcademicTermResponseDto data = academicTermQueryUseCase.getCurrentAcademicTerm(LocalDate.now());
        return ResponseEntity.ok(ApiResponse.success(data));
    }
}
