package com.mzc.lms.course.adapter.in.web;

import com.mzc.lms.course.adapter.in.web.dto.CourseCreateRequest;
import com.mzc.lms.course.adapter.in.web.dto.CourseResponse;
import com.mzc.lms.course.adapter.in.web.dto.CourseUpdateRequest;
import com.mzc.lms.course.application.port.in.CourseScheduleUseCase;
import com.mzc.lms.course.application.port.in.CourseUseCase;
import com.mzc.lms.course.domain.model.Course;
import com.mzc.lms.course.domain.model.CourseSchedule;
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

import java.util.List;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
@Tag(name = "Course", description = "강좌 관리 API")
public class CourseController {

    private final CourseUseCase courseUseCase;
    private final CourseScheduleUseCase scheduleUseCase;

    @PostMapping
    @Operation(summary = "강좌 생성", description = "새로운 강좌를 생성합니다")
    public ResponseEntity<CourseResponse> createCourse(
            @Valid @RequestBody CourseCreateRequest request
    ) {
        CourseUseCase.CreateCourseCommand command = new CourseUseCase.CreateCourseCommand(
                request.getSubjectId(),
                request.getProfessorId(),
                request.getAcademicTermId(),
                request.getSectionNumber(),
                request.getMaxStudents(),
                request.getDescription()
        );

        Course course = courseUseCase.createCourse(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(CourseResponse.from(course));
    }

    @GetMapping("/{id}")
    @Operation(summary = "강좌 조회", description = "ID로 강좌를 조회합니다")
    public ResponseEntity<CourseResponse> getCourse(@PathVariable Long id) {
        return courseUseCase.getCourse(id)
                .map(course -> {
                    List<CourseSchedule> schedules = scheduleUseCase.getSchedulesByCourse(id);
                    return ResponseEntity.ok(CourseResponse.from(course, schedules));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "강좌 목록 조회", description = "필터 조건에 맞는 강좌 목록을 조회합니다")
    public ResponseEntity<Page<CourseResponse>> getCourses(
            @RequestParam(required = false) Long subjectId,
            @RequestParam(required = false) Long professorId,
            @RequestParam(required = false) Long academicTermId,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) Boolean hasAvailableSeats,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        CourseUseCase.CourseSearchCriteria criteria = new CourseUseCase.CourseSearchCriteria(
                subjectId, professorId, academicTermId, isActive, hasAvailableSeats
        );

        Page<Course> courses = courseUseCase.getCourses(criteria, pageable);
        return ResponseEntity.ok(courses.map(CourseResponse::from));
    }

    @GetMapping("/subject/{subjectId}")
    @Operation(summary = "과목별 강좌 조회", description = "특정 과목의 강좌 목록을 조회합니다")
    public ResponseEntity<List<CourseResponse>> getCoursesBySubject(@PathVariable Long subjectId) {
        List<Course> courses = courseUseCase.getCoursesBySubject(subjectId);
        return ResponseEntity.ok(courses.stream().map(CourseResponse::from).toList());
    }

    @GetMapping("/professor/{professorId}")
    @Operation(summary = "교수별 강좌 조회", description = "특정 교수의 강좌 목록을 조회합니다")
    public ResponseEntity<List<CourseResponse>> getCoursesByProfessor(@PathVariable Long professorId) {
        List<Course> courses = courseUseCase.getCoursesByProfessor(professorId);
        return ResponseEntity.ok(courses.stream().map(CourseResponse::from).toList());
    }

    @GetMapping("/term/{academicTermId}")
    @Operation(summary = "학기별 강좌 조회", description = "특정 학기의 강좌 목록을 조회합니다")
    public ResponseEntity<List<CourseResponse>> getCoursesByTerm(@PathVariable Long academicTermId) {
        List<Course> courses = courseUseCase.getCoursesByAcademicTerm(academicTermId);
        return ResponseEntity.ok(courses.stream().map(CourseResponse::from).toList());
    }

    @GetMapping("/subject/{subjectId}/term/{academicTermId}")
    @Operation(summary = "과목-학기별 강좌 조회", description = "특정 과목의 특정 학기 강좌 목록을 조회합니다")
    public ResponseEntity<List<CourseResponse>> getCoursesBySubjectAndTerm(
            @PathVariable Long subjectId,
            @PathVariable Long academicTermId
    ) {
        List<Course> courses = courseUseCase.getCoursesBySubjectAndTerm(subjectId, academicTermId);
        return ResponseEntity.ok(courses.stream().map(CourseResponse::from).toList());
    }

    @PutMapping("/{id}")
    @Operation(summary = "강좌 수정", description = "강좌 정보를 수정합니다")
    public ResponseEntity<CourseResponse> updateCourse(
            @PathVariable Long id,
            @Valid @RequestBody CourseUpdateRequest request
    ) {
        CourseUseCase.UpdateCourseCommand command = new CourseUseCase.UpdateCourseCommand(
                request.getProfessorId(),
                request.getMaxStudents(),
                request.getDescription()
        );

        Course course = courseUseCase.updateCourse(id, command);
        return ResponseEntity.ok(CourseResponse.from(course));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "강좌 삭제", description = "강좌를 삭제합니다")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseUseCase.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "강좌 비활성화", description = "강좌를 비활성화합니다")
    public ResponseEntity<CourseResponse> deactivateCourse(@PathVariable Long id) {
        Course course = courseUseCase.deactivateCourse(id);
        return ResponseEntity.ok(CourseResponse.from(course));
    }

    @PatchMapping("/{id}/increment-students")
    @Operation(summary = "수강생 수 증가", description = "강좌의 현재 수강생 수를 1 증가시킵니다")
    public ResponseEntity<CourseResponse> incrementStudents(@PathVariable Long id) {
        Course course = courseUseCase.incrementStudentCount(id);
        return ResponseEntity.ok(CourseResponse.from(course));
    }

    @PatchMapping("/{id}/decrement-students")
    @Operation(summary = "수강생 수 감소", description = "강좌의 현재 수강생 수를 1 감소시킵니다")
    public ResponseEntity<CourseResponse> decrementStudents(@PathVariable Long id) {
        Course course = courseUseCase.decrementStudentCount(id);
        return ResponseEntity.ok(CourseResponse.from(course));
    }
}
