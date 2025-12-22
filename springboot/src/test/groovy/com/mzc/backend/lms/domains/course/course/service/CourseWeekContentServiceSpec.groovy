package com.mzc.backend.lms.domains.course.course.service

import com.mzc.backend.lms.domains.course.course.dto.*
import com.mzc.backend.lms.domains.course.course.entity.Course
import com.mzc.backend.lms.domains.course.course.entity.CourseWeek
import com.mzc.backend.lms.domains.course.course.entity.WeekContent
import com.mzc.backend.lms.domains.course.course.repository.CourseRepository
import com.mzc.backend.lms.domains.course.course.repository.CourseWeekRepository
import com.mzc.backend.lms.domains.course.course.repository.WeekContentRepository
import com.mzc.backend.lms.domains.enrollment.repository.EnrollmentRepository
import com.mzc.backend.lms.domains.user.professor.entity.Professor
import spock.lang.Specification
import spock.lang.Subject

/**
 * CourseWeekContentService Spock 테스트
 */
class CourseWeekContentServiceSpec extends Specification {

    def courseRepository = Mock(CourseRepository)
    def courseWeekRepository = Mock(CourseWeekRepository)
    def weekContentRepository = Mock(WeekContentRepository)
    def enrollmentRepository = Mock(EnrollmentRepository)

    @Subject
    CourseWeekContentService courseWeekContentService = new CourseWeekContentService(
            courseRepository,
            courseWeekRepository,
            weekContentRepository,
            enrollmentRepository
    )

    def professor
    def course
    def courseWeek
    def weekContent

    def setup() {
        professor = Mock(Professor) {
            getProfessorId() >> 1L
        }

        course = Mock(Course) {
            getId() >> 1L
            getProfessor() >> professor
        }

        courseWeek = Mock(CourseWeek) {
            getId() >> 1L
            getCourse() >> course
            getWeekNumber() >> 1
            getWeekTitle() >> "1주차 - 오리엔테이션"
            getCreatedAt() >> null
        }

        weekContent = Mock(WeekContent) {
            getId() >> 1L
            getWeek() >> courseWeek
            getContentType() >> "VIDEO"
            getTitle() >> "1강 - 개요"
            getContentUrl() >> "https://example.com/video1.mp4"
            getDuration() >> "45:30"
            getDisplayOrder() >> 1
            getCreatedAt() >> null
        }
    }

    // createWeek 테스트

    def "createWeek에서 courseId가 null이면 IllegalArgumentException이 발생한다"() {
        given: "유효한 request"
        def request = CreateWeekRequestDto.builder()
                .weekNumber(1)
                .weekTitle("1주차")
                .build()

        when: "courseId가 null로 호출하면"
        courseWeekContentService.createWeek(null, request, 1L)

        then: "IllegalArgumentException이 발생한다"
        def exception = thrown(IllegalArgumentException)
        exception.message == "강의 ID는 필수입니다."
    }

    def "createWeek에서 강의를 찾을 수 없으면 IllegalArgumentException이 발생한다"() {
        given: "존재하지 않는 courseId"
        def request = CreateWeekRequestDto.builder()
                .weekNumber(1)
                .weekTitle("1주차")
                .build()

        courseRepository.findById(999L) >> Optional.empty()

        when: "createWeek을 호출하면"
        courseWeekContentService.createWeek(999L, request, 1L)

        then: "IllegalArgumentException이 발생한다"
        def exception = thrown(IllegalArgumentException)
        exception.message == "강의를 찾을 수 없습니다."
    }

    def "createWeek에서 권한이 없으면 IllegalArgumentException이 발생한다"() {
        given: "다른 교수의 강의"
        def request = CreateWeekRequestDto.builder()
                .weekNumber(1)
                .weekTitle("1주차")
                .build()

        courseRepository.findById(1L) >> Optional.of(course)

        when: "다른 교수가 생성을 시도하면"
        courseWeekContentService.createWeek(1L, request, 999L)

        then: "IllegalArgumentException이 발생한다"
        def exception = thrown(IllegalArgumentException)
        exception.message == "주차 생성 권한이 없습니다."
    }

