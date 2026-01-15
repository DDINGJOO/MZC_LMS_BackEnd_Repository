package com.mzc.backend.lms.domains.user.domain.model;

import lombok.Builder;
import lombok.Getter;

/**
 * 학번 시퀀스 도메인 모델
 * 년도/단과대학/학과별로 마지막 순번을 관리
 */
@Getter
@Builder
public class StudentNumberSequenceDomain {

    private final Long id;
    private final Integer year;
    private final Long collegeId;
    private final Long departmentId;
    private Integer lastSequence;
    private final Long version;

    /**
     * 시퀀스 생성
     */
    public static StudentNumberSequenceDomain create(Integer year, Long collegeId, Long departmentId) {
        return StudentNumberSequenceDomain.builder()
                .year(year)
                .collegeId(collegeId)
                .departmentId(departmentId)
                .lastSequence(0)
                .build();
    }

    /**
     * 초기 시퀀스 값을 지정하여 생성 (기존 데이터 고려)
     */
    public static StudentNumberSequenceDomain createWithInitialSequence(
            Integer year, Long collegeId, Long departmentId, Integer initialSequence) {
        return StudentNumberSequenceDomain.builder()
                .year(year)
                .collegeId(collegeId)
                .departmentId(departmentId)
                .lastSequence(initialSequence)
                .build();
    }

    /**
     * 다음 시퀀스 번호 반환 및 증가
     */
    public Integer getNextSequence() {
        return ++this.lastSequence;
    }
}
