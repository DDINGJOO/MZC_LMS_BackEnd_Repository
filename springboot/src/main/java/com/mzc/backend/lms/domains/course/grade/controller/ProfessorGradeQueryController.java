package com.mzc.backend.lms.domains.course.grade.controller;

import com.mzc.backend.lms.common.response.ApiResponse;
import com.mzc.backend.lms.domains.course.grade.dto.ProfessorCourseGradesResponseDto;
import com.mzc.backend.lms.domains.course.grade.service.ProfessorGradeQueryService;
import com.mzc.backend.lms.domains.user.auth.exception.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/professor/courses")
@RequiredArgsConstructor
public class ProfessorGradeQueryController {

    private final ProfessorGradeQueryService professorGradeQueryService;

    /**
     * 담당 강의 수강생 성적 전체 조회
     * - 담당 교수만 조회 가능
     * - status=ALL|PUBLISHED (기본 ALL)
     */
    @GetMapping("/{courseId}/grades")
    public ResponseEntity<ApiResponse<List<ProfessorCourseGradesResponseDto>>> listCourseGrades(
            @PathVariable Long courseId,
            @RequestParam(required = false, defaultValue = "ALL") String status,
            @AuthenticationPrincipal Long professorId
    ) {
        if (professorId == null) {
            throw AuthException.unauthorized();
        }

        ProfessorGradeQueryService.GradeQueryStatus st;
        try {
            st = ProfessorGradeQueryService.GradeQueryStatus.valueOf(status);
        } catch (Exception e) {
            throw new IllegalArgumentException("status는 ALL 또는 PUBLISHED 여야 합니다.");
        }

        List<ProfessorCourseGradesResponseDto> data = professorGradeQueryService.listCourseGrades(courseId, professorId, st);
        return ResponseEntity.ok(ApiResponse.success(data));
    }
}
