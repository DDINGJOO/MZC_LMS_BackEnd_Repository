package com.mzc.backend.lms.domains.enrollment.application.service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.mzc.backend.lms.common.constants.CourseConstants;
import com.mzc.backend.lms.domains.enrollment.application.port.out.CoursePort;
import com.mzc.backend.lms.domains.enrollment.application.port.out.EnrollmentRepositoryPort;
import com.mzc.backend.lms.domains.enrollment.application.port.out.CartRepositoryPort;
import com.mzc.backend.lms.domains.enrollment.application.port.out.EnrollmentPeriodPort;
import com.mzc.backend.lms.domains.enrollment.adapter.in.web.dto.common.*;
import com.mzc.backend.lms.domains.enrollment.adapter.in.web.dto.request.*;
import com.mzc.backend.lms.domains.enrollment.adapter.in.web.dto.response.*;
import com.mzc.backend.lms.domains.enrollment.application.port.in.EnrollmentCourseUseCase;
import com.mzc.backend.lms.domains.enrollment.application.port.out.EnrollmentRepositoryPort.EnrollmentInfo;
import com.mzc.backend.lms.domains.enrollment.domain.exception.EnrollmentException;
import com.mzc.backend.lms.views.UserViewService;

/**
 * 수강신청 강의 조회 UseCase 구현체
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EnrollmentCourseUseCaseImpl implements EnrollmentCourseUseCase {

    private final CoursePort coursePort;
    private final EnrollmentRepositoryPort enrollmentRepositoryPort;
    private final CartRepositoryPort cartRepositoryPort;
    private final EnrollmentPeriodPort enrollmentPeriodPort;
    private final UserViewService userViewService;

    @Override
    public CourseListResponseDto searchCourses(CourseSearchRequestDto request, String studentId) {
        // 페이징 설정
        int page = request.getPage() != null ? request.getPage() : 0;
        int size = request.getSize() != null ? request.getSize() : 20;

        // 정렬 설정
        Sort sort = parseSort(request.getSort());

        // 필터링된 강의 조회
        List<CoursePort.CourseInfo> courses = filterCourses(request);

        // 정렬 적용
        courses = sortCourses(courses, sort);

        // 페이징 적용
        int start = page * size;
        int end = Math.min(start + size, courses.size());
        List<CoursePort.CourseInfo> pagedCourses = start < courses.size()
                ? courses.subList(start, end)
                : new ArrayList<>();

        // DTO 변환
        List<CourseItemDto> content = pagedCourses.stream()
                .map(course -> convertToCourseItemDto(course, studentId))
                .collect(Collectors.toList());

        return CourseListResponseDto.builder()
                .content(content)
                .totalElements(courses.size())
                .totalPages((int) Math.ceil((double) courses.size() / size))
                .currentPage(page)
                .size(size)
                .build();
    }

    private List<CoursePort.CourseInfo> filterCourses(CourseSearchRequestDto request) {
        // enrollmentPeriodId 필수 체크
        if (request.getEnrollmentPeriodId() == null) {
            throw EnrollmentException.periodNotFound(null);
        }

        // EnrollmentPeriod 조회
        EnrollmentPeriodPort.PeriodInfo enrollmentPeriod = enrollmentPeriodPort.getPeriod(request.getEnrollmentPeriodId());

        // EnrollmentPeriod의 AcademicTerm으로 강의 조회
        Long academicTermId = enrollmentPeriod.academicTermId();
        List<CoursePort.CourseInfo> courses = coursePort.getCoursesByAcademicTermId(academicTermId);

        // 디버깅: 초기 강의 수 확인
        log.debug("enrollmentPeriodId={}, academicTermId={}로 조회된 강의 수: {}",
                request.getEnrollmentPeriodId(), academicTermId, courses.size());

        // 학과 필터
        if (request.getDepartmentId() != null) {
            int beforeSize = courses.size();
            courses = courses.stream()
                    .filter(c -> c.departmentId().equals(request.getDepartmentId()))
                    .collect(Collectors.toList());
            log.debug("departmentId={} 필터 후: {} -> {}", request.getDepartmentId(), beforeSize, courses.size());
        }

        // 이수구분 필터
        if (request.getCourseType() != null) {
            int beforeSize = courses.size();
            int typeCode = request.getCourseType();
            courses = courses.stream()
                    .filter(c -> c.courseTypeIntCode() == typeCode)
                    .collect(Collectors.toList());
            log.debug("courseType={} 필터 후: {} -> {}", typeCode, beforeSize, courses.size());
        }

        // 학점 필터
        if (request.getCredits() != null) {
            int beforeSize = courses.size();
            courses = courses.stream()
                    .filter(c -> c.credits() == request.getCredits())
                    .collect(Collectors.toList());
            log.debug("credits={} 필터 후: {} -> {}", request.getCredits(), beforeSize, courses.size());
        }

        // 키워드 검색 (과목명, 과목코드, 교수명)
        if (request.getKeyword() != null && !request.getKeyword().trim().isEmpty()) {
            int beforeSize = courses.size();
            String keyword = request.getKeyword().trim();
            log.debug("키워드 검색 시작: keyword='{}', 필터 전 강의 수: {}", keyword, beforeSize);

            courses = courses.stream()
                    .filter(c -> {
                        // 과목명, 과목코드 매칭
                        String subjectName = c.subjectName();
                        String subjectCode = c.subjectCode();

                        boolean matchesSubjectName = subjectName != null &&
                                subjectName.toLowerCase().contains(keyword.toLowerCase());
                        boolean matchesSubjectCode = subjectCode != null &&
                                subjectCode.toLowerCase().contains(keyword.toLowerCase());

                        // 교수명 매칭
                        String professorName = userViewService.getUserName(
                                c.professorId().toString()
                        );
                        boolean matchesProfessor = professorName != null &&
                                professorName.toLowerCase().contains(keyword.toLowerCase());

                        boolean matches = matchesSubjectName || matchesSubjectCode || matchesProfessor;
                        if (matches) {
                            log.debug("매칭된 강의: subjectName={}, subjectCode={}, professorName={}",
                                    subjectName, subjectCode, professorName);
                        }
                        return matches;
                    })
                    .collect(Collectors.toList());

            log.debug("키워드 검색 후: {} -> {}", beforeSize, courses.size());
        }

        log.debug("최종 필터링된 강의 수: {}", courses.size());
        return courses;
    }

    private CourseItemDto convertToCourseItemDto(CoursePort.CourseInfo courseInfo, String studentId) {
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

        // 스케줄 텍스트 생성
        String scheduleText = generateScheduleText(schedules);

        // CourseType 변환
        CourseTypeDto courseTypeDto = convertToCourseTypeDto(courseInfo.courseTypeIntCode());

        // 수강신청 정보
        EnrollmentDto enrollmentDto = EnrollmentDto.builder()
                .current(courseInfo.currentStudents())
                .max(courseInfo.maxStudents())
                .isFull(courseInfo.isFull())
                .build();

        // 장바구니/수강신청 여부 확인
        boolean isInCart = false;
        boolean isEnrolled = false;
        boolean hasPrerequisites = true; // 선수과목 이수 여부

        if (studentId != null) {
            Long studentIdLong = Long.parseLong(studentId);
            isInCart = cartRepositoryPort.existsByStudentIdAndCourseId(
                    studentIdLong, courseInfo.id());
            isEnrolled = enrollmentRepositoryPort.existsByStudentIdAndCourseId(
                    studentIdLong, courseInfo.id());

            // 선수과목 이수 여부 확인
            hasPrerequisites = checkPrerequisites(courseInfo, studentIdLong);
        }

        return CourseItemDto.builder()
                .id(courseInfo.id())
                .courseCode(courseInfo.subjectCode())
                .courseName(courseInfo.subjectName())
                .section(courseInfo.sectionNumber())
                .professor(ProfessorDto.builder()
                        .id(courseInfo.professorId())
                        .name(professorName != null ? professorName : "교수")
                        .build())
                .department(DepartmentDto.builder()
                        .id(courseInfo.departmentId())
                        .name(courseInfo.departmentName())
                        .build())
                .credits(courseInfo.credits())
                .courseType(courseTypeDto)
                .schedule(schedules)
                .scheduleText(scheduleText)
                .enrollment(enrollmentDto)
                .isInCart(isInCart)
                .isEnrolled(isEnrolled)
                .canEnroll(!isEnrolled && !enrollmentDto.getIsFull() && hasPrerequisites)
                .warnings(new ArrayList<>())
                .build();
    }

    private ScheduleDto convertToScheduleDto(CoursePort.ScheduleInfo scheduleInfo) {
        DayOfWeek dayOfWeek = scheduleInfo.dayOfWeek();
        LocalTime startTime = scheduleInfo.startTime();
        LocalTime endTime = scheduleInfo.endTime();
        return ScheduleDto.builder()
                .dayOfWeek(dayOfWeek.getValue()) // DayOfWeek를 int로 변환
                .dayName(CourseConstants.DAY_NAME_MAP.get(dayOfWeek))
                .startTime(startTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")))
                .endTime(endTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")))
                .classroom(scheduleInfo.classroom())
                .build();
    }

    private String generateScheduleText(List<ScheduleDto> schedules) {
        if (schedules.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        Map<String, List<ScheduleDto>> byRoom = schedules.stream()
                .collect(Collectors.groupingBy(ScheduleDto::getClassroom));

        List<String> parts = new ArrayList<>();
        for (Map.Entry<String, List<ScheduleDto>> entry : byRoom.entrySet()) {
            String room = entry.getKey();
            List<String> timeParts = entry.getValue().stream()
                    .map(s -> String.format("%s %s-%s", s.getDayName(), 
                            s.getStartTime(), s.getEndTime()))
                    .collect(Collectors.toList());
            parts.add(String.join(", ", timeParts) + "\n" + room);
        }

        return String.join("\n", parts);
    }

    private CourseTypeDto convertToCourseTypeDto(int courseTypeIntCode) {
        String code = CourseConstants.COURSE_TYPE_CODE_MAP.get(courseTypeIntCode);
        String name = CourseConstants.COURSE_TYPE_NAME_MAP.get(courseTypeIntCode);
        String color = CourseConstants.getCourseTypeColor(code);

        return CourseTypeDto.builder()
                .code(code)
                .name(name)
                .color(color)
                .build();
    }

    private Sort parseSort(String sort) {
        if (sort == null || sort.isEmpty()) {
            return Sort.by(Sort.Direction.ASC, "subject.subjectCode");
        }

        String[] parts = sort.split(",");
        String property = parts[0].trim();
        Sort.Direction direction = parts.length > 1 && 
                parts[1].trim().equalsIgnoreCase("desc") 
                ? Sort.Direction.DESC 
                : Sort.Direction.ASC;

        // property 매핑
        String mappedProperty = switch (property) {
            case "courseCode" -> "subject.subjectCode";
            case "courseName" -> "subject.subjectName";
            case "credits" -> "subject.credits";
            default -> "subject.subjectCode";
        };

        return Sort.by(direction, mappedProperty);
    }

    /**
     * 강의 리스트 정렬
     */
    private List<CoursePort.CourseInfo> sortCourses(List<CoursePort.CourseInfo> courses, Sort sort) {
        if (sort == null || sort.isEmpty()) {
            return courses;
        }

        Comparator<CoursePort.CourseInfo> comparator = null;

        for (Sort.Order order : sort) {
            Comparator<CoursePort.CourseInfo> orderComparator = getComparator(order);
            if (comparator == null) {
                comparator = orderComparator;
            } else {
                comparator = comparator.thenComparing(orderComparator);
            }
        }

        if (comparator != null) {
            return courses.stream()
                    .sorted(comparator)
                    .collect(Collectors.toList());
        }

        return courses;
    }

    /**
     * Sort.Order에 따른 Comparator 생성
     */
    private Comparator<CoursePort.CourseInfo> getComparator(Sort.Order order) {
        Comparator<CoursePort.CourseInfo> comparator = switch (order.getProperty()) {
            case "subject.subjectCode" -> Comparator.comparing(
                    CoursePort.CourseInfo::subjectCode);
            case "subject.subjectName" -> Comparator.comparing(
                    CoursePort.CourseInfo::subjectName);
            case "subject.credits" -> Comparator.comparing(
                    CoursePort.CourseInfo::credits);
            default -> Comparator.comparing(
                    CoursePort.CourseInfo::subjectCode);
        };

        return order.getDirection() == Sort.Direction.DESC
                ? comparator.reversed()
                : comparator;
    }

    /**
     * 선수과목 이수 여부 확인
     */
    private boolean checkPrerequisites(CoursePort.CourseInfo courseInfo, Long studentId) {
        Long subjectId = courseInfo.subjectId();
        List<Long> prerequisiteSubjectIds = coursePort.getMandatoryPrerequisiteSubjectIds(subjectId);

        log.debug("과목 ID: {}, 필수 선수과목 개수: {}", subjectId, prerequisiteSubjectIds.size());

        if (prerequisiteSubjectIds.isEmpty()) {
            log.debug("선수과목이 없으므로 true 반환");
            return true;
        }

        // 학생이 수강신청한 강의 목록 조회
        List<EnrollmentInfo> studentEnrollments = enrollmentRepositoryPort.findByStudentId(studentId);
        Set<Long> enrolledSubjectIds = studentEnrollments.stream()
                .map(enrollment -> coursePort.getCourse(enrollment.courseId()).subjectId())
                .collect(Collectors.toSet());

        log.debug("학생 ID: {}, 수강신청한 과목 수: {}, 과목 IDs: {}", studentId, enrolledSubjectIds.size(), enrolledSubjectIds);

        // 필수 선수과목이 모두 이수되었는지 확인
        boolean allPrerequisitesMet = enrolledSubjectIds.containsAll(prerequisiteSubjectIds);
        log.debug("모든 필수 선수과목 이수 여부: {}", allPrerequisitesMet);

        return allPrerequisitesMet;
    }
}