    def "createWeek에서 주차 번호가 16을 초과하면 IllegalArgumentException이 발생한다"() {
        given: "주차 번호가 17인 request"
        def request = CreateWeekRequestDto.builder()
                .weekNumber(17)
                .weekTitle("17주차")
                .build()

        courseRepository.findById(1L) >> Optional.of(course)

        when: "createWeek을 호출하면"
        courseWeekContentService.createWeek(1L, request, 1L)

        then: "IllegalArgumentException이 발생한다"
        def exception = thrown(IllegalArgumentException)
        exception.message == "주차는 최대 16주까지만 생성할 수 있습니다."
    }

    def "createWeek에서 중복 주차 번호면 IllegalArgumentException이 발생한다"() {
        given: "중복된 주차 번호"
        def request = CreateWeekRequestDto.builder()
                .weekNumber(1)
                .weekTitle("1주차")
                .build()

        courseRepository.findById(1L) >> Optional.of(course)
        courseWeekRepository.existsByCourseIdAndWeekNumber(1L, 1) >> true

        when: "createWeek을 호출하면"
        courseWeekContentService.createWeek(1L, request, 1L)

        then: "IllegalArgumentException이 발생한다"
        def exception = thrown(IllegalArgumentException)
        exception.message.contains("이미 1주차가 존재합니다")
    }

    def "createWeek에서 콘텐츠 타입이 없으면 IllegalArgumentException이 발생한다"() {
        given: "콘텐츠 타입이 없는 request"
        def contentRequest = CreateWeekContentRequestDto.builder()
                .contentType(null)
                .title("제목")
                .contentUrl("https://example.com")
                .build()

        def request = CreateWeekRequestDto.builder()
                .weekNumber(1)
                .weekTitle("1주차")
                .contents([contentRequest])
                .build()

        courseRepository.findById(1L) >> Optional.of(course)
        courseWeekRepository.existsByCourseIdAndWeekNumber(1L, 1) >> false

        when: "createWeek을 호출하면"
        courseWeekContentService.createWeek(1L, request, 1L)

        then: "IllegalArgumentException이 발생한다"
        def exception = thrown(IllegalArgumentException)
        exception.message == "콘텐츠 타입은 필수입니다."
    }

    def "createWeek에서 지원하지 않는 콘텐츠 타입이면 IllegalArgumentException이 발생한다"() {
        given: "지원하지 않는 콘텐츠 타입"
        def contentRequest = CreateWeekContentRequestDto.builder()
                .contentType("INVALID_TYPE")
                .title("제목")
                .contentUrl("https://example.com")
                .build()

        def request = CreateWeekRequestDto.builder()
                .weekNumber(1)
                .weekTitle("1주차")
                .contents([contentRequest])
                .build()

        courseRepository.findById(1L) >> Optional.of(course)
        courseWeekRepository.existsByCourseIdAndWeekNumber(1L, 1) >> false

        when: "createWeek을 호출하면"
        courseWeekContentService.createWeek(1L, request, 1L)

        then: "IllegalArgumentException이 발생한다"
        def exception = thrown(IllegalArgumentException)
        exception.message.contains("지원하지 않는 콘텐츠 타입")
    }

    // updateWeek 테스트

    def "updateWeek에서 courseId가 null이면 IllegalArgumentException이 발생한다"() {
        given: "유효한 request"
        def request = UpdateWeekRequestDto.builder()
                .weekTitle("수정된 제목")
                .build()

        when: "courseId가 null로 호출하면"
        courseWeekContentService.updateWeek(null, 1L, request, 1L)

        then: "IllegalArgumentException이 발생한다"
        def exception = thrown(IllegalArgumentException)
        exception.message == "강의 ID는 필수입니다."
    }

