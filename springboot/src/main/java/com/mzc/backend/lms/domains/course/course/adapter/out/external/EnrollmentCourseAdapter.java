package com.mzc.backend.lms.domains.course.course.adapter.out.external;

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
 * Course 도메인 Adapter (enrollment 도메인용)
 * enrollment 도메인의 CoursePort를 구현하여 강의 데이터 제공
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

        String courseTypeCode = CourseConstants.COURSE_TYPE_CODE_MAP
                .getOrDefault(course.getSubject().getCourseType().getTypeCode(), "기타");
        String courseTypeName = CourseConstants.COURSE_TYPE_NAME_MAP
                .getOrDefault(course.getSubject().getCourseType().getTypeCode(), "기타");

        return new CourseInfo(
                course.getId(),
                course.getSubject().getId(),
                course.getSubject().getSubjectCode(),
                course.getSubject().getSubjectName(),
                course.getSectionNumber(),
                course.getSubject().getCredits(),
                course.getMaxStudents(),
                course.getCurrentStudents(),
                course.getProfessor().getProfessorId(),
                course.getAcademicTerm().getId(),
                courseTypeCode,
                courseTypeName,
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
