package com.mzc.backend.lms.domains.user.adapter.out.persistence.repository;

import com.mzc.backend.lms.common.repository.BaseCustomRepository;
import com.mzc.backend.lms.domains.user.adapter.in.web.dto.search.UserSearchRequestDto;
import com.mzc.backend.lms.domains.user.adapter.in.web.dto.search.UserSearchRequestDto.SortBy;
import com.mzc.backend.lms.domains.user.adapter.in.web.dto.search.UserSearchResponseDto;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 유저 검색 전용 쿼리 Repository
 * BaseCustomRepository 상속으로 EntityManager 설정 및 공통 유틸리티 활용
 */
@Repository
public class UserSearchQueryRepositoryJpa extends BaseCustomRepository<Void> {

    public UserSearchQueryRepositoryJpa() {
        super();
    }

    /**
     * 학생 검색
     */
    public List<UserSearchResponseDto> searchStudents(UserSearchRequestDto request, int fetchSize, SortBy sortBy) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT new com.mzc.backend.lms.domains.user.adapter.in.web.dto.search.UserSearchResponseDto(");
        jpql.append("s.studentId, up.name, u.email, d.departmentName, c.collegeName, 'STUDENT', upi.thumbnailUrl) ");
        jpql.append("FROM Student s ");
        jpql.append("JOIN s.user u ");
        jpql.append("LEFT JOIN UserProfile up ON up.user = u ");
        jpql.append("LEFT JOIN UserProfileImage upi ON upi.user = u ");
        jpql.append("LEFT JOIN StudentDepartment sd ON sd.student = s AND sd.isPrimary = true ");
        jpql.append("LEFT JOIN sd.department d ");
        jpql.append("LEFT JOIN d.college c ");
        jpql.append("WHERE u.deletedAt IS NULL ");

        Map<String, Object> params = new HashMap<>();

        appendCursorCondition(jpql, params, request, sortBy, "s.studentId");
        appendFilters(jpql, params, request);
        appendOrderBy(jpql, sortBy, "s.studentId");

        TypedQuery<UserSearchResponseDto> query = createQuery(jpql.toString(), UserSearchResponseDto.class);
        params.forEach(query::setParameter);
        applyLimit(query, fetchSize);

        return query.getResultList();
    }

    /**
     * 교수 검색
     */
    public List<UserSearchResponseDto> searchProfessors(UserSearchRequestDto request, int fetchSize, SortBy sortBy) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT new com.mzc.backend.lms.domains.user.adapter.in.web.dto.search.UserSearchResponseDto(");
        jpql.append("p.professorId, up.name, u.email, d.departmentName, c.collegeName, 'PROFESSOR', upi.thumbnailUrl) ");
        jpql.append("FROM Professor p ");
        jpql.append("JOIN p.user u ");
        jpql.append("LEFT JOIN UserProfile up ON up.user = u ");
        jpql.append("LEFT JOIN UserProfileImage upi ON upi.user = u ");
        jpql.append("LEFT JOIN ProfessorDepartment pd ON pd.professor = p AND pd.isPrimary = true ");
        jpql.append("LEFT JOIN pd.department d ");
        jpql.append("LEFT JOIN d.college c ");
        jpql.append("WHERE u.deletedAt IS NULL ");

        Map<String, Object> params = new HashMap<>();

        appendCursorCondition(jpql, params, request, sortBy, "p.professorId");
        appendFilters(jpql, params, request);
        appendOrderBy(jpql, sortBy, "p.professorId");

        TypedQuery<UserSearchResponseDto> query = createQuery(jpql.toString(), UserSearchResponseDto.class);
        params.forEach(query::setParameter);
        applyLimit(query, fetchSize);

        return query.getResultList();
    }

    private void appendCursorCondition(StringBuilder jpql, Map<String, Object> params,
                                       UserSearchRequestDto request, SortBy sortBy, String idField) {
        if (sortBy == SortBy.NAME) {
            if (request.getCursorName() != null && request.getCursorId() != null) {
                jpql.append("AND (up.name > :cursorName OR (up.name = :cursorName AND ");
                jpql.append(idField).append(" > :cursorId)) ");
                params.put("cursorName", request.getCursorName());
                params.put("cursorId", request.getCursorId());
            } else if (request.getCursorName() != null) {
                jpql.append("AND up.name > :cursorName ");
                params.put("cursorName", request.getCursorName());
            }
        } else {
            if (request.getCursorId() != null) {
                jpql.append("AND ").append(idField).append(" > :cursorId ");
                params.put("cursorId", request.getCursorId());
            }
        }
    }

    private void appendFilters(StringBuilder jpql, Map<String, Object> params, UserSearchRequestDto request) {
        if (request.getCollegeId() != null) {
            jpql.append("AND c.id = :collegeId ");
            params.put("collegeId", request.getCollegeId());
        }

        if (request.getDepartmentId() != null) {
            jpql.append("AND d.id = :departmentId ");
            params.put("departmentId", request.getDepartmentId());
        }

        if (request.getName() != null && !request.getName().isBlank()) {
            jpql.append("AND up.name LIKE :name ");
            params.put("name", "%" + request.getName() + "%");
        }
    }

    private void appendOrderBy(StringBuilder jpql, SortBy sortBy, String idField) {
        if (sortBy == SortBy.NAME) {
            jpql.append("ORDER BY up.name ASC, ").append(idField).append(" ASC");
        } else {
            jpql.append("ORDER BY ").append(idField).append(" ASC");
        }
    }
}
