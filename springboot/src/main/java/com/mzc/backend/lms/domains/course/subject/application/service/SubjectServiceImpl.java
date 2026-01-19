package com.mzc.backend.lms.domains.course.subject.application.service;

import com.mzc.backend.lms.common.config.CacheConfig;
import com.mzc.backend.lms.domains.course.course.adapter.out.persistence.entity.CourseType;
import com.mzc.backend.lms.domains.course.exception.CourseException;
import com.mzc.backend.lms.domains.course.subject.application.port.in.SubjectUseCase;
import com.mzc.backend.lms.domains.course.subject.application.port.out.ProfessorDepartmentPort;
import com.mzc.backend.lms.domains.course.subject.application.port.out.SubjectCourseTypePort;
import com.mzc.backend.lms.domains.course.subject.application.port.out.SubjectRepositoryPort;
import com.mzc.backend.lms.domains.course.subject.adapter.in.web.dto.SubjectDetailResponse;
import com.mzc.backend.lms.domains.course.subject.adapter.in.web.dto.SubjectResponse;
import com.mzc.backend.lms.domains.course.subject.adapter.in.web.dto.SubjectSearchResponse;
import com.mzc.backend.lms.domains.course.subject.adapter.out.persistence.entity.Subject;
import com.mzc.backend.lms.domains.course.subject.adapter.out.persistence.entity.SubjectPrerequisites;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.Department;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

