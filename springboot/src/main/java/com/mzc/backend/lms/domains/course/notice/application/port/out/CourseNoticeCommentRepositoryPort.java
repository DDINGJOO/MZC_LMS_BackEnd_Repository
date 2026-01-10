package com.mzc.backend.lms.domains.course.notice.application.port.out;

import com.mzc.backend.lms.domains.course.notice.adapter.out.persistence.entity.CourseNoticeComment;

import java.util.List;
import java.util.Optional;

/**
 * 강의 공지사항 댓글 영속성 Port
 */
public interface CourseNoticeCommentRepositoryPort {

    /**
     * 댓글 저장
     */
    CourseNoticeComment save(CourseNoticeComment comment);

    /**
     * ID로 삭제되지 않은 댓글 조회
     */
    Optional<CourseNoticeComment> findByIdAndNotDeleted(Long commentId);

    /**
     * 공지사항 ID로 댓글 목록 조회 (자식 포함)
     */
    List<CourseNoticeComment> findAllByNoticeIdWithChildren(Long noticeId);
}