    def "updateWeek에서 weekId가 null이면 IllegalArgumentException이 발생한다"() {
        given: "유효한 request"
        def request = UpdateWeekRequestDto.builder()
                .weekTitle("수정된 제목")
                .build()

        courseRepository.findById(1L) >> Optional.of(course)

        when: "weekId가 null로 호출하면"
        courseWeekContentService.updateWeek(1L, null, request, 1L)

        then: "IllegalArgumentException이 발생한다"
        def exception = thrown(IllegalArgumentException)
        exception.message == "주차 ID는 필수입니다."
    }

    def "updateWeek에서 주차를 찾을 수 없으면 IllegalArgumentException이 발생한다"() {
        given: "존재하지 않는 weekId"
        def request = UpdateWeekRequestDto.builder()
                .weekTitle("수정된 제목")
                .build()

        courseRepository.findById(1L) >> Optional.of(course)
        courseWeekRepository.findById(999L) >> Optional.empty()

        when: "updateWeek을 호출하면"
        courseWeekContentService.updateWeek(1L, 999L, request, 1L)

        then: "IllegalArgumentException이 발생한다"
        def exception = thrown(IllegalArgumentException)
        exception.message == "주차를 찾을 수 없습니다."
    }

    def "updateWeek에서 해당 강의의 주차가 아니면 IllegalArgumentException이 발생한다"() {
        given: "다른 강의의 주차"
        def request = UpdateWeekRequestDto.builder()
                .weekTitle("수정된 제목")
                .build()

        def otherCourse = Mock(Course) {
            getId() >> 999L
        }
        def otherWeek = Mock(CourseWeek) {
            getId() >> 1L
            getCourse() >> otherCourse
        }

        courseRepository.findById(1L) >> Optional.of(course)
        courseWeekRepository.findById(1L) >> Optional.of(otherWeek)

        when: "updateWeek을 호출하면"
        courseWeekContentService.updateWeek(1L, 1L, request, 1L)

        then: "IllegalArgumentException이 발생한다"
        def exception = thrown(IllegalArgumentException)
        exception.message == "해당 강의의 주차가 아닙니다."
    }

    // deleteWeek 테스트

    def "deleteWeek에서 모든 파라미터가 null이면 IllegalArgumentException이 발생한다"() {
        when: "모든 파라미터가 null로 호출하면"
        courseWeekContentService.deleteWeek(null, null, null)

        then: "IllegalArgumentException이 발생한다"
        def exception = thrown(IllegalArgumentException)
        exception.message == "잘못된 요청입니다."
    }

    def "deleteWeek에서 권한이 없으면 IllegalArgumentException이 발생한다"() {
        given: "다른 교수의 강의"
        courseRepository.findById(1L) >> Optional.of(course)

        when: "다른 교수가 삭제를 시도하면"
        courseWeekContentService.deleteWeek(1L, 1L, 999L)

        then: "IllegalArgumentException이 발생한다"
        def exception = thrown(IllegalArgumentException)
        exception.message == "주차 삭제 권한이 없습니다."
    }

    def "deleteWeek에서 정상적으로 삭제된다"() {
        given: "유효한 요청"
        courseRepository.findById(1L) >> Optional.of(course)
        courseWeekRepository.findById(1L) >> Optional.of(courseWeek)

        when: "deleteWeek을 호출하면"
        courseWeekContentService.deleteWeek(1L, 1L, 1L)

        then: "주차와 콘텐츠가 삭제된다"
        1 * weekContentRepository.deleteByWeekId(1L)
        1 * courseWeekRepository.delete(courseWeek)
    }

    // createContent 테스트

