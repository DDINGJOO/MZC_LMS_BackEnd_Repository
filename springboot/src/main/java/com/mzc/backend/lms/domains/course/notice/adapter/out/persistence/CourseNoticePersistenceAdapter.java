package com.mzc.backend.lms.domains.course.notice.adapter.out.persistence;

import com.mzc.backend.lms.domains.course.notice.application.port.out.CourseNoticeRepositoryPort;
import com.mzc.backend.lms.domains.course.notice.adapter.out.persistence.entity.CourseNotice;
import com.mzc.backend.lms.domains.course.notice.adapter.out.persistence.repository.CourseNoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 강의 공지사항 영속성 Adapter
 */
@Component
@RequiredArgsConstructor
public class CourseNoticePersistenceAdapter implements CourseNoticeRepositoryPort {

    private final CourseNoticeRepository courseNoticeRepository;

    @Override
    public CourseNotice save(CourseNotice notice) {
        return courseNoticeRepository.save(notice);
    }

    @Override
    public Page<CourseNotice> findByCourseId(Long courseId, Pageable pageable) {
        return courseNoticeRepository.findByCourseId(courseId, pageable);
    }

    @Override
    public Optional<CourseNotice> findByIdAndNotDeleted(Long noticeId) {
        return courseNoticeRepository.findByIdAndNotDeleted(noticeId);
    }
}
