package com.mzc.backend.lms.domains.user.application.port.out;

import com.mzc.backend.lms.domains.user.domain.model.StudentNumberSequenceDomain;

import java.util.Optional;

/**
 * 학번 시퀀스 Port
 * 학번 시퀀스 영속성 작업을 위한 인터페이스
 */
public interface StudentNumberSequencePort {

    /**
     * 년도/단과대학/학과별 시퀀스 조회 (비관적 락)
     */
    Optional<StudentNumberSequenceDomain> findByYearAndCollegeAndDepartmentWithLock(
            Integer year, Long collegeId, Long departmentId);

    /**
     * 시퀀스 저장
     */
    StudentNumberSequenceDomain save(StudentNumberSequenceDomain sequence);

    /**
     * 특정 prefix로 시작하는 학번의 최대 순번 조회
     */
    Optional<Integer> findMaxSequenceByPrefix(String prefix);
}