    def "createContent에서 모든 파라미터가 null이면 IllegalArgumentException이 발생한다"() {
        given: "유효한 request"
        def request = CreateWeekContentRequestDto.builder()
                .contentType("VIDEO")
                .title("제목")
                .contentUrl("https://example.com")
                .build()

        when: "모든 파라미터가 null로 호출하면"
        courseWeekContentService.createContent(null, null, request, null)

        then: "IllegalArgumentException이 발생한다"
        def exception = thrown(IllegalArgumentException)
        exception.message == "잘못된 요청입니다."
    }

    def "createContent에서 권한이 없으면 IllegalArgumentException이 발생한다"() {
        given: "다른 교수의 강의"
        def request = CreateWeekContentRequestDto.builder()
                .contentType("VIDEO")
                .title("제목")
                .contentUrl("https://example.com")
                .build()

        courseRepository.findById(1L) >> Optional.of(course)

        when: "다른 교수가 생성을 시도하면"
        courseWeekContentService.createContent(1L, 1L, request, 999L)

        then: "IllegalArgumentException이 발생한다"
        def exception = thrown(IllegalArgumentException)
        exception.message == "콘텐츠 등록 권한이 없습니다."
    }

    def "createContent에서 해당 강의의 주차가 아니면 IllegalArgumentException이 발생한다"() {
        given: "다른 강의의 주차"
        def request = CreateWeekContentRequestDto.builder()
                .contentType("VIDEO")
                .title("제목")
                .contentUrl("https://example.com")
                .build()

        def otherCourse = Mock(Course) {
            getId() >> 999L
        }
        def otherWeek = Mock(CourseWeek) {
            getId() >> 1L
            getCourse() >> otherCourse
        }

        courseRepository.findById(1L) >> Optional.of(course)
        courseWeekRepository.findById(1L) >> Optional.of(otherWeek)

        when: "createContent를 호출하면"
        courseWeekContentService.createContent(1L, 1L, request, 1L)

        then: "IllegalArgumentException이 발생한다"
        def exception = thrown(IllegalArgumentException)
        exception.message == "해당 강의의 주차가 아닙니다."
    }

    def "createContent에서 displayOrder 중복이면 IllegalArgumentException이 발생한다"() {
        given: "중복된 displayOrder"
        def request = CreateWeekContentRequestDto.builder()
                .contentType("VIDEO")
                .title("제목")
                .contentUrl("https://example.com")
                .order(1)
                .build()

        courseRepository.findById(1L) >> Optional.of(course)
        courseWeekRepository.findById(1L) >> Optional.of(courseWeek)
        weekContentRepository.existsByWeekIdAndDisplayOrder(1L, 1) >> true

        when: "createContent를 호출하면"
        courseWeekContentService.createContent(1L, 1L, request, 1L)

        then: "IllegalArgumentException이 발생한다"
        def exception = thrown(IllegalArgumentException)
        exception.message.contains("이미 동일한 순서")
    }

    // updateContent 테스트

    def "updateContent에서 모든 파라미터가 null이면 IllegalArgumentException이 발생한다"() {
        given: "유효한 request"
        def request = UpdateWeekContentRequestDto.builder()
                .title("수정된 제목")
                .build()

        when: "모든 파라미터가 null로 호출하면"
        courseWeekContentService.updateContent(null, null, null, request, null)

        then: "IllegalArgumentException이 발생한다"
        def exception = thrown(IllegalArgumentException)
        exception.message == "잘못된 요청입니다."
    }

    def "updateContent에서 콘텐츠를 찾을 수 없으면 IllegalArgumentException이 발생한다"() {
        given: "존재하지 않는 contentId"
        def request = UpdateWeekContentRequestDto.builder()
                .title("수정된 제목")
                .build()

        courseRepository.findById(1L) >> Optional.of(course)
        weekContentRepository.findById(999L) >> Optional.empty()

        when: "updateContent를 호출하면"
        courseWeekContentService.updateContent(1L, 1L, 999L, request, 1L)

        then: "IllegalArgumentException이 발생한다"
        def exception = thrown(IllegalArgumentException)
        exception.message == "콘텐츠를 찾을 수 없습니다."
    }

