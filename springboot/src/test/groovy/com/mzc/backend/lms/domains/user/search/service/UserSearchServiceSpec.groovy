package com.mzc.backend.lms.domains.user.search.service

import com.mzc.backend.lms.domains.user.organization.entity.College
import com.mzc.backend.lms.domains.user.organization.entity.Department
import com.mzc.backend.lms.domains.user.organization.repository.CollegeRepository
import com.mzc.backend.lms.domains.user.organization.repository.DepartmentRepository
import com.mzc.backend.lms.domains.user.search.dto.UserSearchRequestDto
import com.mzc.backend.lms.domains.user.search.dto.UserSearchResponseDto
import jakarta.persistence.EntityManager
import jakarta.persistence.TypedQuery
import spock.lang.Specification
import spock.lang.Subject

/**
 * UserSearchService 테스트
 * 유저 검색 및 필터 조회 기능 테스트
 */
class UserSearchServiceSpec extends Specification {

    def entityManager = Mock(EntityManager)
    def collegeRepository = Mock(CollegeRepository)
    def departmentRepository = Mock(DepartmentRepository)

    @Subject
    def userSearchService = new UserSearchService(
            entityManager,
            collegeRepository,
            departmentRepository
    )

    def "전체 단과대 목록을 조회한다"() {
        given: "단과대 목록"
        def college1 = Mock(College) {
            getId() >> 1L
            getCollegeName() >> "공과대학"
        }
        def college2 = Mock(College) {
            getId() >> 2L
            getCollegeName() >> "경영대학"
        }
        collegeRepository.findAll() >> [college1, college2]

        when: "전체 단과대 목록을 조회하면"
        def result = userSearchService.getAllColleges()

        then: "모든 단과대가 반환된다"
        result.size() == 2
        result[0].collegeName == "공과대학"
        result[1].collegeName == "경영대학"
    }

    def "단과대별 학과 목록을 조회한다"() {
        given: "특정 단과대의 학과 목록"
        def collegeId = 1L
        def college = Mock(College) {
            getId() >> collegeId
            getCollegeName() >> "공과대학"
        }
        def dept1 = Mock(Department) {
            getId() >> 1L
            getDepartmentName() >> "컴퓨터공학과"
            getCollege() >> college
        }
        def dept2 = Mock(Department) {
            getId() >> 2L
            getDepartmentName() >> "전자공학과"
            getCollege() >> college
        }
        departmentRepository.findByCollegeId(collegeId) >> [dept1, dept2]

        when: "단과대별 학과 목록을 조회하면"
        def result = userSearchService.getDepartmentsByCollegeId(collegeId)

        then: "해당 단과대의 학과들이 반환된다"
        result.size() == 2
        result[0].departmentName == "컴퓨터공학과"
        result[1].departmentName == "전자공학과"
    }

    def "전체 학과 목록을 조회한다"() {
        given: "전체 학과 목록"
        def college = Mock(College) {
            getId() >> 1L
            getCollegeName() >> "공과대학"
        }
        def dept1 = Mock(Department) {
            getId() >> 1L
            getDepartmentName() >> "컴퓨터공학과"
            getCollege() >> college
        }
        def dept2 = Mock(Department) {
            getId() >> 2L
            getDepartmentName() >> "경영학과"
            getCollege() >> college
        }
        departmentRepository.findAll() >> [dept1, dept2]

        when: "전체 학과 목록을 조회하면"
        def result = userSearchService.getAllDepartments()

        then: "모든 학과가 반환된다"
        result.size() == 2
    }

    def "단과대가 없으면 빈 목록을 반환한다"() {
        given: "빈 단과대 목록"
        collegeRepository.findAll() >> []

        when: "전체 단과대 목록을 조회하면"
        def result = userSearchService.getAllColleges()

        then: "빈 목록이 반환된다"
        result.isEmpty()
    }

    def "해당 단과대에 학과가 없으면 빈 목록을 반환한다"() {
        given: "학과가 없는 단과대"
        def collegeId = 999L
        departmentRepository.findByCollegeId(collegeId) >> []

        when: "단과대별 학과 목록을 조회하면"
        def result = userSearchService.getDepartmentsByCollegeId(collegeId)

        then: "빈 목록이 반환된다"
        result.isEmpty()
    }

    def "유저 검색 시 기본 설정이 적용된다"() {
        given: "기본 설정의 검색 요청"
        def request = Mock(UserSearchRequestDto) {
            getSize() >> null
            getSortBy() >> null
            getUserType() >> null
            getCursorId() >> null
            getCursorName() >> null
            getCollegeId() >> null
            getDepartmentId() >> null
            getName() >> null
        }
        def typedQuery = Mock(TypedQuery)
        entityManager.createQuery(_, UserSearchResponseDto.class) >> typedQuery
        typedQuery.setMaxResults(_) >> typedQuery
        typedQuery.getResultList() >> []

        when: "유저를 검색하면"
        def result = userSearchService.searchUsers(request)

        then: "결과가 반환된다"
        result != null
        result.content != null
    }

    def "유저 검색 시 학생 타입만 검색한다"() {
        given: "학생 타입 검색 요청"
        def request = Mock(UserSearchRequestDto) {
            getSize() >> 10
            getSortBy() >> UserSearchRequestDto.SortBy.ID
            getUserType() >> UserSearchRequestDto.UserType.STUDENT
            getCursorId() >> null
            getCursorName() >> null
            getCollegeId() >> null
            getDepartmentId() >> null
            getName() >> null
        }
        def typedQuery = Mock(TypedQuery)
        entityManager.createQuery({ it.contains("FROM Student") }, UserSearchResponseDto.class) >> typedQuery
        typedQuery.setMaxResults(_) >> typedQuery
        typedQuery.getResultList() >> []

        when: "학생을 검색하면"
        def result = userSearchService.searchUsers(request)

        then: "학생 쿼리만 실행된다"
        result != null
    }

    def "유저 검색 시 교수 타입만 검색한다"() {
        given: "교수 타입 검색 요청"
        def request = Mock(UserSearchRequestDto) {
            getSize() >> 10
            getSortBy() >> UserSearchRequestDto.SortBy.ID
            getUserType() >> UserSearchRequestDto.UserType.PROFESSOR
            getCursorId() >> null
            getCursorName() >> null
            getCollegeId() >> null
            getDepartmentId() >> null
            getName() >> null
        }
        def typedQuery = Mock(TypedQuery)
        entityManager.createQuery({ it.contains("FROM Professor") }, UserSearchResponseDto.class) >> typedQuery
        typedQuery.setMaxResults(_) >> typedQuery
        typedQuery.getResultList() >> []

        when: "교수를 검색하면"
        def result = userSearchService.searchUsers(request)

        then: "교수 쿼리만 실행된다"
        result != null
    }
}
