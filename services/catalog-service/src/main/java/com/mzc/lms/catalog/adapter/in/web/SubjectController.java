package com.mzc.lms.catalog.adapter.in.web;

import com.mzc.lms.catalog.adapter.in.web.dto.PrerequisiteRequest;
import com.mzc.lms.catalog.adapter.in.web.dto.SubjectCreateRequest;
import com.mzc.lms.catalog.adapter.in.web.dto.SubjectResponse;
import com.mzc.lms.catalog.adapter.in.web.dto.SubjectUpdateRequest;
import com.mzc.lms.catalog.application.port.in.SubjectUseCase;
import com.mzc.lms.catalog.domain.model.Subject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subjects")
@RequiredArgsConstructor
@Tag(name = "Subject", description = "과목 관리 API")
public class SubjectController {

    private final SubjectUseCase subjectUseCase;

    @PostMapping
    @Operation(summary = "과목 생성", description = "새로운 과목을 생성합니다")
    public ResponseEntity<SubjectResponse> createSubject(
            @Valid @RequestBody SubjectCreateRequest request
    ) {
        SubjectUseCase.CreateSubjectCommand command = new SubjectUseCase.CreateSubjectCommand(
                request.getSubjectCode(),
                request.getSubjectName(),
                request.getSubjectDescription(),
                request.getDepartmentId(),
                request.getCourseTypeId(),
                request.getCredits(),
                request.getTheoryHours(),
                request.getPracticeHours(),
                request.getDescription()
        );

        Subject subject = subjectUseCase.createSubject(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(SubjectResponse.from(subject));
    }

    @GetMapping("/{id}")
    @Operation(summary = "과목 조회", description = "ID로 과목을 조회합니다")
    public ResponseEntity<SubjectResponse> getSubject(@PathVariable Long id) {
        return subjectUseCase.getSubject(id)
                .map(subject -> ResponseEntity.ok(SubjectResponse.from(subject)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/code/{subjectCode}")
    @Operation(summary = "과목 코드로 조회", description = "과목 코드로 과목을 조회합니다")
    public ResponseEntity<SubjectResponse> getSubjectByCode(@PathVariable String subjectCode) {
        return subjectUseCase.getSubjectByCode(subjectCode)
                .map(subject -> ResponseEntity.ok(SubjectResponse.from(subject)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "과목 목록 조회", description = "필터 조건에 맞는 과목 목록을 조회합니다")
    public ResponseEntity<Page<SubjectResponse>> getSubjects(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Long courseTypeId,
            @RequestParam(required = false) Integer credits,
            @RequestParam(required = false) Boolean isActive,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        SubjectUseCase.SubjectSearchCriteria criteria = new SubjectUseCase.SubjectSearchCriteria(
                keyword,
                departmentId,
                courseTypeId,
                credits,
                isActive
        );

        Page<Subject> subjects = subjectUseCase.getSubjects(criteria, pageable);
        return ResponseEntity.ok(subjects.map(SubjectResponse::from));
    }

    @GetMapping("/search")
    @Operation(summary = "과목 검색", description = "키워드로 과목을 검색합니다")
    public ResponseEntity<Page<SubjectResponse>> searchSubjects(
            @RequestParam String query,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        Page<Subject> subjects = subjectUseCase.searchSubjects(query, pageable);
        return ResponseEntity.ok(subjects.map(SubjectResponse::from));
    }

    @GetMapping("/department/{departmentId}")
    @Operation(summary = "학과별 과목 조회", description = "특정 학과의 과목 목록을 조회합니다")
    public ResponseEntity<Page<SubjectResponse>> getSubjectsByDepartment(
            @PathVariable Long departmentId,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        Page<Subject> subjects = subjectUseCase.getSubjectsByDepartment(departmentId, pageable);
        return ResponseEntity.ok(subjects.map(SubjectResponse::from));
    }

    @GetMapping("/course-type/{courseTypeId}")
    @Operation(summary = "이수구분별 과목 조회", description = "특정 이수구분의 과목 목록을 조회합니다")
    public ResponseEntity<Page<SubjectResponse>> getSubjectsByCourseType(
            @PathVariable Long courseTypeId,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        Page<Subject> subjects = subjectUseCase.getSubjectsByCourseType(courseTypeId, pageable);
        return ResponseEntity.ok(subjects.map(SubjectResponse::from));
    }

    @PutMapping("/{id}")
    @Operation(summary = "과목 수정", description = "과목 정보를 수정합니다")
    public ResponseEntity<SubjectResponse> updateSubject(
            @PathVariable Long id,
            @Valid @RequestBody SubjectUpdateRequest request
    ) {
        SubjectUseCase.UpdateSubjectCommand command = new SubjectUseCase.UpdateSubjectCommand(
                request.getSubjectName(),
                request.getSubjectDescription(),
                request.getCourseTypeId(),
                request.getCredits(),
                request.getTheoryHours(),
                request.getPracticeHours(),
                request.getDescription()
        );

        Subject subject = subjectUseCase.updateSubject(id, command);
        return ResponseEntity.ok(SubjectResponse.from(subject));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "과목 삭제", description = "과목을 삭제합니다")
    public ResponseEntity<Void> deleteSubject(@PathVariable Long id) {
        subjectUseCase.deleteSubject(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "과목 비활성화", description = "과목을 비활성화합니다")
    public ResponseEntity<SubjectResponse> deactivateSubject(@PathVariable Long id) {
        Subject subject = subjectUseCase.deactivateSubject(id);
        return ResponseEntity.ok(SubjectResponse.from(subject));
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "과목 활성화", description = "과목을 활성화합니다")
    public ResponseEntity<SubjectResponse> activateSubject(@PathVariable Long id) {
        Subject subject = subjectUseCase.activateSubject(id);
        return ResponseEntity.ok(SubjectResponse.from(subject));
    }

    @PostMapping("/{id}/prerequisites")
    @Operation(summary = "선수과목 추가", description = "과목에 선수과목을 추가합니다")
    public ResponseEntity<Void> addPrerequisite(
            @PathVariable Long id,
            @Valid @RequestBody PrerequisiteRequest request
    ) {
        subjectUseCase.addPrerequisite(id, request.getPrerequisiteId(), request.getIsMandatory());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}/prerequisites/{prerequisiteId}")
    @Operation(summary = "선수과목 제거", description = "과목에서 선수과목을 제거합니다")
    public ResponseEntity<Void> removePrerequisite(
            @PathVariable Long id,
            @PathVariable Long prerequisiteId
    ) {
        subjectUseCase.removePrerequisite(id, prerequisiteId);
        return ResponseEntity.noContent().build();
    }
}
