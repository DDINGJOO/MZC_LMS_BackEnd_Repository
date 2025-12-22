package com.mzc.backend.lms.domains.user.auth.jwt.service

import com.mzc.backend.lms.domains.user.professor.entity.Professor
import com.mzc.backend.lms.domains.user.student.entity.Student
import com.mzc.backend.lms.domains.user.user.entity.User
import spock.lang.Specification
import spock.lang.Subject

import java.lang.reflect.Field

/**
 * JwtTokenService 테스트
 * JWT 토큰 생성, 검증, 정보 추출 기능 테스트
 */
class JwtTokenServiceSpec extends Specification {

    @Subject
    JwtTokenService jwtTokenService

    def setup() {
        jwtTokenService = new JwtTokenService()
        setField(jwtTokenService, "secretKey", "testSecretKeyForJwtTokenService123456789012345678901234567890")
        setField(jwtTokenService, "accessTokenExpiration", 1800000L)  // 30분
        setField(jwtTokenService, "refreshTokenExpiration", 604800000L)  // 7일
    }

    private void setField(Object target, String fieldName, Object value) {
        Field field = target.class.getDeclaredField(fieldName)
        field.setAccessible(true)
        field.set(target, value)
    }

    def "학생용 Access Token을 생성한다"() {
        given: "사용자와 학생 정보"
        def user = Mock(User) {
            getId() >> 1L
            getEmail() >> "encrypted_email"
        }
        def student = Mock(Student) {
            getStudentNumber() >> 2025010001L
        }

        when: "학생용 Access Token을 생성하면"
        def token = jwtTokenService.generateStudentAccessToken(user, student)

        then: "유효한 토큰이 생성된다"
        token != null
        !token.isEmpty()
    }

    def "교수용 Access Token을 생성한다"() {
        given: "사용자와 교수 정보"
        def user = Mock(User) {
            getId() >> 2L
            getEmail() >> "encrypted_professor_email"
        }
        def professor = Mock(Professor) {
            getProfessorNumber() >> 1001L
        }

        when: "교수용 Access Token을 생성하면"
        def token = jwtTokenService.generateProfessorAccessToken(user, professor)

        then: "유효한 토큰이 생성된다"
        token != null
        !token.isEmpty()
    }

    def "Refresh Token을 생성한다"() {
        given: "사용자 정보"
        def user = Mock(User) {
            getId() >> 1L
            getEmail() >> "encrypted_email"
        }

        when: "Refresh Token을 생성하면"
        def token = jwtTokenService.generateRefreshToken(user)

        then: "유효한 토큰이 생성된다"
        token != null
        !token.isEmpty()
    }

    def "토큰에서 사용자 ID를 추출한다"() {
        given: "생성된 Access Token"
        def user = Mock(User) {
            getId() >> 123L
            getEmail() >> "test@example.com"
        }
        def student = Mock(Student) {
            getStudentNumber() >> 2025010001L
        }
        def token = jwtTokenService.generateStudentAccessToken(user, student)

        when: "토큰에서 사용자 ID를 추출하면"
        def userId = jwtTokenService.extractUserId(token)

        then: "올바른 사용자 ID를 반환한다"
        userId == 123L
    }

    def "토큰에서 이메일을 추출한다"() {
        given: "생성된 Access Token"
        def email = "test@example.com"
        def user = Mock(User) {
            getId() >> 1L
            getEmail() >> email
        }
        def student = Mock(Student) {
            getStudentNumber() >> 2025010001L
        }
        def token = jwtTokenService.generateStudentAccessToken(user, student)

        when: "토큰에서 이메일을 추출하면"
        def extractedEmail = jwtTokenService.extractEmail(token)

        then: "올바른 이메일을 반환한다"
        extractedEmail == email
    }

    def "토큰에서 사용자 타입을 추출한다 - #userType"() {
        given: "생성된 Access Token"
        def user = Mock(User) {
            getId() >> 1L
            getEmail() >> "test@example.com"
        }
        def token

        if (userType == "STUDENT") {
            def student = Mock(Student) { getStudentNumber() >> 2025010001L }
            token = jwtTokenService.generateStudentAccessToken(user, student)
        } else {
            def professor = Mock(Professor) { getProfessorNumber() >> 1001L }
            token = jwtTokenService.generateProfessorAccessToken(user, professor)
        }

        when: "토큰에서 사용자 타입을 추출하면"
        def extractedType = jwtTokenService.extractUserType(token)

        then: "올바른 사용자 타입을 반환한다"
        extractedType == userType

        where:
        userType << ["STUDENT", "PROFESSOR"]
    }

