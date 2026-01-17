package com.mzc.backend.lms.domains.enrollment.application.service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.mzc.backend.lms.common.constants.CourseConstants;
import com.mzc.backend.lms.domains.enrollment.adapter.in.web.dto.common.*;
import com.mzc.backend.lms.domains.enrollment.adapter.in.web.dto.request.*;
import com.mzc.backend.lms.domains.enrollment.adapter.in.web.dto.response.*;
import com.mzc.backend.lms.domains.enrollment.application.port.in.CartUseCase;
import com.mzc.backend.lms.domains.enrollment.application.port.out.CartRepositoryPort.CartInfo;
import com.mzc.backend.lms.domains.enrollment.application.port.out.EnrollmentRepositoryPort.EnrollmentInfo;
import com.mzc.backend.lms.domains.enrollment.application.port.out.CartRepositoryPort;
import com.mzc.backend.lms.domains.enrollment.application.port.out.CoursePort;
import com.mzc.backend.lms.domains.enrollment.application.port.out.EnrollmentPeriodPort;
import com.mzc.backend.lms.domains.enrollment.application.port.out.EnrollmentRepositoryPort;
import com.mzc.backend.lms.domains.enrollment.domain.exception.EnrollmentErrorCode;
import com.mzc.backend.lms.domains.enrollment.domain.exception.EnrollmentException;
import com.mzc.backend.lms.views.UserViewService;

