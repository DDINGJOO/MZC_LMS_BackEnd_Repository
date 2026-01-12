package com.mzc.backend.lms.domains.user.adapter.in.web.swagger;

import com.mzc.backend.lms.domains.user.adapter.in.web.dto.search.CollegeListResponseDto;
import com.mzc.backend.lms.domains.user.adapter.in.web.dto.search.DepartmentListResponseDto;
import com.mzc.backend.lms.domains.user.adapter.in.web.dto.search.UserSearchCursorResponseDto;
import com.mzc.backend.lms.domains.user.adapter.in.web.dto.search.UserSearchRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * User Search Controller Swagger 인터페이스
 * (Hexagonal Architecture - Inbound Adapter)
 */
@Tag(name = "User Search", description = "유저 탐색 API")
public interface UserSearchControllerSwagger {

    @Operation(summary = "유저 탐색", description = "단과대, 학과, 이름, 사용자 타입으로 유저를 탐색합니다. (커서 기반 무한스크롤)")
    ResponseEntity<UserSearchCursorResponseDto> searchUsers(
            @Parameter(description = "검색 조건") UserSearchRequestDto request);

    @Operation(summary = "단과대 목록 조회", description = "유저 탐색 필터용 단과대 목록을 조회합니다.")
    ResponseEntity<List<CollegeListResponseDto>> getColleges();

    @Operation(summary = "학과 목록 조회 (단과대별)", description = "특정 단과대의 학과 목록을 조회합니다.")
    ResponseEntity<List<DepartmentListResponseDto>> getDepartmentsByCollege(
            @Parameter(description = "단과대 ID") Long collegeId);

    @Operation(summary = "전체 학과 목록 조회", description = "모든 학과 목록을 조회합니다.")
    ResponseEntity<List<DepartmentListResponseDto>> getAllDepartments();
}
