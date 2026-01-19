package com.mzc.backend.lms.integration.course;

import com.mzc.backend.lms.domains.course.constants.CourseConstants;
import com.mzc.backend.lms.domains.course.course.adapter.out.persistence.entity.Course;
import com.mzc.backend.lms.domains.course.course.adapter.out.persistence.entity.CourseSchedule;
import com.mzc.backend.lms.domains.course.course.adapter.out.persistence.repository.CourseRepository;
import com.mzc.backend.lms.domains.course.subject.adapter.out.persistence.entity.SubjectPrerequisites;
import com.mzc.backend.lms.domains.course.subject.adapter.out.persistence.repository.SubjectPrerequisitesRepository;
import com.mzc.backend.lms.domains.course.exception.CourseException;
import com.mzc.backend.lms.domains.enrollment.application.port.out.CoursePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Course → Enrollment 통합 Adapter
 *
 * Enrollment 도메인이 Course 도메인의 데이터에 접근할 때 사용
 * integration 패키지에 위치하여 도메인 간 순환 의존성 방지
 *
 * MSA 전환 시: HTTP Client로 교체
 */
@Component("enrollmentCourseAdapter")
@RequiredArgsConstructor
public class EnrollmentCourseAdapter implements CoursePort {

    private final CourseRepository courseRepository;
    private final SubjectPrerequisitesRepository prerequisitesRepository;

    @Override
    public CourseInfo getCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> CourseException.courseNotFound(courseId));
        return toCourseInfo(course);
    }

    @Override
    public CourseInfo getCourseWithLock(Long courseId) {
        Course course = courseRepository.findByIdWithLock(courseId)
                .orElseThrow(() -> CourseException.courseNotFound(courseId));
        return toCourseInfo(course);
    }

    @Override
    public List<CourseInfo> getCourses(List<Long> courseIds) {
        return courseRepository.findAllById(courseIds).stream()
                .map(this::toCourseInfo)
                .toList();
    }

    @Override
    public List<CourseInfo> findByAcademicTermId(Long academicTermId) {
        return courseRepository.findByAcademicTermId(academicTermId).stream()
                .map(this::toCourseInfo)
                .toList();
    }

    @Override
    public void increaseCurrentStudents(Long courseId) {
        Course course = courseRepository.findByIdWithLock(courseId)
                .orElseThrow(() -> CourseException.courseNotFound(courseId));
        course.setCurrentStudents(course.getCurrentStudents() + 1);
        courseRepository.save(course);
    }

    @Override
    public void decreaseCurrentStudents(Long courseId) {
        Course course = courseRepository.findByIdWithLock(courseId)
                .orElseThrow(() -> CourseException.courseNotFound(courseId));
        if (course.getCurrentStudents() > 0) {
            course.setCurrentStudents(course.getCurrentStudents() - 1);
            courseRepository.save(course);
        }
    }

    @Override
    public List<Long> getMandatoryPrerequisiteSubjectIds(Long subjectId) {
        return prerequisitesRepository.findBySubjectId(subjectId).stream()
                .filter(SubjectPrerequisites::getIsMandatory)
                .map(p -> p.getPrerequisite().getId())
                .toList();
    }

    private CourseInfo toCourseInfo(Course course) {
        List<ScheduleInfo> schedules = course.getSchedules().stream()
                .map(this::toScheduleInfo)
                .toList();

        var subject = course.getSubject();
        var courseType = subject.getCourseType();
        var department = subject.getDepartment();

        String courseTypeCode = CourseConstants.COURSE_TYPE_CODE_MAP
                .getOrDefault(courseType.getTypeCode(), "기타");
        String courseTypeName = CourseConstants.COURSE_TYPE_NAME_MAP
                .getOrDefault(courseType.getTypeCode(), "기타");

        return new CourseInfo(
                course.getId(),
                subject.getId(),
                subject.getSubjectCode(),
                subject.getSubjectName(),
                course.getSectionNumber(),
                subject.getCredits(),
                course.getMaxStudents(),
                course.getCurrentStudents(),
                course.getProfessor().getProfessorId(),
                course.getAcademicTerm().getId(),
                courseTypeCode,
                courseTypeName,
                courseType.getTypeCode(),
                department.getId(),
                department.getDepartmentName(),
                schedules
        );
    }

    private ScheduleInfo toScheduleInfo(CourseSchedule schedule) {
        return new ScheduleInfo(
                schedule.getDayOfWeek(),
                schedule.getStartTime(),
                schedule.getEndTime(),
                schedule.getScheduleRoom()
        );
    }
}