    // deleteContent 테스트

    def "deleteContent에서 모든 파라미터가 null이면 IllegalArgumentException이 발생한다"() {
        when: "모든 파라미터가 null로 호출하면"
        courseWeekContentService.deleteContent(null, null, null, null)

        then: "IllegalArgumentException이 발생한다"
        def exception = thrown(IllegalArgumentException)
        exception.message == "잘못된 요청입니다."
    }

    def "deleteContent에서 권한이 없으면 IllegalArgumentException이 발생한다"() {
        given: "다른 교수의 강의"
        courseRepository.findById(1L) >> Optional.of(course)

        when: "다른 교수가 삭제를 시도하면"
        courseWeekContentService.deleteContent(1L, 1L, 1L, 999L)

        then: "IllegalArgumentException이 발생한다"
        def exception = thrown(IllegalArgumentException)
        exception.message == "콘텐츠 삭제 권한이 없습니다."
    }

    def "deleteContent에서 정상적으로 삭제된다"() {
        given: "유효한 요청"
        courseRepository.findById(1L) >> Optional.of(course)
        weekContentRepository.findById(1L) >> Optional.of(weekContent)

        when: "deleteContent를 호출하면"
        courseWeekContentService.deleteContent(1L, 1L, 1L, 1L)

        then: "콘텐츠가 삭제된다"
        1 * weekContentRepository.delete(weekContent)
    }

    // getWeeks 테스트

    def "getWeeks에서 courseId가 null이면 IllegalArgumentException이 발생한다"() {
        when: "courseId가 null로 호출하면"
        courseWeekContentService.getWeeks(null, 1L)

        then: "IllegalArgumentException이 발생한다"
        def exception = thrown(IllegalArgumentException)
        exception.message == "강의 ID는 필수입니다."
    }

    def "getWeeks에서 교수도 수강생도 아니면 IllegalArgumentException이 발생한다"() {
        given: "권한이 없는 사용자"
        courseRepository.findById(1L) >> Optional.of(course)
        enrollmentRepository.existsByStudentIdAndCourseId(999L, 1L) >> false

        when: "권한이 없는 사용자가 조회를 시도하면"
        courseWeekContentService.getWeeks(1L, 999L)

        then: "IllegalArgumentException이 발생한다"
        def exception = thrown(IllegalArgumentException)
        exception.message == "주차 조회 권한이 없습니다."
    }

    def "getWeeks에서 교수는 조회할 수 있다"() {
        given: "교수인 경우"
        courseRepository.findById(1L) >> Optional.of(course)
        courseWeekRepository.findByCourseId(1L) >> [courseWeek]
        weekContentRepository.findByWeekIdOrderByDisplayOrder(1L) >> []

        when: "교수가 조회하면"
        def result = courseWeekContentService.getWeeks(1L, 1L)

        then: "주차 목록이 반환된다"
        result != null
        result.size() == 1
    }

    def "getWeeks에서 수강생은 조회할 수 있다"() {
        given: "수강생인 경우"
        courseRepository.findById(1L) >> Optional.of(course)
        enrollmentRepository.existsByStudentIdAndCourseId(100L, 1L) >> true
        courseWeekRepository.findByCourseId(1L) >> [courseWeek]
        weekContentRepository.findByWeekIdOrderByDisplayOrder(1L) >> []

        when: "수강생이 조회하면"
        def result = courseWeekContentService.getWeeks(1L, 100L)

        then: "주차 목록이 반환된다"
        result != null
        result.size() == 1
    }

    // getWeekContents 테스트

    def "getWeekContents에서 모든 파라미터가 null이면 IllegalArgumentException이 발생한다"() {
        when: "모든 파라미터가 null로 호출하면"
        courseWeekContentService.getWeekContents(null, null, null)

        then: "IllegalArgumentException이 발생한다"
        def exception = thrown(IllegalArgumentException)
        exception.message == "잘못된 요청입니다."
    }

