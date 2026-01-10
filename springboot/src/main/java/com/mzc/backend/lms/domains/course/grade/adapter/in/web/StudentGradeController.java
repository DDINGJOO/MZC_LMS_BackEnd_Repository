package com.mzc.backend.lms.domains.course.grade.adapter.in.web;

import com.mzc.backend.lms.common.response.ApiResponse;
import com.mzc.backend.lms.domains.course.grade.adapter.in.web.dto.StudentGradeResponseDto;
import com.mzc.backend.lms.domains.course.grade.application.service.StudentGradeService;
import com.mzc.backend.lms.domains.user.auth.exception.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/student/grades")
@RequiredArgsConstructor
public class StudentGradeController {

    private final StudentGradeService studentGradeService;

    /**
     * 학생 성적 조회 (지난 학기 포함)
     * - 본인(studentId) 기준
     * - 공개된(PUBLISHED) 성적만 반환
     * - academicTermId로 학기 필터 가능
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<StudentGradeResponseDto>>> listMyGrades(
            @AuthenticationPrincipal Long studentId,
            @RequestParam(required = false) Long academicTermId
    ) {
        if (studentId == null) {
            throw AuthException.unauthorized();
        }

        List<StudentGradeResponseDto> data = studentGradeService.listPublishedGrades(studentId, academicTermId);
        return ResponseEntity.ok(ApiResponse.success(data));
    }
}
