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

import com.mzc.backend.lms.domains.enrollment.application.port.out.EnrollmentPeriodPort;
import com.mzc.backend.lms.domains.enrollment.application.port.out.CoursePort;
import com.mzc.backend.lms.domains.enrollment.application.port.out.CoursePort.CourseInfo;
import com.mzc.backend.lms.domains.enrollment.application.port.out.CoursePort.ScheduleInfo;
import com.mzc.backend.lms.domains.enrollment.application.port.out.StudentPort;
import com.mzc.backend.lms.domains.course.constants.CourseConstants;
import com.mzc.backend.lms.domains.enrollment.adapter.in.web.dto.common.*;
import com.mzc.backend.lms.domains.enrollment.adapter.in.web.dto.request.*;
import com.mzc.backend.lms.domains.enrollment.adapter.in.web.dto.response.*;
import com.mzc.backend.lms.domains.enrollment.adapter.out.persistence.entity.CourseCart;
import com.mzc.backend.lms.domains.enrollment.adapter.out.persistence.entity.Enrollment;
import com.mzc.backend.lms.domains.enrollment.adapter.out.persistence.repository.CourseCartRepository;
import com.mzc.backend.lms.domains.enrollment.adapter.out.persistence.repository.EnrollmentRepository;
import com.mzc.backend.lms.domains.enrollment.application.port.in.CartUseCase;
import com.mzc.backend.lms.domains.enrollment.domain.exception.EnrollmentErrorCode;
import com.mzc.backend.lms.domains.enrollment.domain.exception.EnrollmentException;
import com.mzc.backend.lms.views.UserViewService;

