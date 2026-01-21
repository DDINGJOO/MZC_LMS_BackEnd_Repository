package com.mzc.lms.organization.adapter.in.web;

import com.mzc.lms.organization.adapter.in.web.dto.AcademicTermCreateRequest;
import com.mzc.lms.organization.adapter.in.web.dto.AcademicTermResponse;
import com.mzc.lms.organization.adapter.in.web.dto.AcademicTermUpdateRequest;
import com.mzc.lms.organization.application.port.in.AcademicTermUseCase;
import com.mzc.lms.organization.domain.model.AcademicTerm;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/academic-terms")
@RequiredArgsConstructor
@Tag(name = "Academic Term", description = "학기 API")
public class AcademicTermController {

    private final AcademicTermUseCase academicTermUseCase;

    @PostMapping
    @Operation(summary = "학기 생성", description = "새로운 학기를 생성합니다.")
    public ResponseEntity<AcademicTermResponse> createAcademicTerm(@Valid @RequestBody AcademicTermCreateRequest request) {
        log.debug("Creating academic term: {} {}", request.getYear(), request.getTermType());
        AcademicTerm term = academicTermUseCase.createAcademicTerm(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(AcademicTermResponse.from(term));
    }

    @GetMapping("/{id}")
    @Operation(summary = "학기 조회", description = "ID로 학기를 조회합니다.")
    public ResponseEntity<AcademicTermResponse> getAcademicTerm(@PathVariable Long id) {
        log.debug("Getting academic term: {}", id);
        AcademicTerm term = academicTermUseCase.getAcademicTerm(id);
        return ResponseEntity.ok(AcademicTermResponse.from(term));
    }

    @GetMapping("/current")
    @Operation(summary = "현재 학기 조회", description = "현재 진행 중인 학기를 조회합니다.")
    public ResponseEntity<AcademicTermResponse> getCurrentTerm() {
        log.debug("Getting current term");
        return academicTermUseCase.getCurrentTerm()
                .map(term -> ResponseEntity.ok(AcademicTermResponse.from(term)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "모든 학기 조회", description = "모든 학기 목록을 조회합니다.")
    public ResponseEntity<List<AcademicTermResponse>> getAllTerms() {
        log.debug("Getting all terms");
        List<AcademicTerm> terms = academicTermUseCase.getAllTerms();
        return ResponseEntity.ok(AcademicTermResponse.from(terms));
    }

    @GetMapping("/year/{year}")
    @Operation(summary = "연도별 학기 조회", description = "특정 연도의 학기 목록을 조회합니다.")
    public ResponseEntity<List<AcademicTermResponse>> getTermsByYear(@PathVariable Integer year) {
        log.debug("Getting terms by year: {}", year);
        List<AcademicTerm> terms = academicTermUseCase.getTermsByYear(year);
        return ResponseEntity.ok(AcademicTermResponse.from(terms));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "학기 수정", description = "학기 정보를 수정합니다.")
    public ResponseEntity<AcademicTermResponse> updateAcademicTerm(
            @PathVariable Long id,
            @Valid @RequestBody AcademicTermUpdateRequest request) {
        log.debug("Updating academic term: {}", id);
        AcademicTerm term = academicTermUseCase.updateAcademicTerm(id, request);
        return ResponseEntity.ok(AcademicTermResponse.from(term));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "학기 삭제", description = "학기를 삭제합니다.")
    public ResponseEntity<Void> deleteAcademicTerm(@PathVariable Long id) {
        log.debug("Deleting academic term: {}", id);
        academicTermUseCase.deleteAcademicTerm(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/health")
    @Operation(summary = "헬스체크", description = "서비스 상태를 확인합니다.")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "organization-service"));
    }
}
