package com.mzc.backend.lms.domains.user.profile.service

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.mzc.backend.lms.domains.user.auth.encryption.service.EncryptionService
import com.mzc.backend.lms.domains.user.professor.entity.Professor
import com.mzc.backend.lms.domains.user.professor.repository.ProfessorRepository
import com.mzc.backend.lms.domains.user.profile.dto.UserBasicInfoDto
import com.mzc.backend.lms.domains.user.profile.repository.UserProfileRepository
import com.mzc.backend.lms.domains.user.student.entity.Student
import com.mzc.backend.lms.domains.user.student.repository.StudentRepository
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations
import spock.lang.Specification
import spock.lang.Subject

import java.util.concurrent.TimeUnit

/**
 * UserInfoCacheServiceImpl 테스트
 * Redis Cache-Aside 패턴으로 유저 기본 정보 조회 테스트
 */
class UserInfoCacheServiceImplSpec extends Specification {

    def redisTemplate = Mock(RedisTemplate)
    def valueOperations = Mock(ValueOperations)
    def objectMapper = Mock(ObjectMapper)
    def encryptionService = Mock(EncryptionService)
    def userProfileRepository = Mock(UserProfileRepository)
    def studentRepository = Mock(StudentRepository)
    def professorRepository = Mock(ProfessorRepository)

    @Subject
    def userInfoCacheService = new UserInfoCacheServiceImpl(
            redisTemplate,
            objectMapper,
            encryptionService,
            userProfileRepository,
            studentRepository,
            professorRepository
    )

    def setup() {
        redisTemplate.opsForValue() >> valueOperations
    }

    def "null 또는 빈 userIds로 호출하면 빈 Map을 반환한다"() {
        expect: "빈 Map이 반환된다"
        userInfoCacheService.getUserInfoMap(userIds) == [:]

        where:
        userIds << [null, [] as Set]
    }

    def "캐시에 있는 유저 정보를 조회한다"() {
        given: "캐시에 저장된 유저 정보"
        def userIds = [1L] as Set
        def cachedDto = UserBasicInfoDto.builder()
                .id(1L)
                .name("홍길동")
                .userType("STUDENT")
                .build()
        def cachedJson = '{"id":1,"name":"홍길동","userType":"STUDENT"}'

        valueOperations.get("user:info:1") >> cachedJson
        objectMapper.readValue(cachedJson, UserBasicInfoDto) >> cachedDto

        when: "유저 정보를 조회하면"
        def result = userInfoCacheService.getUserInfoMap(userIds)

        then: "캐시에서 조회된 정보가 반환된다"
        result.size() == 1
        result[1L].name == "홍길동"
        result[1L].userType == "STUDENT"
    }

    def "캐시 미스 시 DB에서 조회하고 캐시에 저장한다"() {
        given: "캐시에 없는 유저 정보"
        def userIds = [1L] as Set
        def student = Mock(Student) {
            getStudentId() >> 1L
        }

        valueOperations.get("user:info:1") >> null

        // DB 조회 결과 - Object[] 배열로 명시적 변환
        userProfileRepository.findNamesByUserIds([1L]) >> [[1L, "encrypted_name"] as Object[]]
        studentRepository.findAllById([1L]) >> [student]
        professorRepository.findAllById([1L]) >> []
        encryptionService.decryptName("encrypted_name") >> "홍길동"
        objectMapper.writeValueAsString(_) >> '{"id":1,"name":"홍길동","userType":"STUDENT"}'

        when: "유저 정보를 조회하면"
        def result = userInfoCacheService.getUserInfoMap(userIds)

        then: "DB에서 조회된 정보가 반환된다"
        result.size() == 1
        result[1L].name == "홍길동"

        and: "캐시에 저장된다"
        1 * valueOperations.set("user:info:1", _, 1, TimeUnit.HOURS)
    }

    def "교수 유저 정보를 조회한다"() {
        given: "교수 유저"
        def userIds = [2L] as Set
        def professor = Mock(Professor) {
            getProfessorId() >> 2L
        }

        valueOperations.get("user:info:2") >> null
        userProfileRepository.findNamesByUserIds([2L]) >> [[2L, "encrypted_professor_name"] as Object[]]
        studentRepository.findAllById([2L]) >> []
        professorRepository.findAllById([2L]) >> [professor]
        encryptionService.decryptName("encrypted_professor_name") >> "김교수"
        objectMapper.writeValueAsString(_) >> '{"id":2,"name":"김교수","userType":"PROFESSOR"}'

        when: "유저 정보를 조회하면"
        def result = userInfoCacheService.getUserInfoMap(userIds)

        then: "교수 정보가 반환된다"
        result.size() == 1
        result[2L].name == "김교수"
        result[2L].userType == "PROFESSOR"
    }

    def "학생도 교수도 아닌 유저는 UNKNOWN 타입으로 반환된다"() {
        given: "학생도 교수도 아닌 유저"
        def userIds = [3L] as Set

        valueOperations.get("user:info:3") >> null
        userProfileRepository.findNamesByUserIds([3L]) >> [[3L, "encrypted_admin_name"] as Object[]]
        studentRepository.findAllById([3L]) >> []
        professorRepository.findAllById([3L]) >> []
        encryptionService.decryptName("encrypted_admin_name") >> "관리자"
        objectMapper.writeValueAsString(_) >> '{"id":3,"name":"관리자","userType":"UNKNOWN"}'

        when: "유저 정보를 조회하면"
        def result = userInfoCacheService.getUserInfoMap(userIds)

        then: "UNKNOWN 타입으로 반환된다"
        result[3L].userType == "UNKNOWN"
    }

