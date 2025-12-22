package com.mzc.backend.lms.domains.course.course.entity

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

/**
 * CourseType 엔티티 Spock 테스트
 */
class CourseTypeSpec extends Specification {

    @Subject
    CourseType courseType

    def "CourseType 객체가 올바르게 생성된다"() {
        given: "전공필수 타입"
        courseType = CourseType.builder()
                .id(1L)
                .typeCode(1)
                .category(0)
                .build()

        expect: "모든 필드가 올바르게 설정된다"
        courseType.id == 1L
        courseType.typeCode == 1
        courseType.category == 0
    }

    @Unroll
    def "typeCode #typeCode는 getTypeCodeString()으로 '#expectedString'을 반환한다"() {
        given: "특정 typeCode를 가진 CourseType"
        courseType = CourseType.builder()
                .id(1L)
                .typeCode(typeCode)
                .category(category)
                .build()

        expect: "올바른 문자열을 반환한다"
        courseType.getTypeCodeString() == expectedString

        where:
        typeCode | category | expectedString
        1        | 0        | "MAJOR_REQ"
        2        | 0        | "MAJOR_ELEC"
        3        | 1        | "GEN_REQ"
        4        | 1        | "GEN_ELEC"
        5        | 0        | "UNKNOWN"
        0        | 0        | "UNKNOWN"
        -1       | 0        | "UNKNOWN"
    }

    @Unroll
    def "typeCode #typeCode는 getTypeName()으로 '#expectedName'을 반환한다"() {
        given: "특정 typeCode를 가진 CourseType"
        courseType = CourseType.builder()
                .id(1L)
                .typeCode(typeCode)
                .category(category)
                .build()

        expect: "올바른 한글 이름을 반환한다"
        courseType.getTypeName() == expectedName

        where:
        typeCode | category | expectedName
        1        | 0        | "전공필수"
        2        | 0        | "전공선택"
        3        | 1        | "교양필수"
        4        | 1        | "교양선택"
        5        | 0        | "미지정"
        0        | 0        | "미지정"
        -1       | 0        | "미지정"
    }

    @Unroll
    def "typeCode #typeCode는 getColor()로 '#expectedColor'을 반환한다"() {
        given: "특정 typeCode를 가진 CourseType"
        courseType = CourseType.builder()
                .id(1L)
                .typeCode(typeCode)
                .category(category)
                .build()

        expect: "올바른 색상 코드를 반환한다"
        courseType.getColor() == expectedColor

        where:
        typeCode | category | expectedColor
        1        | 0        | "#FFB4C8"   // 전공필수 - 분홍
        2        | 0        | "#B4D7FF"   // 전공선택 - 파랑
        3        | 1        | "#FFD9B4"   // 교양필수 - 주황
        4        | 1        | "#C8E6C9"   // 교양선택 - 초록
        5        | 0        | "#E0E0E0"   // 기본 - 회색
        0        | 0        | "#E0E0E0"
        -1       | 0        | "#E0E0E0"
    }

    @Unroll
    def "parseTypeCode('#typeCodeString')는 #expectedTypeCode를 반환한다"() {
        expect: "올바른 typeCode 숫자를 반환한다"
        CourseType.parseTypeCode(typeCodeString) == expectedTypeCode

        where:
        typeCodeString | expectedTypeCode
        "MAJOR_REQ"    | 1
        "MAJOR_ELEC"   | 2
        "GEN_REQ"      | 3
        "GEN_ELEC"     | 4
    }

    def "parseTypeCode에 유효하지 않은 문자열을 전달하면 IllegalArgumentException이 발생한다"() {
        when: "유효하지 않은 문자열로 parseTypeCode를 호출하면"
        CourseType.parseTypeCode("INVALID_TYPE")

        then: "IllegalArgumentException이 발생한다"
        def exception = thrown(IllegalArgumentException)
        exception.message == "Invalid course type code: INVALID_TYPE"
    }

    def "parseTypeCode에 빈 문자열을 전달하면 IllegalArgumentException이 발생한다"() {
        when: "빈 문자열로 parseTypeCode를 호출하면"
        CourseType.parseTypeCode("")

        then: "IllegalArgumentException이 발생한다"
        thrown(IllegalArgumentException)
    }

    def "category가 0이면 전공과목이다"() {
        given: "전공 카테고리"
        courseType = CourseType.builder()
                .id(1L)
                .typeCode(1)
                .category(0)
                .build()

        expect: "category가 0이다"
        courseType.category == 0
    }

    def "category가 1이면 교양과목이다"() {
        given: "교양 카테고리"
        courseType = CourseType.builder()
                .id(1L)
                .typeCode(3)
                .category(1)
                .build()

        expect: "category가 1이다"
        courseType.category == 1
    }

    def "getTypeCodeString과 parseTypeCode는 상호 변환된다"() {
        given: "전공필수 타입"
        courseType = CourseType.builder()
                .id(1L)
                .typeCode(1)
                .category(0)
                .build()

        when: "typeCode를 문자열로 변환하고 다시 숫자로 변환하면"
        def typeCodeString = courseType.getTypeCodeString()
        def parsedTypeCode = CourseType.parseTypeCode(typeCodeString)

        then: "원래 값과 동일하다"
        parsedTypeCode == courseType.typeCode
    }

    def "모든 유효한 typeCode에 대해 getTypeCodeString과 parseTypeCode가 일관성을 유지한다"() {
        given: "모든 유효한 typeCode"
        def validTypeCodes = [1, 2, 3, 4]

        expect: "각 typeCode에 대해 문자열 변환과 역변환이 일관성을 유지한다"
        validTypeCodes.each { typeCode ->
            def ct = CourseType.builder()
                    .id(1L)
                    .typeCode(typeCode)
                    .category(typeCode <= 2 ? 0 : 1)
                    .build()
            def typeCodeString = ct.getTypeCodeString()
            def parsedTypeCode = CourseType.parseTypeCode(typeCodeString)
            assert parsedTypeCode == typeCode
        }
    }
}
