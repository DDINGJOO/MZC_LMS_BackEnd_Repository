package com.mzc.backend.lms.domains.course.course.adapter.in.web;

import com.mzc.backend.lms.common.response.ApiResponse;
import com.mzc.backend.lms.domains.course.course.adapter.in.web.dto.UpdateWeekContentRequestDto;
import com.mzc.backend.lms.domains.course.course.adapter.in.web.dto.WeekContentDto;
import com.mzc.backend.lms.domains.course.course.application.service.CourseWeekContentService;
import com.mzc.backend.lms.domains.user.auth.exception.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 콘텐츠 단건(교수용) 관리 컨트롤러
 * 문서 스펙: /api/v1/professor/contents/{contentId}
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/professor/contents")
@RequiredArgsConstructor
public class ProfessorWeekContentController {

    private final CourseWeekContentService courseWeekContentService;

    /**
     * 콘텐츠 수정 (단일 contentId)
     */
    @PutMapping("/{contentId}")
    public ResponseEntity<ApiResponse<WeekContentDto>> updateContent(
            @PathVariable Long contentId,
            @RequestBody UpdateWeekContentRequestDto request,
            @AuthenticationPrincipal Long professorId
    ) {
        if (professorId == null) {
            throw AuthException.unauthorized();
        }

        WeekContentDto response = courseWeekContentService.updateContentByContentId(contentId, request, professorId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 콘텐츠 삭제 (단일 contentId)
     */
    @DeleteMapping("/{contentId}")
    public ResponseEntity<ApiResponse<Void>> deleteContent(
            @PathVariable Long contentId,
            @AuthenticationPrincipal Long professorId
    ) {
        if (professorId == null) {
            throw AuthException.unauthorized();
        }

        courseWeekContentService.deleteContentByContentId(contentId, professorId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
