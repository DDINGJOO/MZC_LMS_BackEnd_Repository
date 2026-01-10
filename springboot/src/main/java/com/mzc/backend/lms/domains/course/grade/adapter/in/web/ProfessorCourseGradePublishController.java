package com.mzc.backend.lms.domains.course.grade.adapter.in.web;

import com.mzc.backend.lms.domains.course.course.adapter.out.persistence.entity.Course;
import com.mzc.backend.lms.domains.course.course.adapter.out.persistence.repository.CourseRepository;
import com.mzc.backend.lms.domains.course.exception.CourseException;
import com.mzc.backend.lms.domains.course.grade.application.service.GradePublishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/professor/courses")
@RequiredArgsConstructor
public class ProfessorCourseGradePublishController {

    private final GradePublishService gradePublishService;
    private final CourseRepository courseRepository;

    /**
     * 특정 강의 성적 산출(점수 계산) 수동 실행
     * - 담당 교수만 실행 가능
     * - 성적산출기간(GRADE_CALCULATION) 진행 중에만 실행 가능
     */
    @PostMapping("/{courseId}/grades/calculate")
    public ResponseEntity<?> calculateCourse(
            @PathVariable Long courseId,
            @AuthenticationPrincipal Long professorId
    ) {
        try {
            if (courseId == null) {
                throw CourseException.courseIdRequired();
            }
            if (professorId == null) {
                return ResponseEntity.status(401).body(error("인증이 필요합니다."));
            }

            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> CourseException.courseNotFound(courseId));
            if (course.getProfessor() == null || course.getProfessor().getProfessorId() == null
                    || !course.getProfessor().getProfessorId().equals(professorId)) {
                throw CourseException.gradeCalculateNotAuthorized();
            }

            gradePublishService.calculateCourseIfAllowed(courseId, LocalDateTime.now());
            return ResponseEntity.ok(success(null, "성적 산출 처리를 실행했습니다. (강의 단위)"));
        } catch (CourseException e) {
            return ResponseEntity.status(e.getErrorCode().getStatus()).body(error(e.getMessage()));
        } catch (Exception e) {
            log.error("강의 성적 산출 실행 실패 courseId={}", courseId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error("서버 오류"));
        }
    }

    /**
     * 특정 강의 성적 산출/공개 수동 실행
     * - 담당 교수만 실행 가능
     * - 성적산출기간(GRADE_CALCULATION) 종료 이후에만 실행 가능
     */
    @PostMapping("/{courseId}/grades/publish")
    public ResponseEntity<?> publishCourse(
            @PathVariable Long courseId,
            @AuthenticationPrincipal Long professorId
    ) {
        try {
            if (courseId == null) {
                throw CourseException.courseIdRequired();
            }
            if (professorId == null) {
                return ResponseEntity.status(401).body(error("인증이 필요합니다."));
            }

            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> CourseException.courseNotFound(courseId));
            if (course.getProfessor() == null || course.getProfessor().getProfessorId() == null
                    || !course.getProfessor().getProfessorId().equals(professorId)) {
                throw CourseException.gradePublishNotAuthorized();
            }

            gradePublishService.publishCourseIfAllowed(courseId, LocalDateTime.now());
            return ResponseEntity.ok(success(null, "성적 공개 처리를 실행했습니다. (강의 단위)"));
        } catch (CourseException e) {
            return ResponseEntity.status(e.getErrorCode().getStatus()).body(error(e.getMessage()));
        } catch (Exception e) {
            log.error("강의 성적 수동 공개 실행 실패 courseId={}", courseId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error("서버 오류"));
        }
    }

    private Map<String, Object> success(Object data, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", data);
        if (message != null) {
            response.put("message", message);
        }
        return response;
    }

    private Map<String, Object> error(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        return response;
    }
}


