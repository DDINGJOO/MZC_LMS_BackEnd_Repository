package com.mzc.lms.organization.adapter.in.web;

import com.mzc.lms.organization.adapter.in.web.dto.CollegeCreateRequest;
import com.mzc.lms.organization.adapter.in.web.dto.CollegeResponse;
import com.mzc.lms.organization.adapter.in.web.dto.CollegeUpdateRequest;
import com.mzc.lms.organization.application.port.in.CollegeUseCase;
import com.mzc.lms.organization.domain.model.College;
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
@RequestMapping("/colleges")
@RequiredArgsConstructor
@Tag(name = "College", description = "단과대학 API")
public class CollegeController {

    private final CollegeUseCase collegeUseCase;

    @PostMapping
    @Operation(summary = "단과대학 생성", description = "새로운 단과대학을 생성합니다.")
    public ResponseEntity<CollegeResponse> createCollege(@Valid @RequestBody CollegeCreateRequest request) {
        log.debug("Creating college: {}", request.getCollegeName());
        College college = collegeUseCase.createCollege(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(CollegeResponse.from(college));
    }

    @GetMapping("/{id}")
    @Operation(summary = "단과대학 조회", description = "ID로 단과대학을 조회합니다.")
    public ResponseEntity<CollegeResponse> getCollege(@PathVariable Long id) {
        log.debug("Getting college: {}", id);
        College college = collegeUseCase.getCollege(id);
        return ResponseEntity.ok(CollegeResponse.from(college));
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "코드로 단과대학 조회", description = "코드로 단과대학을 조회합니다.")
    public ResponseEntity<CollegeResponse> getCollegeByCode(@PathVariable String code) {
        log.debug("Getting college by code: {}", code);
        College college = collegeUseCase.getCollegeByCode(code);
        return ResponseEntity.ok(CollegeResponse.from(college));
    }

    @GetMapping
    @Operation(summary = "모든 단과대학 조회", description = "모든 단과대학 목록을 조회합니다.")
    public ResponseEntity<List<CollegeResponse>> getAllColleges() {
        log.debug("Getting all colleges");
        List<College> colleges = collegeUseCase.getAllColleges();
        return ResponseEntity.ok(CollegeResponse.from(colleges));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "단과대학 수정", description = "단과대학 정보를 수정합니다.")
    public ResponseEntity<CollegeResponse> updateCollege(
            @PathVariable Long id,
            @Valid @RequestBody CollegeUpdateRequest request) {
        log.debug("Updating college: {}", id);
        College college = collegeUseCase.updateCollege(id, request);
        return ResponseEntity.ok(CollegeResponse.from(college));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "단과대학 삭제", description = "단과대학을 삭제합니다.")
    public ResponseEntity<Void> deleteCollege(@PathVariable Long id) {
        log.debug("Deleting college: {}", id);
        collegeUseCase.deleteCollege(id);
        return ResponseEntity.noContent().build();
    }
}
