package com.mzc.backend.lms.domains.course.course.entity

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

/**
 * CourseWeek 엔티티 Spock 테스트
 */
class CourseWeekSpec extends Specification {

    def course = Mock(Course)

    @Subject
    CourseWeek courseWeek

    def setup() {
        courseWeek = CourseWeek.builder()
                .id(1L)
                .course(course)
                .weekNumber(1)
                .weekTitle("1주차 - 오리엔테이션")
                .build()
    }

    def "CourseWeek 객체가 올바르게 생성된다"() {
        expect: "모든 필드가 올바르게 설정된다"
        courseWeek.id == 1L
        courseWeek.course == course
        courseWeek.weekNumber == 1
        courseWeek.weekTitle == "1주차 - 오리엔테이션"
    }

    def "update 메서드로 주차 번호와 제목을 수정할 수 있다"() {
        when: "주차 정보를 수정하면"
        courseWeek.update(2, "2주차 - 기본 개념")

        then: "값이 변경된다"
        courseWeek.weekNumber == 2
        courseWeek.weekTitle == "2주차 - 기본 개념"
    }

    def "update 메서드에서 weekNumber가 null이면 기존 값을 유지한다"() {
        when: "weekNumber를 null로 업데이트하면"
        courseWeek.update(null, "수정된 제목")

        then: "weekNumber는 기존 값을 유지하고 weekTitle만 변경된다"
        courseWeek.weekNumber == 1
        courseWeek.weekTitle == "수정된 제목"
    }

    def "update 메서드에서 weekTitle이 null이면 기존 값을 유지한다"() {
        when: "weekTitle을 null로 업데이트하면"
        courseWeek.update(5, null)

        then: "weekTitle은 기존 값을 유지하고 weekNumber만 변경된다"
        courseWeek.weekNumber == 5
        courseWeek.weekTitle == "1주차 - 오리엔테이션"
    }

    def "update 메서드에서 모든 파라미터가 null이면 기존 값을 유지한다"() {
        when: "모든 파라미터를 null로 업데이트하면"
        courseWeek.update(null, null)

        then: "모든 필드가 기존 값을 유지한다"
        courseWeek.weekNumber == 1
        courseWeek.weekTitle == "1주차 - 오리엔테이션"
    }

    @Unroll
    def "주차 번호 #weekNumber로 CourseWeek을 생성할 수 있다"() {
        given: "특정 주차 번호"
        def week = CourseWeek.builder()
                .id(1L)
                .course(course)
                .weekNumber(weekNumber)
                .weekTitle("${weekNumber}주차")
                .build()

        expect: "주차 번호가 올바르게 설정된다"
        week.weekNumber == weekNumber

        where:
        weekNumber << (1..16)
    }

    def "다양한 주차 제목으로 CourseWeek을 생성할 수 있다"() {
        given: "다양한 주차 제목"
        def titles = [
                "오리엔테이션",
                "Spring Framework 기초",
                "중간고사",
                "프로젝트 발표",
                "기말고사"
        ]

        expect: "각 제목으로 CourseWeek을 생성할 수 있다"
        titles.eachWithIndex { title, index ->
            def week = CourseWeek.builder()
                    .id((long) index + 1)
                    .course(course)
                    .weekNumber(index + 1)
                    .weekTitle(title)
                    .build()
            assert week.weekTitle == title
        }
    }

    def "여러 번 update를 호출해도 정상 동작한다"() {
        when: "여러 번 update를 호출하면"
        courseWeek.update(2, "2주차")
        courseWeek.update(3, "3주차")
        courseWeek.update(4, "최종 수정된 제목")

        then: "마지막 값으로 설정된다"
        courseWeek.weekNumber == 4
        courseWeek.weekTitle == "최종 수정된 제목"
    }
}