/**
 * 과목 관리 UseCase 구현체
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubjectServiceImpl implements SubjectUseCase {

    // Persistence Ports
    private final SubjectRepositoryPort subjectRepository;

    // External Ports
    private final ProfessorDepartmentPort professorDepartmentPort;
    private final SubjectCourseTypePort courseTypePort;

    @Override
    public Page<SubjectResponse> getSubjects(
            Long userId,
            String userType,
            String keyword,
            Long departmentId,
            Boolean showAllDepartments,
            String courseType,
            Integer credits,
            Boolean isActive,
            Pageable pageable
    ) {
        // 교수인 경우 기본적으로 자기 학과 과목만 조회
        Long targetDepartmentId = departmentId;
        
        if ("PROFESSOR".equals(userType) && Boolean.FALSE.equals(showAllDepartments)) {
            // 교수의 소속 학과 조회
            if (targetDepartmentId == null) {
                targetDepartmentId = professorDepartmentPort
                        .findDepartmentIdByProfessorId(userId)
                        .orElse(null);
            }
        }

        // CourseType 필터링 (문자열 코드를 숫자로 변환하여 조회)
        Long courseTypeId = null;
        if (courseType != null) {
            try {
                int typeCodeInt = CourseType.parseTypeCode(courseType);
                courseTypeId = courseTypePort.findIdByTypeCode(typeCodeInt)
                        .orElse(null);
            } catch (IllegalArgumentException e) {
                // 잘못된 courseType은 무시
                courseTypeId = null;
            }
        }

        // 과목 조회
        Page<Subject> subjects = subjectRepository.findSubjectsWithFilters(
                targetDepartmentId,
                keyword,
                courseTypeId,
                credits,
                pageable
        );

        // DTO 변환
        return subjects.map(this::toSubjectResponse);
    }

    @Override
    @Cacheable(value = CacheConfig.CACHE_SUBJECTS, key = "#subjectId")
    public SubjectDetailResponse getSubjectDetail(Long subjectId) {
        Subject subject = subjectRepository.findByIdWithDetails(subjectId)
                .orElseThrow(() -> CourseException.subjectNotFound(subjectId));

        return toSubjectDetailResponse(subject);
    }

    @Override
    public Page<SubjectSearchResponse> searchSubjects(String query, Pageable pageable) {
        if (query == null || query.trim().length() < 2) {
            throw CourseException.searchQueryTooShort();
        }

        Page<Subject> subjects = subjectRepository.searchSubjects(query.trim(), pageable);

        return subjects.map(this::toSubjectSearchResponse);
    }

    // DTO 변환 메서드들
    private SubjectResponse toSubjectResponse(Subject subject) {
        Department dept = subject.getDepartment();
        
        return SubjectResponse.builder()
                .id(subject.getId())
                .subjectCode(subject.getSubjectCode())
                .subjectName(subject.getSubjectName())
                .englishName(null)  // 향후 추가
                .credits(subject.getCredits())
                .courseType(SubjectResponse.CourseTypeDto.builder()
                        .id(subject.getCourseType().getId())
                        .code(subject.getCourseType().getTypeCodeString())
                        .name(subject.getCourseType().getTypeName())
                        .color(subject.getCourseType().getColor())
                        .build())
                .department(SubjectResponse.DepartmentDto.builder()
                        .id(dept.getId())
                        .name(dept.getDepartmentName())
                        .college(dept.getCollege() != null ? dept.getCollege().getCollegeName() : null)
                        .build())
                .description(subject.getDescription())
                .prerequisites(subject.getPrerequisites().stream()
                        .map(this::toPrerequisiteDto)
                        .collect(Collectors.toList()))
                .currentTermSections(subject.getCourses().size())  // 간단히 전체 개설 수
                .isActive(true)  // 향후 isActive 필드 추가 시 사용
                .createdAt(subject.getCreatedAt())
                .build();
    }

    private SubjectDetailResponse toSubjectDetailResponse(Subject subject) {
        Department dept = subject.getDepartment();
        
        return SubjectDetailResponse.builder()
                .id(subject.getId())
                .subjectCode(subject.getSubjectCode())
                .subjectName(subject.getSubjectName())
                .englishName(null)  // 향후 추가
                .credits(subject.getCredits())
                .courseType(SubjectResponse.CourseTypeDto.builder()
                        .id(subject.getCourseType().getId())
                        .code(subject.getCourseType().getTypeCodeString())
                        .name(subject.getCourseType().getTypeName())
                        .color(subject.getCourseType().getColor())
                        .build())
                .department(SubjectResponse.DepartmentDto.builder()
                        .id(dept.getId())
                        .name(dept.getDepartmentName())
                        .college(dept.getCollege() != null ? dept.getCollege().getCollegeName() : null)
                        .build())
                .description(subject.getDescription())
                .objectives(null)  // 향후 추가
                .prerequisites(subject.getPrerequisites().stream()
                        .map(this::toPrerequisiteDto)
                        .collect(Collectors.toList()))
                .courses(subject.getCourses().stream()
                        .map(course -> SubjectDetailResponse.CourseInfoDto.builder()
                                .id(course.getId())
                                .section(course.getSectionNumber())
                                .professor(SubjectDetailResponse.ProfessorDto.builder()
                                        .id(course.getProfessor().getId())
                                        .name(course.getProfessor().getUser() != null ? 
                                              course.getProfessor().getUser().getEmail() : "N/A")  // 향후 Profile에서 name 가져오기
                                        .build())
                                .term(SubjectDetailResponse.TermDto.builder()
                                        .year(course.getAcademicTerm().getYear())
                                        .termType(course.getAcademicTerm().getTermType())
                                        .build())
                                .currentStudents(course.getCurrentStudents())
                                .maxStudents(course.getMaxStudents())
                                .build())
                        .collect(Collectors.toList()))
                .isActive(true)  // 향후 추가
                .createdAt(subject.getCreatedAt())
                .build();
    }

    private SubjectSearchResponse toSubjectSearchResponse(Subject subject) {
        return SubjectSearchResponse.builder()
                .id(subject.getId())
                .subjectCode(subject.getSubjectCode())
                .subjectName(subject.getSubjectName())
                .credits(subject.getCredits())
                .courseType(subject.getCourseType().getTypeName())
                .department(subject.getDepartment().getDepartmentName())
                .build();
    }

    private SubjectResponse.PrerequisiteDto toPrerequisiteDto(SubjectPrerequisites prerequisite) {
        Subject prereqSubject = prerequisite.getPrerequisite();
        return SubjectResponse.PrerequisiteDto.builder()
                .id(prereqSubject.getId())
                .subjectCode(prereqSubject.getSubjectCode())
                .subjectName(prereqSubject.getSubjectName())
                .credits(prereqSubject.getCredits())
                .isMandatory(prerequisite.getIsMandatory())
                .build();
    }
}

