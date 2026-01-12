package com.mzc.backend.lms.domains.user.adapter.in.web;

import com.mzc.backend.lms.domains.user.adapter.in.web.dto.search.CollegeListResponseDto;
import com.mzc.backend.lms.domains.user.adapter.in.web.dto.search.DepartmentListResponseDto;
import com.mzc.backend.lms.domains.user.adapter.in.web.dto.search.UserSearchCursorResponseDto;
import com.mzc.backend.lms.domains.user.adapter.in.web.dto.search.UserSearchRequestDto;
import com.mzc.backend.lms.domains.user.adapter.in.web.swagger.UserSearchControllerSwagger;
import com.mzc.backend.lms.domains.user.application.port.in.GetCollegesUseCase;
import com.mzc.backend.lms.domains.user.application.port.in.GetDepartmentsUseCase;
import com.mzc.backend.lms.domains.user.application.port.in.SearchUsersUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 유저 탐색 컨트롤러
 * (Hexagonal Architecture - Inbound Adapter)
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserSearchController implements UserSearchControllerSwagger {

    private final SearchUsersUseCase searchUsersUseCase;
    private final GetCollegesUseCase getCollegesUseCase;
    private final GetDepartmentsUseCase getDepartmentsUseCase;

    @Override
    @GetMapping("/search")
    public ResponseEntity<UserSearchCursorResponseDto> searchUsers(
            @ModelAttribute UserSearchRequestDto request) {
        UserSearchCursorResponseDto result = searchUsersUseCase.searchUsers(request);
        return ResponseEntity.ok(result);
    }

    @Override
    @GetMapping("/colleges")
    public ResponseEntity<List<CollegeListResponseDto>> getColleges() {
        List<CollegeListResponseDto> result = getCollegesUseCase.getAllColleges();
        return ResponseEntity.ok(result);
    }

    @Override
    @GetMapping("/colleges/{collegeId}/departments")
    public ResponseEntity<List<DepartmentListResponseDto>> getDepartmentsByCollege(
            @PathVariable Long collegeId) {
        List<DepartmentListResponseDto> result = getDepartmentsUseCase.getDepartmentsByCollegeId(collegeId);
        return ResponseEntity.ok(result);
    }

    @Override
    @GetMapping("/departments")
    public ResponseEntity<List<DepartmentListResponseDto>> getAllDepartments() {
        List<DepartmentListResponseDto> result = getDepartmentsUseCase.getAllDepartments();
        return ResponseEntity.ok(result);
    }
}
