package com.mzc.backend.lms.domains.course.course.entity

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

/**
 * WeekContent 엔티티 Spock 테스트
 */
class WeekContentSpec extends Specification {

    def courseWeek = Mock(CourseWeek)

    @Subject
    WeekContent weekContent

    def setup() {
        weekContent = WeekContent.builder()
                .id(1L)
                .week(courseWeek)
                .contentType("VIDEO")
                .title("1강 - 개요")
                .contentUrl("https://example.com/video1.mp4")
                .duration("45:30")
                .displayOrder(1)
                .build()
    }

    def "WeekContent 객체가 올바르게 생성된다"() {
        expect: "모든 필드가 올바르게 설정된다"
        weekContent.id == 1L
        weekContent.week == courseWeek
        weekContent.contentType == "VIDEO"
        weekContent.title == "1강 - 개요"
        weekContent.contentUrl == "https://example.com/video1.mp4"
        weekContent.duration == "45:30"
        weekContent.displayOrder == 1
    }

    def "update 메서드로 모든 필드를 수정할 수 있다"() {
        when: "모든 필드를 수정하면"
        weekContent.update("DOCUMENT", "수정된 제목", "https://new-url.com/doc.pdf", "30:00", 2)

        then: "모든 값이 변경된다"
        weekContent.contentType == "DOCUMENT"
        weekContent.title == "수정된 제목"
        weekContent.contentUrl == "https://new-url.com/doc.pdf"
        weekContent.duration == "30:00"
        weekContent.displayOrder == 2
    }

    def "update 메서드에서 null 파라미터는 기존 값을 유지한다"() {
        when: "일부 파라미터만 전달하면"
        weekContent.update(null, "새 제목", null, null, null)

        then: "전달된 값만 변경되고 나머지는 기존 값을 유지한다"
        weekContent.contentType == "VIDEO"
        weekContent.title == "새 제목"
        weekContent.contentUrl == "https://example.com/video1.mp4"
        weekContent.duration == "45:30"
        weekContent.displayOrder == 1
    }

    def "update 메서드에서 모든 파라미터가 null이면 기존 값을 유지한다"() {
        when: "모든 파라미터를 null로 전달하면"
        weekContent.update(null, null, null, null, null)

        then: "모든 필드가 기존 값을 유지한다"
        weekContent.contentType == "VIDEO"
        weekContent.title == "1강 - 개요"
        weekContent.contentUrl == "https://example.com/video1.mp4"
        weekContent.duration == "45:30"
        weekContent.displayOrder == 1
    }

    @Unroll
    def "contentType이 #contentType인 콘텐츠를 생성할 수 있다"() {
        given: "특정 콘텐츠 타입"
        def content = WeekContent.builder()
                .id(1L)
                .week(courseWeek)
                .contentType(contentType)
                .title("테스트 콘텐츠")
                .contentUrl("https://example.com/content")
                .displayOrder(1)
                .build()

        expect: "콘텐츠 타입이 올바르게 설정된다"
        content.contentType == contentType

        where:
        contentType << ["VIDEO", "DOCUMENT", "LINK", "QUIZ"]
    }

    def "update로 contentType만 수정할 수 있다"() {
        when: "contentType만 수정하면"
        weekContent.update("DOCUMENT", null, null, null, null)

        then: "contentType만 변경된다"
        weekContent.contentType == "DOCUMENT"
        weekContent.title == "1강 - 개요"
    }

    def "update로 title만 수정할 수 있다"() {
        when: "title만 수정하면"
        weekContent.update(null, "새로운 제목", null, null, null)

        then: "title만 변경된다"
        weekContent.title == "새로운 제목"
        weekContent.contentType == "VIDEO"
    }

    def "update로 contentUrl만 수정할 수 있다"() {
        when: "contentUrl만 수정하면"
        weekContent.update(null, null, "https://new-url.com", null, null)

        then: "contentUrl만 변경된다"
        weekContent.contentUrl == "https://new-url.com"
        weekContent.title == "1강 - 개요"
    }

    def "update로 duration만 수정할 수 있다"() {
        when: "duration만 수정하면"
        weekContent.update(null, null, null, "60:00", null)

        then: "duration만 변경된다"
        weekContent.duration == "60:00"
        weekContent.contentUrl == "https://example.com/video1.mp4"
    }

    def "update로 displayOrder만 수정할 수 있다"() {
        when: "displayOrder만 수정하면"
        weekContent.update(null, null, null, null, 5)

        then: "displayOrder만 변경된다"
        weekContent.displayOrder == 5
        weekContent.duration == "45:30"
    }

    @Unroll
    def "displayOrder #order로 콘텐츠 순서를 설정할 수 있다"() {
        given: "특정 순서"
        def content = WeekContent.builder()
                .id(1L)
                .week(courseWeek)
                .contentType("VIDEO")
                .title("테스트")
                .contentUrl("https://example.com")
                .displayOrder(order)
                .build()

        expect: "순서가 올바르게 설정된다"
        content.displayOrder == order

        where:
        order << [1, 2, 3, 4, 5, 10, 100]
    }

    def "duration이 null인 콘텐츠를 생성할 수 있다"() {
        given: "duration이 null인 콘텐츠 (예: DOCUMENT, LINK)"
        def content = WeekContent.builder()
                .id(1L)
                .week(courseWeek)
                .contentType("DOCUMENT")
                .title("강의자료")
                .contentUrl("https://example.com/doc.pdf")
                .duration(null)
                .displayOrder(1)
                .build()

        expect: "duration이 null로 설정된다"
        content.duration == null
        content.contentType == "DOCUMENT"
    }

    def "여러 번 update를 호출해도 정상 동작한다"() {
        when: "여러 번 update를 호출하면"
        weekContent.update("DOCUMENT", null, null, null, null)
        weekContent.update(null, "중간 제목", null, null, null)
        weekContent.update(null, "최종 제목", "https://final-url.com", null, 10)

        then: "마지막 값으로 설정된다"
        weekContent.contentType == "DOCUMENT"
        weekContent.title == "최종 제목"
        weekContent.contentUrl == "https://final-url.com"
        weekContent.displayOrder == 10
    }
}
