package com.mzc.lms.catalog.adapter.in.web;

import com.mzc.lms.catalog.adapter.in.web.dto.CourseTypeResponse;
import com.mzc.lms.catalog.application.port.in.CourseTypeUseCase;
import com.mzc.lms.catalog.domain.model.CourseType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/course-types")
@RequiredArgsConstructor
@Tag(name = "CourseType", description = "이수구분 관리 API")
public class CourseTypeController {

    private final CourseTypeUseCase courseTypeUseCase;

    @GetMapping("/{id}")
    @Operation(summary = "이수구분 조회", description = "ID로 이수구분을 조회합니다")
    public ResponseEntity<CourseTypeResponse> getCourseType(@PathVariable Long id) {
        return courseTypeUseCase.getCourseType(id)
                .map(courseType -> ResponseEntity.ok(CourseTypeResponse.from(courseType)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/code/{typeCode}")
    @Operation(summary = "이수구분 코드로 조회", description = "이수구분 코드로 조회합니다")
    public ResponseEntity<CourseTypeResponse> getCourseTypeByCode(@PathVariable Integer typeCode) {
        return courseTypeUseCase.getCourseTypeByTypeCode(typeCode)
                .map(courseType -> ResponseEntity.ok(CourseTypeResponse.from(courseType)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "전체 이수구분 조회", description = "모든 이수구분을 조회합니다")
    public ResponseEntity<List<CourseTypeResponse>> getAllCourseTypes() {
        List<CourseType> courseTypes = courseTypeUseCase.getAllCourseTypes();
        List<CourseTypeResponse> responses = courseTypes.stream()
                .map(CourseTypeResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "카테고리별 이수구분 조회", description = "카테고리별 이수구분을 조회합니다 (0: 전공, 1: 교양)")
    public ResponseEntity<List<CourseTypeResponse>> getCourseTypesByCategory(@PathVariable Integer category) {
        List<CourseType> courseTypes = courseTypeUseCase.getCourseTypesByCategory(category);
        List<CourseTypeResponse> responses = courseTypes.stream()
                .map(CourseTypeResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/major")
    @Operation(summary = "전공 이수구분 조회", description = "전공 관련 이수구분을 조회합니다")
    public ResponseEntity<List<CourseTypeResponse>> getMajorCourseTypes() {
        List<CourseType> courseTypes = courseTypeUseCase.getCourseTypesByCategory(CourseType.CATEGORY_MAJOR);
        List<CourseTypeResponse> responses = courseTypes.stream()
                .map(CourseTypeResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/general")
    @Operation(summary = "교양 이수구분 조회", description = "교양 관련 이수구분을 조회합니다")
    public ResponseEntity<List<CourseTypeResponse>> getGeneralCourseTypes() {
        List<CourseType> courseTypes = courseTypeUseCase.getCourseTypesByCategory(CourseType.CATEGORY_GENERAL);
        List<CourseTypeResponse> responses = courseTypes.stream()
                .map(CourseTypeResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
}
