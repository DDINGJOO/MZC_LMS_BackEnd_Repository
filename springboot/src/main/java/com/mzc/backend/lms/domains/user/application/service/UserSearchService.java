package com.mzc.backend.lms.domains.user.application.service;

import com.mzc.backend.lms.domains.user.adapter.in.web.dto.search.CollegeListResponseDto;
import com.mzc.backend.lms.domains.user.adapter.in.web.dto.search.DepartmentListResponseDto;
import com.mzc.backend.lms.domains.user.adapter.in.web.dto.search.UserSearchCursorResponseDto;
import com.mzc.backend.lms.domains.user.adapter.in.web.dto.search.UserSearchRequestDto;
import com.mzc.backend.lms.domains.user.adapter.in.web.dto.search.UserSearchRequestDto.SortBy;
import com.mzc.backend.lms.domains.user.adapter.in.web.dto.search.UserSearchResponseDto;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.repository.UserSearchQueryRepositoryJpa;
import com.mzc.backend.lms.domains.user.application.port.in.GetCollegesUseCase;
import com.mzc.backend.lms.domains.user.application.port.in.GetDepartmentsUseCase;
import com.mzc.backend.lms.domains.user.application.port.in.SearchUsersUseCase;
import com.mzc.backend.lms.domains.user.application.port.out.CollegeQueryPort;
import com.mzc.backend.lms.domains.user.application.port.out.DepartmentQueryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 유저 탐색 서비스
 * (Hexagonal Architecture - Application Service)
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserSearchService implements SearchUsersUseCase, GetCollegesUseCase, GetDepartmentsUseCase {

    private final UserSearchQueryRepositoryJpa userSearchQueryRepository;
    private final CollegeQueryPort collegeQueryPort;
    private final DepartmentQueryPort departmentQueryPort;

    @Override
    public UserSearchCursorResponseDto searchUsers(UserSearchRequestDto request) {
        int size = request.getSize() != null ? request.getSize() : 20;
        int fetchSize = size + 1;
        SortBy sortBy = request.getSortBy() != null ? request.getSortBy() : SortBy.ID;

        List<UserSearchResponseDto> results = new ArrayList<>();

        if (request.getUserType() == null || request.getUserType() == UserSearchRequestDto.UserType.STUDENT) {
            results.addAll(userSearchQueryRepository.searchStudents(request, fetchSize, sortBy));
        }

        if (request.getUserType() == null || request.getUserType() == UserSearchRequestDto.UserType.PROFESSOR) {
            results.addAll(userSearchQueryRepository.searchProfessors(request, fetchSize, sortBy));
        }

        if (sortBy == SortBy.NAME) {
            results.sort(Comparator.comparing(UserSearchResponseDto::getName, Comparator.nullsLast(Comparator.naturalOrder()))
                    .thenComparing(UserSearchResponseDto::getUserId));
        } else {
            results.sort(Comparator.comparing(UserSearchResponseDto::getUserId));
        }

        if (results.size() > fetchSize) {
            results = results.subList(0, fetchSize);
        }

        return UserSearchCursorResponseDto.of(results, size, sortBy);
    }

    @Override
    public List<CollegeListResponseDto> getAllColleges() {
        return collegeQueryPort.findAll().stream()
                .map(CollegeListResponseDto::from)
                .toList();
    }

    @Override
    public List<DepartmentListResponseDto> getDepartmentsByCollegeId(Long collegeId) {
        return departmentQueryPort.findByCollegeId(collegeId).stream()
                .map(DepartmentListResponseDto::from)
                .toList();
    }

    @Override
    public List<DepartmentListResponseDto> getAllDepartments() {
        return departmentQueryPort.findAll().stream()
                .map(DepartmentListResponseDto::from)
                .toList();
    }
}