    def "토큰에서 학번/교번을 추출한다"() {
        given: "생성된 Access Token"
        def user = Mock(User) {
            getId() >> 1L
            getEmail() >> "test@example.com"
        }
        def student = Mock(Student) {
            getStudentNumber() >> 2025010001L
        }
        def token = jwtTokenService.generateStudentAccessToken(user, student)

        when: "토큰에서 학번을 추출하면"
        def userNumber = jwtTokenService.extractUserNumber(token)

        then: "올바른 학번을 반환한다"
        userNumber == "2025010001"
    }

    def "유효한 토큰은 만료되지 않은 것으로 판단한다"() {
        given: "새로 생성된 Access Token"
        def user = Mock(User) {
            getId() >> 1L
            getEmail() >> "test@example.com"
        }
        def student = Mock(Student) {
            getStudentNumber() >> 2025010001L
        }
        def token = jwtTokenService.generateStudentAccessToken(user, student)

        when: "토큰 만료 여부를 확인하면"
        def isExpired = jwtTokenService.isTokenExpired(token)

        then: "만료되지 않은 것으로 반환한다"
        !isExpired
    }

    def "유효한 토큰 검증 시 true를 반환한다"() {
        given: "새로 생성된 Access Token"
        def user = Mock(User) {
            getId() >> 1L
            getEmail() >> "test@example.com"
        }
        def student = Mock(Student) {
            getStudentNumber() >> 2025010001L
        }
        def token = jwtTokenService.generateStudentAccessToken(user, student)

        when: "토큰 유효성을 검증하면"
        def isValid = jwtTokenService.validateToken(token)

        then: "유효한 것으로 반환한다"
        isValid
    }

    def "잘못된 토큰 검증 시 false를 반환한다"() {
        given: "잘못된 토큰"
        def invalidToken = "invalid.token.string"

        when: "토큰 유효성을 검증하면"
        def isValid = jwtTokenService.validateToken(invalidToken)

        then: "유효하지 않은 것으로 반환한다"
        !isValid
    }

    def "사용자 정보와 함께 토큰 검증 - 일치하는 경우"() {
        given: "Access Token과 동일한 사용자"
        def email = "test@example.com"
        def user = Mock(User) {
            getId() >> 1L
            getEmail() >> email
        }
        def student = Mock(Student) {
            getStudentNumber() >> 2025010001L
        }
        def token = jwtTokenService.generateStudentAccessToken(user, student)

        when: "사용자 정보와 함께 토큰을 검증하면"
        def isValid = jwtTokenService.validateToken(token, user)

        then: "유효한 것으로 반환한다"
        isValid
    }

    def "사용자 정보와 함께 토큰 검증 - 불일치하는 경우"() {
        given: "Access Token과 다른 사용자"
        def user = Mock(User) {
            getId() >> 1L
            getEmail() >> "original@example.com"
        }
        def student = Mock(Student) {
            getStudentNumber() >> 2025010001L
        }
        def token = jwtTokenService.generateStudentAccessToken(user, student)

        def differentUser = Mock(User) {
            getEmail() >> "different@example.com"
        }

        when: "다른 사용자 정보로 토큰을 검증하면"
        def isValid = jwtTokenService.validateToken(token, differentUser)

        then: "유효하지 않은 것으로 반환한다"
        !isValid
    }

    def "학생 토큰으로 isStudent 확인 시 true를 반환한다"() {
        given: "학생용 Access Token"
        def user = Mock(User) {
            getId() >> 1L
            getEmail() >> "student@example.com"
        }
        def student = Mock(Student) {
            getStudentNumber() >> 2025010001L
        }
        def token = jwtTokenService.generateStudentAccessToken(user, student)

        when: "학생 여부를 확인하면"
        def isStudent = jwtTokenService.isStudent(token)
        def isProfessor = jwtTokenService.isProfessor(token)

        then: "학생으로 판단한다"
        isStudent
        !isProfessor
    }

    def "교수 토큰으로 isProfessor 확인 시 true를 반환한다"() {
        given: "교수용 Access Token"
        def user = Mock(User) {
            getId() >> 2L
            getEmail() >> "professor@example.com"
        }
        def professor = Mock(Professor) {
            getProfessorNumber() >> 1001L
        }
        def token = jwtTokenService.generateProfessorAccessToken(user, professor)

        when: "교수 여부를 확인하면"
        def isStudent = jwtTokenService.isStudent(token)
        def isProfessor = jwtTokenService.isProfessor(token)

        then: "교수로 판단한다"
        !isStudent
        isProfessor
    }
}
