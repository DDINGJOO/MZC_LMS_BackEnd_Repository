package com.mzc.backend.lms.domains.enrollment.application.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.mzc.backend.lms.domains.course.constants.CourseConstants;
import com.mzc.backend.lms.domains.enrollment.adapter.in.web.dto.common.*;
import com.mzc.backend.lms.domains.enrollment.adapter.in.web.dto.request.*;
import com.mzc.backend.lms.domains.enrollment.adapter.in.web.dto.response.*;
import com.mzc.backend.lms.domains.enrollment.adapter.out.persistence.entity.Enrollment;
import com.mzc.backend.lms.domains.enrollment.application.port.in.EnrollmentUseCase;
import com.mzc.backend.lms.domains.enrollment.application.port.out.*;
import com.mzc.backend.lms.domains.enrollment.application.port.out.CoursePort.CourseInfo;
import com.mzc.backend.lms.domains.enrollment.application.port.out.CoursePort.ScheduleInfo;
import com.mzc.backend.lms.domains.enrollment.application.port.out.EnrollmentPeriodPort.PeriodInfo;
import com.mzc.backend.lms.domains.enrollment.domain.exception.EnrollmentException;

/**
 * 수강신청 UseCase 구현체
 *
 * Course, User 도메인과의 통신은 Port를 통해 수행 (도메인 간 순환 의존성 방지)
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EnrollmentUseCaseImpl implements EnrollmentUseCase {

    // Ports - 다른 도메인과의 통신
    private final CoursePort coursePort;
    private final StudentPort studentPort;
    private final EnrollmentPeriodPort periodPort;

    // Repository Ports - 영속성
    private final EnrollmentRepositoryPort enrollmentRepository;
    private final CartRepositoryPort cartRepository;

    // Event Port
    private final EnrollmentEventPort eventPort;

    private static final int MAX_CREDITS_PER_TERM = 21;

    @Override
    public EnrollmentBulkResponseDto enrollBulk(CourseIdsRequestDto request, Long studentId) {
        // 1. 수강신청 기간 체크
        if (!periodPort.isEnrollmentPeriodActive()) {
            throw EnrollmentException.notEnrollmentPeriod();
        }

        // 2. 학생 존재 여부 확인 (StudentPort 사용)
        if (!studentPort.existsById(studentId)) {
            throw EnrollmentException.studentNotFound(studentId);
        }

        // 3. 과목 존재 여부 체크
        List<Long> courseIds = request.getCourseIds();
        if (courseIds == null || courseIds.isEmpty()) {
            throw EnrollmentException.emptyCourseList();
        }

        List<CourseInfo> courses = coursePort.getCourses(courseIds);
        if (courses.size() != courseIds.size()) {
            throw EnrollmentException.courseNotExists(null);
        }

        // 4. 기존 수강신청 정보 조회
        List<Enrollment> existingEnrollments = enrollmentRepository.findByStudentId(studentId);
        Set<Long> enrolledCourseIds = existingEnrollments.stream()
                .map(Enrollment::getCourseId)
                .collect(Collectors.toSet());

        // 과목 정보 조회하여 subjectId 수집
        List<CourseInfo> enrolledCourseInfos = coursePort.getCourses(
                existingEnrollments.stream()
                        .map(Enrollment::getCourseId)
                        .toList()
        );
        Set<Long> enrolledSubjectIds = enrolledCourseInfos.stream()
                .map(CourseInfo::subjectId)
                .collect(Collectors.toSet());

        // 5. 현재 수강 학점 계산
        int currentCredits = enrolledCourseInfos.stream()
                .mapToInt(CourseInfo::credits)
                .sum();

        // 6. 기존 스케줄 수집
        List<ScheduleInfo> existingSchedules = enrolledCourseInfos.stream()
                .flatMap(info -> info.schedules().stream())
                .toList();

        // 7. 각 강의에 대해 검증 및 수강신청
        List<EnrollmentBulkResponseDto.SucceededEnrollmentDto> succeeded = new ArrayList<>();
        List<EnrollmentBulkResponseDto.FailedEnrollmentDto> failed = new ArrayList<>();
        int enrolledCredits = 0;
        LocalDateTime now = LocalDateTime.now();

        for (Long courseId : courseIds) {
            try {
                // 비관적 락으로 Course 조회
                CourseInfo course = coursePort.getCourseWithLock(courseId);

                // 정원 체크
                if (course.isFull()) {
                    failed.add(createFailedDto(course, "COURSE_FULL", "수강 정원이 마감되었습니다"));
                    continue;
                }

                // 개별 검증
                String errorCode = validateEnrollment(course, studentId, enrolledCourseIds,
                        enrolledSubjectIds, existingSchedules, currentCredits + enrolledCredits);

                if (errorCode != null) {
                    failed.add(createFailedDto(course, errorCode, getErrorMessage(errorCode)));
                    continue;
                }

                // 수강신청 처리 (studentId 직접 사용)
                Enrollment enrollment = Enrollment.builder()
                        .studentId(studentId)
                        .courseId(courseId)
                        .enrolledAt(now)
                        .build();

                Enrollment saved = enrollmentRepository.save(enrollment);

                // 정원 증가
                coursePort.increaseCurrentStudents(courseId);

                // 장바구니에서 제거
                cartRepository.findByStudentIdAndCourseId(studentId, courseId)
                        .ifPresent(cartRepository::delete);

                // 이벤트 발행
                eventPort.publishEnrollmentCreated(studentId, courseId,
                        course.subjectName(), course.sectionNumber());

                // 성공 처리
                succeeded.add(EnrollmentBulkResponseDto.SucceededEnrollmentDto.builder()
                        .enrollmentId(saved.getId())
                        .courseId(course.id())
                        .courseCode(course.subjectCode())
                        .courseName(course.subjectName())
                        .section(course.sectionNumber())
                        .credits(course.credits())
                        .enrolledAt(saved.getEnrolledAt())
                        .build());

                enrolledCredits += course.credits();
                enrolledCourseIds.add(course.id());
                enrolledSubjectIds.add(course.subjectId());
                existingSchedules.addAll(course.schedules());

            } catch (Exception e) {
                log.error("수강신청 처리 중 오류: courseId={}, error={}", courseId, e.getMessage(), e);
                coursePort.getCourses(List.of(courseId)).stream().findFirst()
                        .ifPresent(c -> failed.add(createFailedDto(c, "ENROLLMENT_ERROR", e.getMessage())));
            }
        }

        // 8. 응답 생성
        return EnrollmentBulkResponseDto.builder()
                .summary(EnrollmentBulkResponseDto.SummaryDto.builder()
                        .totalAttempted(courseIds.size())
                        .successCount(succeeded.size())
                        .failedCount(failed.size())
                        .enrolledCredits(enrolledCredits)
                        .totalCredits(currentCredits + enrolledCredits)
                        .build())
                .succeeded(succeeded)
                .failed(failed)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public MyEnrollmentsResponseDto getMyEnrollments(Long studentId, Long enrollmentPeriodId) {
        // 1. 기간 정보 조회
        PeriodInfo period;
        if (enrollmentPeriodId != null) {
            period = periodPort.getPeriod(enrollmentPeriodId);
        } else {
            period = periodPort.getCurrentActivePeriod();
        }

        // 2. 해당 학기 수강신청 목록 조회
        List<Enrollment> enrollments = enrollmentRepository
                .findByStudentIdAndAcademicTermId(studentId, period.academicTermId());

        // 3. 학기 정보
        String termName = String.format("%d학년도 %s", period.year(), getTermTypeName(period.termType()));
        MyEnrollmentsResponseDto.TermDto termDto = MyEnrollmentsResponseDto.TermDto.builder()
                .id(period.academicTermId())
                .year(period.year())
                .termType(period.termType())
                .termName(termName)
                .build();

        // 4. 요약 정보
        List<CourseInfo> enrolledCourses = coursePort.getCourses(
                enrollments.stream()
                        .map(Enrollment::getCourseId)
                        .toList()
        );
        int totalCredits = enrolledCourses.stream()
                .mapToInt(CourseInfo::credits)
                .sum();

        MyEnrollmentsResponseDto.SummaryDto summary = MyEnrollmentsResponseDto.SummaryDto.builder()
                .totalCourses(enrollments.size())
                .totalCredits(totalCredits)
                .maxCredits(MAX_CREDITS_PER_TERM)
                .remainingCredits(Math.max(0, MAX_CREDITS_PER_TERM - totalCredits))
                .build();

        // 5. 취소 가능 여부
        boolean canCancel = periodPort.isCancelPeriodActive();

        // 6. 수강신청 목록 변환
        List<MyEnrollmentsResponseDto.EnrollmentItemDto> items = enrollments.stream()
                .map(e -> convertToEnrollmentItem(e, canCancel))
                .toList();

        return MyEnrollmentsResponseDto.builder()
                .term(termDto)
                .summary(summary)
                .enrollments(items)
                .build();
    }

    @Override
    public EnrollmentBulkCancelResponseDto cancelBulk(EnrollmentBulkCancelRequestDto request, Long studentId) {
        // 1. 취소 가능 기간 체크
        if (!periodPort.isCancelPeriodActive()) {
            throw EnrollmentException.cancelPeriodEnded();
        }

        List<Long> enrollmentIds = request.getEnrollmentIds();
        if (enrollmentIds == null || enrollmentIds.isEmpty()) {
            throw EnrollmentException.emptyCourseList();
        }

        List<EnrollmentBulkCancelResponseDto.CancelledEnrollmentDto> cancelled = new ArrayList<>();
        List<EnrollmentBulkCancelResponseDto.FailedCancelDto> failed = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (Long enrollmentId : enrollmentIds) {
            try {
                Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                        .orElseThrow(() -> EnrollmentException.enrollmentNotFound(enrollmentId));

                // 본인 확인 (studentId로 비교)
                if (!enrollment.getStudentId().equals(studentId)) {
                    failed.add(EnrollmentBulkCancelResponseDto.FailedCancelDto.builder()
                            .enrollmentId(enrollmentId)
                            .courseId(enrollment.getCourseId())
                            .errorCode("UNAUTHORIZED")
                            .message("본인의 수강신청만 취소할 수 있습니다")
                            .build());
                    continue;
                }

                Long courseId = enrollment.getCourseId();
                CourseInfo course = coursePort.getCourse(courseId);

                // 정원 감소
                coursePort.decreaseCurrentStudents(courseId);

                // 수강신청 삭제
                enrollmentRepository.delete(enrollment);

                // 이벤트 발행
                eventPort.publishEnrollmentCancelled(studentId, courseId,
                        course.subjectName(), course.sectionNumber());

                cancelled.add(EnrollmentBulkCancelResponseDto.CancelledEnrollmentDto.builder()
                        .enrollmentId(enrollmentId)
                        .courseId(courseId)
                        .courseCode(course.subjectCode())
                        .courseName(course.subjectName())
                        .credits(course.credits())
                        .cancelledAt(now)
                        .build());

            } catch (Exception e) {
                log.error("수강신청 취소 실패: enrollmentId={}, error={}", enrollmentId, e.getMessage());
                failed.add(EnrollmentBulkCancelResponseDto.FailedCancelDto.builder()
                        .enrollmentId(enrollmentId)
                        .errorCode("CANCEL_ERROR")
                        .message(e.getMessage())
                        .build());
            }
        }

        // 남은 수강신청 요약
        List<Enrollment> remaining = enrollmentRepository.findByStudentId(studentId);
        List<CourseInfo> remainingCourses = coursePort.getCourses(
                remaining.stream()
                        .map(Enrollment::getCourseId)
                        .toList()
        );
        int totalCredits = remainingCourses.stream()
                .mapToInt(CourseInfo::credits)
                .sum();

        return EnrollmentBulkCancelResponseDto.builder()
                .summary(EnrollmentBulkCancelResponseDto.SummaryDto.builder()
                        .totalAttempted(enrollmentIds.size())
                        .successCount(cancelled.size())
                        .failedCount(failed.size())
                        .build())
                .cancelled(cancelled)
                .failed(failed)
                .enrollmentSummary(EnrollmentBulkCancelResponseDto.EnrollmentSummaryDto.builder()
                        .totalCourses(remaining.size())
                        .totalCredits(totalCredits)
                        .build())
                .build();
    }

    // === Private Methods ===

    private String validateEnrollment(CourseInfo course, Long studentId,
                                      Set<Long> enrolledCourseIds, Set<Long> enrolledSubjectIds,
                                      List<ScheduleInfo> existingSchedules, int currentTotalCredits) {
        // 이미 수강신청
        if (enrolledCourseIds.contains(course.id())) {
            return "ALREADY_ENROLLED";
        }

        // 동일 과목 다른 분반
        if (enrolledSubjectIds.contains(course.subjectId())) {
            return "DUPLICATE_SUBJECT";
        }

        // 시간표 충돌
        for (ScheduleInfo newSchedule : course.schedules()) {
            for (ScheduleInfo existing : existingSchedules) {
                if (hasScheduleConflict(newSchedule, existing)) {
                    return "TIME_CONFLICT";
                }
            }
        }

        // 선수과목
        List<Long> prerequisiteSubjectIds = coursePort.getMandatoryPrerequisiteSubjectIds(course.subjectId());
        if (!enrolledSubjectIds.containsAll(prerequisiteSubjectIds)) {
            return "PREREQUISITE_NOT_MET";
        }

        // 학점 제한
        if (currentTotalCredits + course.credits() > MAX_CREDITS_PER_TERM) {
            return "CREDIT_LIMIT_EXCEEDED";
        }

        return null;
    }

    private boolean hasScheduleConflict(ScheduleInfo s1, ScheduleInfo s2) {
        if (!s1.dayOfWeek().equals(s2.dayOfWeek())) {
            return false;
        }
        return s1.startTime().isBefore(s2.endTime()) && s2.startTime().isBefore(s1.endTime());
    }

    private EnrollmentBulkResponseDto.FailedEnrollmentDto createFailedDto(CourseInfo course, String errorCode, String message) {
        return EnrollmentBulkResponseDto.FailedEnrollmentDto.builder()
                .courseId(course.id())
                .courseCode(course.subjectCode())
                .courseName(course.subjectName())
                .section(course.sectionNumber())
                .errorCode(errorCode)
                .message(message)
                .build();
    }

    private String getErrorMessage(String errorCode) {
        return switch (errorCode) {
            case "ALREADY_ENROLLED" -> "이미 수강신청했습니다";
            case "DUPLICATE_SUBJECT" -> "동일 과목 다른 분반 이미 신청";
            case "COURSE_FULL" -> "수강 정원이 마감되었습니다";
            case "TIME_CONFLICT" -> "시간표가 충돌합니다";
            case "PREREQUISITE_NOT_MET" -> "선수과목을 이수하지 않았습니다";
            case "CREDIT_LIMIT_EXCEEDED" -> "학점 제한을 초과합니다";
            default -> "수강신청에 실패했습니다";
        };
    }

    private MyEnrollmentsResponseDto.EnrollmentItemDto convertToEnrollmentItem(Enrollment enrollment, boolean canCancel) {
        CourseInfo course = coursePort.getCourse(enrollment.getCourseId());
        String professorName = studentPort.getUserName(course.professorId());

        List<ScheduleDto> schedules = course.schedules().stream()
                .map(s -> ScheduleDto.builder()
                        .dayOfWeek(s.dayOfWeek().getValue())
                        .dayName(CourseConstants.DAY_NAME_MAP.get(s.dayOfWeek()))
                        .startTime(s.startTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")))
                        .endTime(s.endTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")))
                        .classroom(s.classroom())
                        .build())
                .sorted(Comparator.comparing(ScheduleDto::getDayOfWeek)
                        .thenComparing(ScheduleDto::getStartTime))
                .toList();

        return MyEnrollmentsResponseDto.EnrollmentItemDto.builder()
                .enrollmentId(enrollment.getId())
                .course(MyEnrollmentsResponseDto.CourseInfoDto.builder()
                        .id(course.id())
                        .courseCode(course.subjectCode())
                        .courseName(course.subjectName())
                        .section(course.sectionNumber())
                        .credits(course.credits())
                        .courseType(CourseTypeDto.builder()
                                .code(course.courseTypeCode())
                                .name(course.courseTypeName())
                                .color(CourseConstants.getCourseTypeColor(course.courseTypeCode()))
                                .build())
                        .currentStudents(course.currentStudents())
                        .maxStudents(course.maxStudents())
                        .build())
                .professor(ProfessorDto.builder()
                        .id(course.professorId())
                        .name(professorName != null ? professorName : "교수")
                        .build())
                .schedule(schedules)
                .enrolledAt(enrollment.getEnrolledAt())
                .canCancel(canCancel)
                .build();
    }

    private String getTermTypeName(String termType) {
        return switch (termType) {
            case "1" -> "1학기";
            case "2" -> "2학기";
            case "SUMMER" -> "여름학기";
            case "WINTER" -> "겨울학기";
            default -> termType;
        };
    }
}
