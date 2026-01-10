package com.mzc.backend.lms.domains.course.notice.application.port.out;

import com.mzc.backend.lms.domains.course.notice.adapter.out.persistence.entity.CourseNotice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * 강의 공지사항 영속성 Port
 */
public interface CourseNoticeRepositoryPort {

    /**
     * 공지사항 저장
     */
    CourseNotice save(CourseNotice notice);

    /**
     * 강의 ID로 공지사항 페이지 조회
     */
    Page<CourseNotice> findByCourseId(Long courseId, Pageable pageable);

    /**
     * ID로 삭제되지 않은 공지사항 조회
     */
    Optional<CourseNotice> findByIdAndNotDeleted(Long noticeId);
}
