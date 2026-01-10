package com.mzc.backend.lms.domains.course.notice.adapter.out.persistence;

import com.mzc.backend.lms.domains.course.notice.application.port.out.CourseNoticeCommentRepositoryPort;
import com.mzc.backend.lms.domains.course.notice.adapter.out.persistence.entity.CourseNoticeComment;
import com.mzc.backend.lms.domains.course.notice.adapter.out.persistence.repository.CourseNoticeCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * 강의 공지사항 댓글 영속성 Adapter
 */
@Component
@RequiredArgsConstructor
public class CourseNoticeCommentPersistenceAdapter implements CourseNoticeCommentRepositoryPort {

    private final CourseNoticeCommentRepository courseNoticeCommentRepository;

    @Override
    public CourseNoticeComment save(CourseNoticeComment comment) {
        return courseNoticeCommentRepository.save(comment);
    }

    @Override
    public Optional<CourseNoticeComment> findByIdAndNotDeleted(Long commentId) {
        return courseNoticeCommentRepository.findByIdAndNotDeleted(commentId);
    }

    @Override
    public List<CourseNoticeComment> findAllByNoticeIdWithChildren(Long noticeId) {
        return courseNoticeCommentRepository.findAllByNoticeIdWithChildren(noticeId);
    }
}