    def "getWeekContents에서 권한이 없으면 IllegalArgumentException이 발생한다"() {
        given: "다른 교수의 강의"
        courseRepository.findById(1L) >> Optional.of(course)

        when: "다른 교수가 조회를 시도하면"
        courseWeekContentService.getWeekContents(1L, 1L, 999L)

        then: "IllegalArgumentException이 발생한다"
        def exception = thrown(IllegalArgumentException)
        exception.message == "콘텐츠 조회 권한이 없습니다."
    }

    def "getWeekContents에서 정상적으로 콘텐츠 목록을 조회한다"() {
        given: "유효한 요청"
        courseRepository.findById(1L) >> Optional.of(course)
        courseWeekRepository.findById(1L) >> Optional.of(courseWeek)
        weekContentRepository.findByWeekIdOrderByDisplayOrder(1L) >> [weekContent]

        when: "getWeekContents를 호출하면"
        def result = courseWeekContentService.getWeekContents(1L, 1L, 1L)

        then: "콘텐츠 목록이 반환된다"
        result != null
        result.weekId == 1L
        result.weekNumber == 1
        result.contents.size() == 1
    }

    // reorderContents 테스트

    def "reorderContents에서 courseId가 null이면 IllegalArgumentException이 발생한다"() {
        given: "유효한 request"
        def request = ReorderContentsRequestDto.builder().build()

        when: "courseId가 null로 호출하면"
        courseWeekContentService.reorderContents(null, 1L, request, 1L)

        then: "IllegalArgumentException이 발생한다"
        def exception = thrown(IllegalArgumentException)
        exception.message == "강의 ID는 필수입니다."
    }

    def "reorderContents에서 weekId가 null이면 IllegalArgumentException이 발생한다"() {
        given: "유효한 request"
        def request = ReorderContentsRequestDto.builder().build()

        courseRepository.findById(1L) >> Optional.of(course)

        when: "weekId가 null로 호출하면"
        courseWeekContentService.reorderContents(1L, null, request, 1L)

        then: "IllegalArgumentException이 발생한다"
        def exception = thrown(IllegalArgumentException)
        exception.message == "주차 ID는 필수입니다."
    }

    def "reorderContents에서 권한이 없으면 IllegalArgumentException이 발생한다"() {
        given: "다른 교수의 강의"
        def request = ReorderContentsRequestDto.builder().build()

        courseRepository.findById(1L) >> Optional.of(course)

        when: "다른 교수가 순서 변경을 시도하면"
        courseWeekContentService.reorderContents(1L, 1L, request, 999L)

        then: "IllegalArgumentException이 발생한다"
        def exception = thrown(IllegalArgumentException)
        exception.message == "콘텐츠 순서 변경 권한이 없습니다."
    }

    def "reorderContents에서 해당 주차의 콘텐츠가 아니면 IllegalArgumentException이 발생한다"() {
        given: "다른 주차의 콘텐츠"
        def orderDto = ContentOrderDto.builder()
                .contentId(1L)
                .order(2)
                .build()
        def request = ReorderContentsRequestDto.builder()
                .contentOrders([orderDto])
                .build()

        def otherWeek = Mock(CourseWeek) {
            getId() >> 999L
        }
        def otherContent = Mock(WeekContent) {
            getId() >> 1L
            getWeek() >> otherWeek
        }

        courseRepository.findById(1L) >> Optional.of(course)
        courseWeekRepository.findById(1L) >> Optional.of(courseWeek)
        weekContentRepository.findById(1L) >> Optional.of(otherContent)

        when: "reorderContents를 호출하면"
        courseWeekContentService.reorderContents(1L, 1L, request, 1L)

        then: "IllegalArgumentException이 발생한다"
        def exception = thrown(IllegalArgumentException)
        exception.message == "해당 주차의 콘텐츠가 아닙니다."
    }
}