    def "일부는 캐시에서, 일부는 DB에서 조회한다"() {
        given: "캐시 히트와 미스가 섞인 경우"
        def userIds = [1L, 2L] as Set
        def cachedDto = UserBasicInfoDto.builder()
                .id(1L)
                .name("캐시유저")
                .userType("STUDENT")
                .build()
        def cachedJson = '{"id":1,"name":"캐시유저","userType":"STUDENT"}'
        def student = Mock(Student) {
            getStudentId() >> 2L
        }

        // 1번은 캐시 히트
        valueOperations.get("user:info:1") >> cachedJson
        objectMapper.readValue(cachedJson, UserBasicInfoDto) >> cachedDto

        // 2번은 캐시 미스
        valueOperations.get("user:info:2") >> null
        userProfileRepository.findNamesByUserIds([2L]) >> [[2L, "encrypted_name"] as Object[]]
        studentRepository.findAllById([2L]) >> [student]
        professorRepository.findAllById([2L]) >> []
        encryptionService.decryptName("encrypted_name") >> "DB유저"
        objectMapper.writeValueAsString(_) >> '{"id":2,"name":"DB유저","userType":"STUDENT"}'

        when: "유저 정보를 조회하면"
        def result = userInfoCacheService.getUserInfoMap(userIds)

        then: "두 유저 정보가 모두 반환된다"
        result.size() == 2
        result[1L].name == "캐시유저"
        result[2L].name == "DB유저"
    }

    def "캐시 역직렬화 실패 시 DB에서 다시 조회한다"() {
        given: "캐시 역직렬화 실패"
        def userIds = [1L] as Set
        def student = Mock(Student) {
            getStudentId() >> 1L
        }

        valueOperations.get("user:info:1") >> "invalid json"
        objectMapper.readValue("invalid json", UserBasicInfoDto) >> { throw new JsonProcessingException("파싱 실패") {} }

        userProfileRepository.findNamesByUserIds([1L]) >> [[1L, "encrypted_name"] as Object[]]
        studentRepository.findAllById([1L]) >> [student]
        professorRepository.findAllById([1L]) >> []
        encryptionService.decryptName("encrypted_name") >> "홍길동"
        objectMapper.writeValueAsString(_) >> '{"id":1,"name":"홍길동","userType":"STUDENT"}'

        when: "유저 정보를 조회하면"
        def result = userInfoCacheService.getUserInfoMap(userIds)

        then: "DB에서 조회된 정보가 반환된다"
        result[1L].name == "홍길동"
    }

    def "복호화 실패 시 평문으로 사용한다"() {
        given: "복호화 실패하는 이름"
        def userIds = [1L] as Set
        def student = Mock(Student) {
            getStudentId() >> 1L
        }

        valueOperations.get("user:info:1") >> null
        userProfileRepository.findNamesByUserIds([1L]) >> [[1L, "plain_name"] as Object[]]
        studentRepository.findAllById([1L]) >> [student]
        professorRepository.findAllById([1L]) >> []
        encryptionService.decryptName("plain_name") >> { throw new RuntimeException("복호화 실패") }
        objectMapper.writeValueAsString(_) >> '{"id":1,"name":"plain_name","userType":"STUDENT"}'

        when: "유저 정보를 조회하면"
        def result = userInfoCacheService.getUserInfoMap(userIds)

        then: "평문 이름이 사용된다"
        result[1L].name == "plain_name"
    }

    def "이름이 없는 유저도 조회할 수 있다"() {
        given: "프로필에 이름이 없는 유저"
        def userIds = [1L] as Set
        def student = Mock(Student) {
            getStudentId() >> 1L
        }

        valueOperations.get("user:info:1") >> null
        userProfileRepository.findNamesByUserIds([1L]) >> []  // 이름 없음
        studentRepository.findAllById([1L]) >> [student]
        professorRepository.findAllById([1L]) >> []
        objectMapper.writeValueAsString(_) >> '{"id":1,"name":null,"userType":"STUDENT"}'

        when: "유저 정보를 조회하면"
        def result = userInfoCacheService.getUserInfoMap(userIds)

        then: "이름이 null인 정보가 반환된다"
        result[1L].name == null
        result[1L].userType == "STUDENT"
    }

    def "캐시 직렬화 실패해도 결과는 반환된다"() {
        given: "캐시 직렬화 실패"
        def userIds = [1L] as Set
        def student = Mock(Student) {
            getStudentId() >> 1L
        }

        valueOperations.get("user:info:1") >> null
        userProfileRepository.findNamesByUserIds([1L]) >> [[1L, "encrypted_name"] as Object[]]
        studentRepository.findAllById([1L]) >> [student]
        professorRepository.findAllById([1L]) >> []
        encryptionService.decryptName("encrypted_name") >> "홍길동"
        objectMapper.writeValueAsString(_) >> { throw new JsonProcessingException("직렬화 실패") {} }

        when: "유저 정보를 조회하면"
        def result = userInfoCacheService.getUserInfoMap(userIds)

        then: "결과는 정상적으로 반환된다"
        result[1L].name == "홍길동"
    }
}
