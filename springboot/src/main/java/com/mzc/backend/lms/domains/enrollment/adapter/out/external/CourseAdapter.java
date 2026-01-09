package com.mzc.backend.lms.domains.enrollment.adapter.out.external;

import com.mzc.backend.lms.domains.course.constants.CourseConstants;
import com.mzc.backend.lms.domains.course.course.entity.Course;
import com.mzc.backend.lms.domains.course.course.entity.CourseSchedule;
import com.mzc.backend.lms.domains.course.course.repository.CourseRepository;
import com.mzc.backend.lms.domains.course.subject.entity.SubjectPrerequisites;
import com.mzc.backend.lms.domains.course.subject.repository.SubjectPrerequisitesRepository;
import com.mzc.backend.lms.domains.enrollment.application.port.out.CoursePort;
import com.mzc.backend.lms.domains.enrollment.adapter.out.persistence.repository.EnrollmentRepository;
import com.mzc.backend.lms.domains.enrollment.domain.exception.EnrollmentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Course 도메인 Adapter
 * 현재: Repository 직접 호출
 * MSA 전환 시: HTTP Client로 교체
 */
@Component
@RequiredArgsConstructor
public class CourseAdapter implements CoursePort {

    private final CourseRepository courseRepository;
    private final SubjectPrerequisitesRepository prerequisitesRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Override
    public CourseInfo getCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> EnrollmentException.courseNotExists(courseId));
        return toCourseInfo(course);
    }

    @Override
    public CourseInfo getCourseWithLock(Long courseId) {
        Course course = courseRepository.findByIdWithLock(courseId)
                .orElseThrow(() -> EnrollmentException.courseNotExists(courseId));
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
                .orElseThrow(() -> EnrollmentException.courseNotExists(courseId));
        course.setCurrentStudents(course.getCurrentStudents() + 1);
        courseRepository.save(course);
    }

    @Override
    public void decreaseCurrentStudents(Long courseId) {
        Course course = courseRepository.findByIdWithLock(courseId)
                .orElseThrow(() -> EnrollmentException.courseNotExists(courseId));
        if (course.getCurrentStudents() > 0) {
            course.setCurrentStudents(course.getCurrentStudents() - 1);
            courseRepository.save(course);
        }
    }

    @Override
    public boolean checkPrerequisites(Long studentId, Long subjectId) {
        List<SubjectPrerequisites> prerequisites = prerequisitesRepository.findBySubjectId(subjectId);

        for (SubjectPrerequisites prerequisite : prerequisites) {
            if (prerequisite.getIsMandatory()) {
                Long prerequisiteSubjectId = prerequisite.getPrerequisite().getId();
                // 해당 선수과목을 수강했는지 확인
                boolean hasCompleted = enrollmentRepository.findByStudentId(studentId).stream()
                        .anyMatch(e -> e.getCourse().getSubject().getId().equals(prerequisiteSubjectId));
                if (!hasCompleted) {
                    return false;
                }
            }
        }
        return true;
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
