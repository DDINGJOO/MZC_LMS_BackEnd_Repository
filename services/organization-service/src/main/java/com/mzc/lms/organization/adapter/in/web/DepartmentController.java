package com.mzc.lms.organization.adapter.in.web;

import com.mzc.lms.organization.adapter.in.web.dto.DepartmentCreateRequest;
import com.mzc.lms.organization.adapter.in.web.dto.DepartmentResponse;
import com.mzc.lms.organization.adapter.in.web.dto.DepartmentUpdateRequest;
import com.mzc.lms.organization.application.port.in.DepartmentUseCase;
import com.mzc.lms.organization.domain.model.Department;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/departments")
@RequiredArgsConstructor
@Tag(name = "Department", description = "학과 API")
public class DepartmentController {

    private final DepartmentUseCase departmentUseCase;

    @PostMapping
    @Operation(summary = "학과 생성", description = "새로운 학과를 생성합니다.")
    public ResponseEntity<DepartmentResponse> createDepartment(@Valid @RequestBody DepartmentCreateRequest request) {
        log.debug("Creating department: {}", request.getDepartmentName());
        Department department = departmentUseCase.createDepartment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(DepartmentResponse.from(department));
    }

    @GetMapping("/{id}")
    @Operation(summary = "학과 조회", description = "ID로 학과를 조회합니다.")
    public ResponseEntity<DepartmentResponse> getDepartment(@PathVariable Long id) {
        log.debug("Getting department: {}", id);
        Department department = departmentUseCase.getDepartment(id);
        return ResponseEntity.ok(DepartmentResponse.from(department));
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "코드로 학과 조회", description = "코드로 학과를 조회합니다.")
    public ResponseEntity<DepartmentResponse> getDepartmentByCode(@PathVariable String code) {
        log.debug("Getting department by code: {}", code);
        Department department = departmentUseCase.getDepartmentByCode(code);
        return ResponseEntity.ok(DepartmentResponse.from(department));
    }

    @GetMapping
    @Operation(summary = "모든 학과 조회", description = "모든 학과 목록을 조회합니다.")
    public ResponseEntity<List<DepartmentResponse>> getAllDepartments() {
        log.debug("Getting all departments");
        List<Department> departments = departmentUseCase.getAllDepartments();
        return ResponseEntity.ok(DepartmentResponse.from(departments));
    }

    @GetMapping("/college/{collegeId}")
    @Operation(summary = "단과대학별 학과 조회", description = "단과대학 소속 학과 목록을 조회합니다.")
    public ResponseEntity<List<DepartmentResponse>> getDepartmentsByCollege(@PathVariable Long collegeId) {
        log.debug("Getting departments by college: {}", collegeId);
        List<Department> departments = departmentUseCase.getDepartmentsByCollege(collegeId);
        return ResponseEntity.ok(DepartmentResponse.from(departments));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "학과 수정", description = "학과 정보를 수정합니다.")
    public ResponseEntity<DepartmentResponse> updateDepartment(
            @PathVariable Long id,
            @Valid @RequestBody DepartmentUpdateRequest request) {
        log.debug("Updating department: {}", id);
        Department department = departmentUseCase.updateDepartment(id, request);
        return ResponseEntity.ok(DepartmentResponse.from(department));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "학과 삭제", description = "학과를 삭제합니다.")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        log.debug("Deleting department: {}", id);
        departmentUseCase.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }
}
