package com.mzc.backend.lms.domains.board.application.port.out;

import com.mzc.backend.lms.domains.board.adapter.out.persistence.entity.Hashtag;

import java.util.List;
import java.util.Optional;

/**
 * Hashtag 영속성을 위한 Port
 */
public interface HashtagRepositoryPort {

    /**
     * 해시태그 저장
     */
    Hashtag save(Hashtag hashtag);

    /**
     * ID로 해시태그 조회
     */
    Optional<Hashtag> findById(Long id);

    /**
     * 이름으로 해시태그 조회
     */
    Optional<Hashtag> findByName(String name);

    /**
     * 이름과 활성 상태로 해시태그 존재 여부 확인
     */
    boolean existsByNameAndIsActive(String name, boolean isActive);

    /**
     * 해시태그 이름으로 검색 (자동완성용)
     */
    List<Hashtag> searchActiveHashtagsByKeyword(String keyword);

    /**
     * 해시태그 이름으로 검색 (name과 displayName 모두 검색, 자동완성용)
     */
    List<Hashtag> searchActiveHashtagsByKeywordInBoth(String keyword);
}
