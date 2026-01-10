package com.mzc.backend.lms.domains.course.course.application.port.out;

import com.mzc.backend.lms.domains.course.course.adapter.out.persistence.entity.WeekContent;

import java.util.List;
import java.util.Optional;

/**
 * 주차별 콘텐츠 영속성 Port
 */
public interface WeekContentRepositoryPort {

    /**
     * 콘텐츠 저장
     */
    WeekContent save(WeekContent content);

    /**
     * ID로 콘텐츠 조회
     */
    Optional<WeekContent> findById(Long id);

    /**
     * 주차 ID로 콘텐츠 목록 조회 (순서대로)
     */
    List<WeekContent> findByWeekIdOrderByDisplayOrder(Long weekId);

    /**
     * 콘텐츠 삭제
     */
    void delete(WeekContent content);

    /**
     * 주차 ID로 콘텐츠 전체 삭제
     */
    void deleteByWeekId(Long weekId);

    /**
     * 주차 ID와 순서로 존재 여부 확인
     */
    boolean existsByWeekIdAndDisplayOrder(Long weekId, Integer displayOrder);
}