/**
 * 장바구니 UseCase 구현체
 *
 * Course, User 도메인과의 통신은 Port를 통해 수행 (도메인 간 순환 의존성 방지)
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CartUseCaseImpl implements CartUseCase {

    private final CourseCartRepository courseCartRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final EnrollmentPeriodPort enrollmentPeriodPort;
    private final CoursePort coursePort;
    private final StudentPort studentPort;
    private final UserViewService userViewService;

    private static final int MAX_CREDITS_PER_TERM = 21; // 학기당 최대 학점

    @Override
    public CartResponseDto getCart(String studentId) {
        Long studentIdLong = Long.parseLong(studentId);

        // 학생의 장바구니 목록 조회
        List<CourseCart> cartItems = courseCartRepository.findByStudentId(studentIdLong);

        // 장바구니에 있는 과목들의 ID 수집 및 조회 (CoursePort 사용)
        List<Long> courseIds = cartItems.stream()
                .map(CourseCart::getCourseId)
                .collect(Collectors.toList());

        Map<Long, CourseInfo> courseMap = coursePort.getCourses(courseIds).stream()
                .collect(Collectors.toMap(CourseInfo::id, course -> course));

        // DTO 변환
        List<CartItemDto> cartItemDtos = cartItems.stream()
                .map(cart -> convertToCartItemDto(cart, courseMap.get(cart.getCourseId())))
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

    private CartItemDto convertToCartItemDto(CourseCart cart, CourseInfo course) {
        // 교수 이름 조회
        String professorName = userViewService.getUserName(course.professorId().toString());

        // 스케줄 변환
        List<ScheduleDto> schedules = course.schedules().stream()
                .map(this::convertToScheduleDto)
                .sorted(Comparator.comparing(ScheduleDto::getDayOfWeek)
                        .thenComparing(ScheduleDto::getStartTime))
                .collect(Collectors.toList());

        // 강의 정보 DTO
        CartItemDto.CourseInfoDto courseInfo = CartItemDto.CourseInfoDto.builder()
                .id(course.id())
                .code(course.subjectCode())
                .name(course.subjectName())
                .section(course.sectionNumber())
                .credits(course.credits())
                .courseType(course.courseTypeName())
                .currentStudents(course.currentStudents())
                .maxStudents(course.maxStudents())
                .build();

        // 교수 정보 DTO
        ProfessorDto professorDto = ProfessorDto.builder()
                .id(course.professorId())
                .name(professorName != null ? professorName : "교수")
                .build();

        // 수강신청 정보 DTO
        EnrollmentDto enrollmentDto = EnrollmentDto.builder()
                .current(course.currentStudents())
                .max(course.maxStudents())
                .isFull(course.isFull())
                .build();

        return CartItemDto.builder()
                .cartId(cart.getId())
                .course(courseInfo)
                .professor(professorDto)
                .schedule(schedules)
                .enrollment(enrollmentDto)
                .addedAt(cart.getAddedAt())
                .build();
    }

    private ScheduleDto convertToScheduleDto(ScheduleInfo schedule) {
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

        // 2. 학생 존재 여부 확인 (StudentPort 사용)
        if (!studentPort.existsById(studentIdLong)) {
            throw EnrollmentException.studentNotFound(studentIdLong);
        }

        // 3. 과목 존재 여부 체크 및 조회 (CoursePort 사용)
        List<Long> courseIds = request.getCourseIds();
        if (courseIds == null || courseIds.isEmpty()) {
            throw EnrollmentException.emptyCourseList();
        }

        List<CourseInfo> courses = coursePort.getCourses(courseIds);
        if (courses.size() != courseIds.size()) {
            throw EnrollmentException.courseNotExists(null);
        }

        // 4. 기존 장바구니 및 수강신청 정보 조회
        List<CourseCart> existingCarts = courseCartRepository.findByStudentId(studentIdLong);
        List<Enrollment> existingEnrollments = enrollmentRepository.findByStudentId(studentIdLong);

        Set<Long> cartCourseIds = existingCarts.stream()
                .map(CourseCart::getCourseId)
                .collect(Collectors.toSet());
        Set<Long> enrolledCourseIds = existingEnrollments.stream()
                .map(Enrollment::getCourseId)
                .collect(Collectors.toSet());

        // 기존 장바구니 및 수강신청의 Course 정보 조회 (CoursePort 사용)
        List<Long> existingCartCourseIds = existingCarts.stream()
                .map(CourseCart::getCourseId)
                .collect(Collectors.toList());
        List<Long> existingEnrollmentCourseIds = existingEnrollments.stream()
                .map(Enrollment::getCourseId)
                .collect(Collectors.toList());

        List<CourseInfo> existingCartCourses = coursePort.getCourses(existingCartCourseIds);
        List<CourseInfo> existingEnrollmentCourses = coursePort.getCourses(existingEnrollmentCourseIds);

        Set<Long> enrolledSubjectIds = existingEnrollmentCourses.stream()
                .map(CourseInfo::subjectId)
                .collect(Collectors.toSet());

        // 장바구니에 있는 과목의 subject_id도 체크용으로 수집
        Set<Long> cartSubjectIds = existingCartCourses.stream()
                .map(CourseInfo::subjectId)
                .collect(Collectors.toSet());

        // 5. 각 강의에 대한 검증
        Set<Long> newSubjectIds = new HashSet<>(); // 새로 추가하려는 강의들의 subject_id
        List<String> validationErrors = new ArrayList<>();

        for (CourseInfo course : courses) {
            Long subjectId = course.subjectId();

            // 5-1. 이미 장바구니에 있는지 체크
            if (cartCourseIds.contains(course.id())) {
                validationErrors.add(String.format("강의 %s(%s)는 이미 장바구니에 있습니다.",
                        course.subjectName(), course.subjectCode()));
                continue;
            }

            // 5-2. 이미 수강신청했는지 체크
            if (enrolledCourseIds.contains(course.id())) {
                validationErrors.add(String.format("강의 %s(%s)는 이미 수강신청했습니다.",
                        course.subjectName(), course.subjectCode()));
                continue;
            }

            // 5-3. 동일 과목 다른 분반 체크 (장바구니 + 수강신청 + 새로 추가하는 강의들 간)
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

            // 5-4. 선수과목 이수 여부 체크 (CoursePort 사용)
            List<Long> prerequisiteSubjectIds = coursePort.getMandatoryPrerequisiteSubjectIds(subjectId);
            for (Long prerequisiteSubjectId : prerequisiteSubjectIds) {
                // 선수과목을 이수했는지 확인 (수강신청한 강의 중에서)
                boolean hasPrerequisite = existingEnrollmentCourses.stream()
                        .anyMatch(enrolledCourse -> enrolledCourse.subjectId().equals(prerequisiteSubjectId));

                if (!hasPrerequisite) {
                    // 선수과목 정보 조회 (에러 메시지를 위해)
                    validationErrors.add(String.format("강의 %s(%s)의 필수 선수과목을 이수하지 않았습니다.",
                            course.subjectName(), course.subjectCode()));
                    break;
                }
            }
        }

        // 하나라도 검증 실패하면 전체 실패
        if (!validationErrors.isEmpty()) {
            throw EnrollmentException.validationFailed(String.join("; ", validationErrors));
        }

        // 6. 학점 제한 체크
        int currentCredits = existingCartCourses.stream()
                .mapToInt(CourseInfo::credits)
                .sum();
        int newCredits = courses.stream()
                .mapToInt(CourseInfo::credits)
                .sum();

        if (currentCredits + newCredits > MAX_CREDITS_PER_TERM) {
            throw EnrollmentException.maxCreditsExceeded(currentCredits, MAX_CREDITS_PER_TERM);
        }

        // 7. 시간표 충돌 체크
        List<ScheduleInfo> existingSchedules = new ArrayList<>();
        for (CourseInfo course : existingCartCourses) {
            existingSchedules.addAll(course.schedules());
        }
        for (CourseInfo course : existingEnrollmentCourses) {
            existingSchedules.addAll(course.schedules());
        }

        // 모든 새 강의의 스케줄 수집
        Map<ScheduleInfo, CourseInfo> newScheduleMap = new HashMap<>();
        for (CourseInfo course : courses) {
            for (ScheduleInfo schedule : course.schedules()) {
                newScheduleMap.put(schedule, course);
            }
        }

        // 기존 강의와의 충돌 체크
        for (Map.Entry<ScheduleInfo, CourseInfo> entry : newScheduleMap.entrySet()) {
            ScheduleInfo newSchedule = entry.getKey();
            CourseInfo newCourse = entry.getValue();
            for (ScheduleInfo existingSchedule : existingSchedules) {
                if (hasScheduleConflict(newSchedule, existingSchedule)) {
                    String conflictInfo = String.format("강의 %s(%s)의 시간표가 기존 강의와 충돌합니다.",
                            newCourse.subjectName(), newCourse.subjectCode());
                    throw EnrollmentException.scheduleConflict(conflictInfo);
                }
            }
        }

        // 새로 추가하는 강의들 간의 충돌 체크
        List<Map.Entry<ScheduleInfo, CourseInfo>> newScheduleList = new ArrayList<>(newScheduleMap.entrySet());
        for (int i = 0; i < newScheduleList.size(); i++) {
            ScheduleInfo schedule1 = newScheduleList.get(i).getKey();
            CourseInfo course1 = newScheduleList.get(i).getValue();

            for (int j = i + 1; j < newScheduleList.size(); j++) {
                ScheduleInfo schedule2 = newScheduleList.get(j).getKey();
                CourseInfo course2 = newScheduleList.get(j).getValue();

                if (hasScheduleConflict(schedule1, schedule2) &&
                        !course1.id().equals(course2.id())) {
                    String conflictInfo = String.format("강의 %s(%s)와 %s(%s)의 시간표가 충돌합니다.",
                            course1.subjectName(), course1.subjectCode(),
                            course2.subjectName(), course2.subjectCode());
                    throw EnrollmentException.scheduleConflict(conflictInfo);
                }
            }
        }

        // 8. 모든 검증 통과 - 장바구니에 추가
        LocalDateTime now = LocalDateTime.now();
        List<CartBulkAddResponseDto.SucceededItemDto> succeededItems = new ArrayList<>();

        for (CourseInfo course : courses) {
            CourseCart cart = CourseCart.builder()
                    .studentId(studentIdLong)
                    .courseId(course.id())
                    .addedAt(now)
                    .build();

            CourseCart savedCart = courseCartRepository.save(cart);
            if (savedCart == null || savedCart.getId() == null) {
                throw new EnrollmentException(EnrollmentErrorCode.ENROLLMENT_NOT_FOUND);
            }

            succeededItems.add(CartBulkAddResponseDto.SucceededItemDto.builder()
                    .cartId(savedCart.getId())
                    .courseId(course.id())
                    .courseCode(course.subjectCode())
                    .courseName(course.subjectName())
                    .credits(course.credits())
                    .addedAt(savedCart.getAddedAt())
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
     * 시간표 충돌 확인 (ScheduleInfo 기반)
     */
    private boolean hasScheduleConflict(ScheduleInfo schedule1, ScheduleInfo schedule2) {
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

    @Override
    public CartBulkDeleteResponseDto deleteFromCartBulk(CartBulkDeleteRequestDto request, String studentId) {
        Long studentIdLong = Long.parseLong(studentId);

        // cartIds 필수 체크
        if (request.getCartIds() == null || request.getCartIds().isEmpty()) {
            throw EnrollmentException.emptyCourseList();
        }

        // 장바구니 항목 조회 및 소유권 확인
        List<CourseCart> cartsToDelete = courseCartRepository.findAllById(request.getCartIds());

        if (cartsToDelete.size() != request.getCartIds().size()) {
            throw EnrollmentException.enrollmentNotFound(null);
        }

        // 삭제할 Course 정보 조회 (CoursePort 사용)
        List<Long> courseIds = cartsToDelete.stream()
                .map(CourseCart::getCourseId)
                .collect(Collectors.toList());
        Map<Long, CourseInfo> courseMap = coursePort.getCourses(courseIds).stream()
                .collect(Collectors.toMap(CourseInfo::id, course -> course));

        // 소유권 확인 및 삭제할 항목 수집
        List<CartBulkDeleteResponseDto.RemovedCourseDto> removedCourses = new ArrayList<>();
        int totalRemovedCredits = 0;

        for (CourseCart cart : cartsToDelete) {
            // 소유권 확인
            if (!cart.getStudentId().equals(studentIdLong)) {
                throw EnrollmentException.validationFailed(
                        String.format("장바구니 항목 %d에 대한 접근 권한이 없습니다.", cart.getId()));
            }

            // 삭제할 항목 정보 수집
            CourseInfo course = courseMap.get(cart.getCourseId());
            removedCourses.add(CartBulkDeleteResponseDto.RemovedCourseDto.builder()
                    .cartId(cart.getId())
                    .courseCode(course.subjectCode())
                    .courseName(course.subjectName())
                    .credits(course.credits())
                    .build());

            totalRemovedCredits += course.credits();
        }

        // 장바구니에서 삭제
        courseCartRepository.deleteAll(cartsToDelete);

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
        List<CourseCart> cartsToDelete = courseCartRepository.findByStudentId(studentIdLong);

        // 삭제할 Course 정보 조회 (CoursePort 사용)
        List<Long> courseIds = cartsToDelete.stream()
                .map(CourseCart::getCourseId)
                .collect(Collectors.toList());
        Map<Long, CourseInfo> courseMap = coursePort.getCourses(courseIds).stream()
                .collect(Collectors.toMap(CourseInfo::id, course -> course));

        // 장바구니에서 삭제
        courseCartRepository.deleteByStudentId(studentIdLong);

        return CartBulkDeleteResponseDto.builder()
                .removedCount(cartsToDelete.size())
                .removedCredits(cartsToDelete.stream()
                        .mapToInt(cart -> courseMap.get(cart.getCourseId()).credits())
                        .sum())
                .removedCourses(cartsToDelete.stream()
                        .map(cart -> {
                            CourseInfo course = courseMap.get(cart.getCourseId());
                            return CartBulkDeleteResponseDto.RemovedCourseDto.builder()
                                    .cartId(cart.getId())
                                    .courseCode(course.subjectCode())
                                    .courseName(course.subjectName())
                                    .credits(course.credits())
                                    .build();
                        })
                        .collect(Collectors.toList()))
                .build();
    }
}
