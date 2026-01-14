package com.mzc.backend.lms.domains.board.application.port.in;

import com.mzc.backend.lms.domains.board.adapter.out.persistence.entity.Hashtag;
import com.mzc.backend.lms.domains.board.adapter.out.persistence.entity.Post;

import java.util.List;

/**
 * 해시태그 UseCase (Inbound Port)
 * Controller에서 이 인터페이스를 호출
 */
public interface HashtagUseCase {

    /**
     * 해시태그 이름으로 조회 또는 생성
     */
    Hashtag getOrCreateHashtag(String tagName, Long userId);

    /**
     * 게시글에 해시태그 목록 연결
     */
    void attachHashtagsToPost(Post post, List<String> tagNames, Long userId);

    /**
     * 게시글의 해시태그 업데이트
     */
    void updatePostHashtags(Post post, List<String> tagNames, Long userId);

    /**
     * 해시태그 검색 (자동완성용)
     */
    List<Hashtag> searchHashtags(String keyword);
}