/**
 * 장바구니 UseCase 구현체
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CartUseCaseImpl implements CartUseCase {

    private final CartRepositoryPort cartRepositoryPort;
    private final CoursePort coursePort;
    private final EnrollmentRepositoryPort enrollmentRepositoryPort;
    private final EnrollmentPeriodPort enrollmentPeriodPort;
    private final UserViewService userViewService;

    private static final int MAX_CREDITS_PER_TERM = 21; // 학기당 최대 학점

    @Override
    public CartResponseDto getCart(String studentId) {
        Long studentIdLong = Long.parseLong(studentId);

        // 학생의 장바구니 목록 조회
        List<CartInfo> cartItems = cartRepositoryPort.findByStudentId(studentIdLong);

        // 장바구니에 있는 과목들의 ID 수집 및 조회
        List<Long> courseIds = cartItems.stream()
                .map(CartInfo::courseId)
                .collect(Collectors.toList());

        Map<Long, CoursePort.CourseInfo> courseMap = coursePort.getCourses(courseIds).stream()
                .collect(Collectors.toMap(CoursePort.CourseInfo::id, courseInfo -> courseInfo));

        // DTO 변환
        List<CartItemDto> cartItemDtos = cartItems.stream()
                .map(cart -> convertToCartItemDto(cart, courseMap.get(cart.courseId())))
                .collect(Collectors.toList());

        // 총 강의 수 계산
        int totalCourses = cartItemDtos.size();

        // 총 학점 계산
        int totalCredits = cartItemDtos.stream()
                .mapToInt(item -> item.getCourse().getCredits())
                .sum();

        return CartResponseDto.builder()
                .totalCourses(totalCourses)
                .totalCredits(totalCredits)
                .courses(cartItemDtos)
                .build();
    }

    private CartItemDto convertToCartItemDto(CartInfo cart, CoursePort.CourseInfo courseInfo) {

        // 교수 이름 조회
        String professorName = userViewService.getUserName(
                courseInfo.professorId().toString()
        );

        // 스케줄 변환
        List<ScheduleDto> schedules = courseInfo.schedules().stream()
                .map(this::convertToScheduleDto)
                .sorted(Comparator.comparing(ScheduleDto::getDayOfWeek)
                        .thenComparing(ScheduleDto::getStartTime))
                .collect(Collectors.toList());

        // 강의 정보 DTO
        CartItemDto.CourseInfoDto courseInfoDto = CartItemDto.CourseInfoDto.builder()
                .id(courseInfo.id())
                .code(courseInfo.subjectCode())
                .name(courseInfo.subjectName())
                .section(courseInfo.sectionNumber())
                .credits(courseInfo.credits())
                .courseType(courseInfo.courseTypeName())
                .currentStudents(courseInfo.currentStudents())  // 수강인원 추가
                .maxStudents(courseInfo.maxStudents())          // 전체 인원 추가
                .build();

        // 교수 정보 DTO
        ProfessorDto professorDto = ProfessorDto.builder()
                .id(courseInfo.professorId())
                .name(professorName != null ? professorName : "교수")
                .build();

        // 수강신청 정보 DTO
        EnrollmentDto enrollmentDto = EnrollmentDto.builder()
                .current(courseInfo.currentStudents())
                .max(courseInfo.maxStudents())
                .isFull(courseInfo.isFull())
                .build();

        return CartItemDto.builder()
                .cartId(cart.id())
                .course(courseInfoDto)
                .professor(professorDto)
                .schedule(schedules)
                .enrollment(enrollmentDto)
                .addedAt(cart.addedAt())
                .build();
    }

    private ScheduleDto convertToScheduleDto(CoursePort.ScheduleInfo schedule) {
        DayOfWeek dayOfWeek = schedule.dayOfWeek();

        LocalTime startTime = schedule.startTime();
        LocalTime endTime = schedule.endTime();
        return ScheduleDto.builder()
                .dayOfWeek(dayOfWeek.getValue())
                .dayName(CourseConstants.DAY_NAME_MAP.get(dayOfWeek))
                .startTime(startTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")))
                .endTime(endTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")))
                .classroom(schedule.classroom())
                .build();
    }

    @Override
    public CartBulkAddResponseDto addToCartBulk(CourseIdsRequestDto request, String studentId) {

        // 1. 수강신청 기간 체크
        if (!isEnrollmentPeriodActive()) {
            throw EnrollmentException.notEnrollmentPeriod();
        }

        Long studentIdLong = Long.parseLong(studentId);

        // 2. 과목 존재 여부 체크 및 조회
        List<Long> courseIds = request.getCourseIds();
        if (courseIds == null || courseIds.isEmpty()) {
            throw EnrollmentException.emptyCourseList();
        }

        List<CoursePort.CourseInfo> courses = coursePort.getCourses(courseIds);
        if (courses.size() != courseIds.size()) {
            throw EnrollmentException.courseNotExists(null);
        }

        // 3. 기존 장바구니 및 수강신청 정보 조회
        List<CartInfo> existingCarts = cartRepositoryPort.findByStudentId(studentIdLong);

        Set<Long> cartCourseIds = existingCarts.stream()
                .map(CartInfo::courseId)
                .collect(Collectors.toSet());

        // 기존 장바구니의 Course 정보 조회
        List<Long> existingCartCourseIds = existingCarts.stream()
                .map(CartInfo::courseId)
                .collect(Collectors.toList());

        List<CoursePort.CourseInfo> existingCartCourses = existingCartCourseIds.isEmpty()
                ? new ArrayList<>()
                : coursePort.getCourses(existingCartCourseIds);

        // 장바구니에 있는 과목의 subject_id도 체크용으로 수집
        Set<Long> cartSubjectIds = existingCartCourses.stream()
                .map(CoursePort.CourseInfo::subjectId)
                .collect(Collectors.toSet());

        // 수강신청한 강의 조회 (선수과목 체크용)
        List<Long> enrolledCourseIds = enrollmentRepositoryPort.findByStudentId(studentIdLong).stream()
                .map(EnrollmentInfo::courseId)
                .collect(Collectors.toList());

        Set<Long> enrolledCourseIdSet = new HashSet<>(enrolledCourseIds);

        List<CoursePort.CourseInfo> existingEnrollmentCourses = enrolledCourseIds.isEmpty()
                ? new ArrayList<>()
                : coursePort.getCourses(enrolledCourseIds);

        Set<Long> enrolledSubjectIds = existingEnrollmentCourses.stream()
                .map(CoursePort.CourseInfo::subjectId)
                .collect(Collectors.toSet());

        // 4. 각 강의에 대한 검증
        Set<Long> newSubjectIds = new HashSet<>(); // 새로 추가하려는 강의들의 subject_id
        List<String> validationErrors = new ArrayList<>();

        for (CoursePort.CourseInfo course : courses) {
            Long subjectId = course.subjectId();

            // 4-1. 이미 장바구니에 있는지 체크
            if (cartCourseIds.contains(course.id())) {
                validationErrors.add(String.format("강의 %s(%s)는 이미 장바구니에 있습니다.",
                        course.subjectName(), course.subjectCode()));
                continue;
            }

            // 4-2. 이미 수강신청했는지 체크
            if (enrolledCourseIdSet.contains(course.id())) {
                validationErrors.add(String.format("강의 %s(%s)는 이미 수강신청했습니다.",
                        course.subjectName(), course.subjectCode()));
                continue;
            }

            // 4-3. 동일 과목 다른 분반 체크 (장바구니 + 수강신청 + 새로 추가하는 강의들 간)
            if (cartSubjectIds.contains(subjectId) || enrolledSubjectIds.contains(subjectId)) {
                validationErrors.add(String.format("강의 %s(%s)는 이미 다른 분반이 장바구니나 수강신청 목록에 있습니다.",
                        course.subjectName(), course.subjectCode()));
                continue;
            }

            // 새로 추가하는 강의들 간 중복 체크
            if (newSubjectIds.contains(subjectId)) {
                validationErrors.add(String.format("강의 %s(%s)는 같은 요청에 중복된 과목입니다.",
                        course.subjectName(), course.subjectCode()));
                continue;
            }

            newSubjectIds.add(subjectId);

            // 4-4. 선수과목 이수 여부 체크
            List<CoursePort.PrerequisiteInfo> prerequisites = coursePort.getPrerequisites(subjectId);
            for (CoursePort.PrerequisiteInfo prerequisite : prerequisites) {
                Long prerequisiteSubjectId = prerequisite.prerequisiteSubjectId();
                // 선수과목을 이수했는지 확인 (수강신청한 강의 중에서)
                boolean hasPrerequisite = existingEnrollmentCourses.stream()
                        .anyMatch(enrolledCourse -> enrolledCourse.subjectId().equals(prerequisiteSubjectId));

                if (!hasPrerequisite && prerequisite.isMandatory()) {
                    validationErrors.add(String.format("강의 %s(%s)의 필수 선수과목 %s(%s)를 이수하지 않았습니다.",
                            course.subjectName(),
                            course.subjectCode(),
                            prerequisite.prerequisiteSubjectName(),
                            prerequisite.prerequisiteSubjectCode()));
                    break;
                }
            }
        }

        // 하나라도 검증 실패하면 전체 실패
        if (!validationErrors.isEmpty()) {
            throw EnrollmentException.validationFailed(String.join("; ", validationErrors));
        }

        // 5. 학점 제한 체크
        int currentCredits = existingCartCourses.stream()
                .mapToInt(CoursePort.CourseInfo::credits)
                .sum();
        int newCredits = courses.stream()
                .mapToInt(CoursePort.CourseInfo::credits)
                .sum();

        if (currentCredits + newCredits > MAX_CREDITS_PER_TERM) {
            throw EnrollmentException.maxCreditsExceeded(currentCredits, MAX_CREDITS_PER_TERM);
        }

        // 6. 시간표 충돌 체크
        List<CoursePort.ScheduleInfo> existingSchedules = new ArrayList<>();
        for (CoursePort.CourseInfo course : existingCartCourses) {
            existingSchedules.addAll(course.schedules());
        }
        for (CoursePort.CourseInfo course : existingEnrollmentCourses) {
            existingSchedules.addAll(course.schedules());
        }

        // 모든 새 강의의 스케줄 수집
        List<CoursePort.ScheduleInfo> newSchedules = courses.stream()
                .flatMap(course -> course.schedules().stream())
                .collect(Collectors.toList());

        // 기존 강의와의 충돌 체크
        for (CoursePort.ScheduleInfo newSchedule : newSchedules) {
            CoursePort.CourseInfo newCourse = findCourseBySchedule(newSchedule, courses);
            for (CoursePort.ScheduleInfo existingSchedule : existingSchedules) {
                if (hasScheduleConflict(newSchedule, existingSchedule)) {
                    String conflictInfo = String.format("강의 %s(%s)의 시간표가 기존 강의와 충돌합니다.",
                        newCourse.subjectName(), newCourse.subjectCode());
                    throw EnrollmentException.scheduleConflict(conflictInfo);
                }
            }
        }

        // 새로 추가하는 강의들 간의 충돌 체크
        for (int i = 0; i < newSchedules.size(); i++) {
            CoursePort.ScheduleInfo schedule1 = newSchedules.get(i);
            CoursePort.CourseInfo course1 = findCourseBySchedule(schedule1, courses);

            for (int j = i + 1; j < newSchedules.size(); j++) {
                CoursePort.ScheduleInfo schedule2 = newSchedules.get(j);
                CoursePort.CourseInfo course2 = findCourseBySchedule(schedule2, courses);

                if (hasScheduleConflict(schedule1, schedule2) &&
                    !course1.id().equals(course2.id())) {
                    String conflictInfo = String.format("강의 %s(%s)와 %s(%s)의 시간표가 충돌합니다.",
                        course1.subjectName(), course1.subjectCode(),
                        course2.subjectName(), course2.subjectCode());
                    throw EnrollmentException.scheduleConflict(conflictInfo);
                }
            }
        }

        // 7. 모든 검증 통과 - 장바구니에 추가
        LocalDateTime now = LocalDateTime.now();
        List<CartBulkAddResponseDto.SucceededItemDto> succeededItems = new ArrayList<>();

        for (CoursePort.CourseInfo course : courses) {
            Long savedCartId = cartRepositoryPort.saveWithStudentId(studentIdLong, course.id(), now);
            if (savedCartId == null) {
                throw new EnrollmentException(EnrollmentErrorCode.ENROLLMENT_NOT_FOUND);
            }

            succeededItems.add(CartBulkAddResponseDto.SucceededItemDto.builder()
                    .cartId(savedCartId)
                    .courseId(course.id())
                    .courseCode(course.subjectCode())
                    .courseName(course.subjectName())
                    .credits(course.credits())
                    .addedAt(now)
                    .build());
        }

        CartBulkAddResponseDto.SummaryDto summary = CartBulkAddResponseDto.SummaryDto.builder()
                .totalAttempted(courseIds.size())
                .successCount(succeededItems.size())
                .failedCount(0)
                .build();

        return CartBulkAddResponseDto.builder()
                .summary(summary)
                .succeeded(succeededItems)
                .build();
    }

    /**
     * 수강신청 기간 활성화 여부 확인
     */
    private boolean isEnrollmentPeriodActive() {
        return enrollmentPeriodPort.isEnrollmentPeriodActive();
    }

    /**
     * 시간표 충돌 확인
     */
    private boolean hasScheduleConflict(CoursePort.ScheduleInfo schedule1, CoursePort.ScheduleInfo schedule2) {
        // 같은 요일이 아니면 충돌 없음
        if (!schedule1.dayOfWeek().equals(schedule2.dayOfWeek())) {
            return false;
        }

        LocalTime start1 = schedule1.startTime();
        LocalTime end1 = schedule1.endTime();
        LocalTime start2 = schedule2.startTime();
        LocalTime end2 = schedule2.endTime();

        // 시간 겹침 확인
        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    /**
     * 스케줄로부터 해당하는 강의 찾기
     */
    private CoursePort.CourseInfo findCourseBySchedule(CoursePort.ScheduleInfo schedule, List<CoursePort.CourseInfo> courses) {
        return courses.stream()
                .filter(course -> course.schedules().contains(schedule))
                .findFirst()
                .orElse(null);
    }

    @Override
    public CartBulkDeleteResponseDto deleteFromCartBulk(CartBulkDeleteRequestDto request, String studentId) {
        Long studentIdLong = Long.parseLong(studentId);

        // cartIds 필수 체크
        if (request.getCartIds() == null || request.getCartIds().isEmpty()) {
            throw EnrollmentException.emptyCourseList();
        }

        // 장바구니 항목 조회 및 소유권 확인
        List<CartInfo> cartsToDelete = cartRepositoryPort.findAllById(request.getCartIds());

        if (cartsToDelete.size() != request.getCartIds().size()) {
            throw EnrollmentException.enrollmentNotFound(null);
        }

        // 삭제할 Course 정보들 조회
        List<Long> courseIds = cartsToDelete.stream()
                .map(CartInfo::courseId)
                .collect(Collectors.toList());
        Map<Long, CoursePort.CourseInfo> courseMap = coursePort.getCourses(courseIds).stream()
                .collect(Collectors.toMap(CoursePort.CourseInfo::id, course -> course));

        // 소유권 확인 및 삭제할 항목 수집
        List<CartBulkDeleteResponseDto.RemovedCourseDto> removedCourses = new ArrayList<>();
        int totalRemovedCredits = 0;

        for (CartInfo cart : cartsToDelete) {
            // 소유권 확인
            if (!cart.studentId().equals(studentIdLong)) {
                throw EnrollmentException.validationFailed(
                    String.format("장바구니 항목 %d에 대한 접근 권한이 없습니다.", cart.id()));
            }

            // 삭제할 항목 정보 수집
            CoursePort.CourseInfo course = courseMap.get(cart.courseId());
            removedCourses.add(CartBulkDeleteResponseDto.RemovedCourseDto.builder()
                    .cartId(cart.id())
                    .courseCode(course.subjectCode())
                    .courseName(course.subjectName())
                    .credits(course.credits())
                    .build());

            totalRemovedCredits += course.credits();
        }

        // 장바구니에서 삭제
        cartRepositoryPort.deleteAll(cartsToDelete);

        return CartBulkDeleteResponseDto.builder()
                .removedCount(removedCourses.size())
                .removedCredits(totalRemovedCredits)
                .removedCourses(removedCourses)
                .build();
    }

    @Override
    public CartBulkDeleteResponseDto deleteAllCart(String studentId) {
        Long studentIdLong = Long.parseLong(studentId);

        // 장바구니 항목 조회 및 소유권 확인
        List<CartInfo> cartsToDelete = cartRepositoryPort.findByStudentId(studentIdLong);

        // 삭제할 Course 정보들 조회
        List<Long> courseIds = cartsToDelete.stream()
                .map(CartInfo::courseId)
                .collect(Collectors.toList());
        Map<Long, CoursePort.CourseInfo> courseMap = courseIds.isEmpty()
                ? new HashMap<>()
                : coursePort.getCourses(courseIds).stream()
                        .collect(Collectors.toMap(CoursePort.CourseInfo::id, course -> course));

        // 장바구니에서 삭제
        cartRepositoryPort.deleteAllByStudentId(studentIdLong);

        return CartBulkDeleteResponseDto.builder()
                .removedCount(cartsToDelete.size())
                .removedCredits(cartsToDelete.stream()
                        .mapToInt(cart -> courseMap.get(cart.courseId()).credits())
                        .sum())
                .removedCourses(cartsToDelete.stream()
                        .map(cart -> {
                            CoursePort.CourseInfo course = courseMap.get(cart.courseId());
                            return CartBulkDeleteResponseDto.RemovedCourseDto.builder()
                                    .cartId(cart.id())
                                    .courseCode(course.subjectCode())
                                    .courseName(course.subjectName())
                                    .credits(course.credits())
                                    .build();
                        })
                        .collect(Collectors.toList()))
                .build();
    }
}
