package com.mzc.backend.lms.domains.course.notice.application.service;

import com.mzc.backend.lms.domains.course.course.adapter.out.persistence.entity.Course;
import com.mzc.backend.lms.domains.course.notice.application.port.in.CourseNoticeUseCase;
import com.mzc.backend.lms.domains.course.notice.application.port.out.*;
import com.mzc.backend.lms.domains.course.notice.adapter.in.web.dto.request.CourseNoticeCommentRequest;
import com.mzc.backend.lms.domains.course.notice.adapter.in.web.dto.request.CourseNoticeCreateRequest;
import com.mzc.backend.lms.domains.course.notice.adapter.in.web.dto.request.CourseNoticeUpdateRequest;
import com.mzc.backend.lms.domains.course.notice.adapter.in.web.dto.response.CourseNoticeCommentResponse;
import com.mzc.backend.lms.domains.course.notice.adapter.in.web.dto.response.CourseNoticeDetailResponse;
import com.mzc.backend.lms.domains.course.notice.adapter.in.web.dto.response.CourseNoticeResponse;
import com.mzc.backend.lms.domains.course.notice.adapter.out.persistence.entity.CourseNotice;
import com.mzc.backend.lms.domains.course.notice.adapter.out.persistence.entity.CourseNoticeComment;
import com.mzc.backend.lms.domains.course.exception.CourseException;
import com.mzc.backend.lms.domains.course.notice.domain.event.CourseNoticeCreatedEvent;
import com.mzc.backend.lms.domains.user.profile.dto.UserBasicInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 강의 공지사항 UseCase 구현체
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseNoticeServiceImpl implements CourseNoticeUseCase {

    // Persistence Ports
    private final CourseNoticeRepositoryPort courseNoticeRepository;
    private final CourseNoticeCommentRepositoryPort courseNoticeCommentRepository;

    // External Ports
    private final NoticeCoursePort coursePort;
    private final NoticeEnrollmentPort enrollmentPort;
    private final NoticeUserInfoPort userInfoPort;
    private final NoticeEventPort eventPort;

    // === 공지 CRUD ===

    @Override
    @Transactional
    public CourseNoticeResponse createNotice(Long courseId, CourseNoticeCreateRequest request, Long professorId) {
        Course course = findCourseById(courseId);
        validateProfessorPermission(course, professorId);

        CourseNotice notice = CourseNotice.create(
                course,
                request.getTitle(),
                request.getContent(),
                request.getAllowComments(),
                professorId
        );

        courseNoticeRepository.save(notice);
        log.info("공지사항 생성: courseId={}, noticeId={}, professorId={}", courseId, notice.getId(), professorId);

        // 수강생들에게 알림 발송을 위한 이벤트 발행
        eventPort.publishNoticeCreatedEvent(new CourseNoticeCreatedEvent(
                notice.getId(),
                courseId,
                course.getSubject().getSubjectName(),
                notice.getTitle(),
                professorId
        ));

        String authorName = getAuthorName(professorId);
        return CourseNoticeResponse.from(notice, authorName);
    }

    @Override
    public Page<CourseNoticeResponse> getNotices(Long courseId, Long userId, Pageable pageable) {
        validateCourseAccess(courseId, userId);

        Page<CourseNotice> notices = courseNoticeRepository.findByCourseId(courseId, pageable);

        Set<Long> authorIds = notices.stream()
                .map(CourseNotice::getCreatedBy)
                .collect(Collectors.toSet());
        Map<Long, UserBasicInfoDto> userInfoMap = userInfoPort.getUserInfoMap(authorIds);

        return notices.map(notice -> {
            String authorName = getAuthorNameFromMap(userInfoMap, notice.getCreatedBy());
            return CourseNoticeResponse.from(notice, authorName);
        });
    }

    @Override
    public CourseNoticeDetailResponse getNotice(Long courseId, Long noticeId, Long userId) {
        validateCourseAccess(courseId, userId);

        CourseNotice notice = findNoticeByIdAndCourseId(noticeId, courseId);
        String authorName = getAuthorName(notice.getCreatedBy());

        List<CourseNoticeCommentResponse> comments = new ArrayList<>();
        if (notice.isCommentsAllowed()) {
            comments = getCommentsWithReplies(noticeId);
        }

        return CourseNoticeDetailResponse.from(notice, authorName, comments);
    }

    @Override
    @Transactional
    public CourseNoticeResponse updateNotice(Long courseId, Long noticeId, CourseNoticeUpdateRequest request, Long professorId) {
        Course course = findCourseById(courseId);
        validateProfessorPermission(course, professorId);

        CourseNotice notice = findNoticeByIdAndCourseId(noticeId, courseId);
        notice.update(request.getTitle(), request.getContent(), request.getAllowComments(), professorId);

        log.info("공지사항 수정: courseId={}, noticeId={}, professorId={}", courseId, noticeId, professorId);

        String authorName = getAuthorName(notice.getCreatedBy());
        return CourseNoticeResponse.from(notice, authorName);
    }

    @Override
    @Transactional
    public void deleteNotice(Long courseId, Long noticeId, Long professorId) {
        Course course = findCourseById(courseId);
        validateProfessorPermission(course, professorId);

        CourseNotice notice = findNoticeByIdAndCourseId(noticeId, courseId);
        notice.delete();

        log.info("공지사항 삭제: courseId={}, noticeId={}, professorId={}", courseId, noticeId, professorId);
    }

    // === 댓글 CRUD ===

    @Override
    @Transactional
    public CourseNoticeCommentResponse createComment(Long courseId, Long noticeId, CourseNoticeCommentRequest request, Long userId) {
        validateCourseAccess(courseId, userId);

        CourseNotice notice = findNoticeByIdAndCourseId(noticeId, courseId);
        validateCommentsAllowed(notice);

        CourseNoticeComment comment = CourseNoticeComment.create(notice, request.getContent(), userId);
        courseNoticeCommentRepository.save(comment);

        log.info("댓글 작성: noticeId={}, commentId={}, userId={}", noticeId, comment.getId(), userId);

        String authorName = getAuthorName(userId);
        return CourseNoticeCommentResponse.from(comment, authorName);
    }

    @Override
    @Transactional
    public CourseNoticeCommentResponse createReply(Long courseId, Long noticeId, Long parentId, CourseNoticeCommentRequest request, Long userId) {
        validateCourseAccess(courseId, userId);

        CourseNotice notice = findNoticeByIdAndCourseId(noticeId, courseId);
        validateCommentsAllowed(notice);

        CourseNoticeComment parent = findCommentByIdAndNoticeId(parentId, noticeId);

        CourseNoticeComment reply = CourseNoticeComment.createReply(notice, parent, request.getContent(), userId);
        courseNoticeCommentRepository.save(reply);

        log.info("대댓글 작성: noticeId={}, parentId={}, replyId={}, userId={}", noticeId, parentId, reply.getId(), userId);

        String authorName = getAuthorName(userId);
        return CourseNoticeCommentResponse.from(reply, authorName);
    }

    @Override
    @Transactional
    public CourseNoticeCommentResponse updateComment(Long courseId, Long noticeId, Long commentId, CourseNoticeCommentRequest request, Long userId) {
        validateCourseAccess(courseId, userId);

        CourseNoticeComment comment = findCommentByIdAndNoticeId(commentId, noticeId);
        validateCommentAuthor(comment, userId);

        comment.update(request.getContent(), userId);

        log.info("댓글 수정: noticeId={}, commentId={}, userId={}", noticeId, commentId, userId);

        String authorName = getAuthorName(userId);
        return CourseNoticeCommentResponse.from(comment, authorName);
    }

    @Override
    @Transactional
    public void deleteComment(Long courseId, Long noticeId, Long commentId, Long userId) {
        validateCourseAccess(courseId, userId);

        CourseNoticeComment comment = findCommentByIdAndNoticeId(commentId, noticeId);
        validateCommentAuthor(comment, userId);

        comment.delete();

        log.info("댓글 삭제: noticeId={}, commentId={}, userId={}", noticeId, commentId, userId);
    }

    // === Private Helper Methods ===

    private Course findCourseById(Long courseId) {
        return coursePort.findById(courseId)
                .orElseThrow(() -> CourseException.courseNotFound(courseId));
    }

    private CourseNotice findNoticeByIdAndCourseId(Long noticeId, Long courseId) {
        CourseNotice notice = courseNoticeRepository.findByIdAndNotDeleted(noticeId)
                .orElseThrow(() -> CourseException.noticeNotFound(noticeId));

        if (!notice.belongsToCourse(courseId)) {
            throw CourseException.noticeNotInCourse(noticeId, courseId);
        }

        return notice;
    }

    private CourseNoticeComment findCommentByIdAndNoticeId(Long commentId, Long noticeId) {
        CourseNoticeComment comment = courseNoticeCommentRepository.findByIdAndNotDeleted(commentId)
                .orElseThrow(() -> CourseException.commentNotFound(commentId));

        if (!comment.belongsToNotice(noticeId)) {
            throw CourseException.commentNotInNotice(commentId, noticeId);
        }

        return comment;
    }

    private void validateProfessorPermission(Course course, Long userId) {
        if (!course.getProfessor().getProfessorId().equals(userId)) {
            throw CourseException.professorOnly();
        }
    }

    private void validateCourseAccess(Long courseId, Long userId) {
        Course course = findCourseById(courseId);

        // 담당 교수인 경우 접근 허용
        if (course.getProfessor().getProfessorId().equals(userId)) {
            return;
        }

        // 수강생인 경우 접근 허용
        boolean isEnrolled = enrollmentPort.existsByStudentIdAndCourseId(userId, courseId);
        if (!isEnrolled) {
            throw CourseException.enrolledOrProfessorOnly();
        }
    }

    private void validateCommentsAllowed(CourseNotice notice) {
        if (!notice.isCommentsAllowed()) {
            throw CourseException.commentNotAllowed(notice.getId());
        }
    }

    private void validateCommentAuthor(CourseNoticeComment comment, Long userId) {
        if (!comment.isAuthor(userId)) {
            throw CourseException.commentOwnerOnly();
        }
    }

    private List<CourseNoticeCommentResponse> getCommentsWithReplies(Long noticeId) {
        List<CourseNoticeComment> rootComments = courseNoticeCommentRepository.findAllByNoticeIdWithChildren(noticeId);

        Set<Long> authorIds = rootComments.stream()
                .flatMap(c -> {
                    List<Long> ids = new ArrayList<>();
                    ids.add(c.getAuthorId());
                    c.getChildren().stream()
                            .filter(child -> !child.isDeleted())
                            .forEach(child -> ids.add(child.getAuthorId()));
                    return ids.stream();
                })
                .collect(Collectors.toSet());

        Map<Long, UserBasicInfoDto> userInfoMap = userInfoPort.getUserInfoMap(authorIds);

        return rootComments.stream()
                .map(comment -> {
                    String authorName = getAuthorNameFromMap(userInfoMap, comment.getAuthorId());
                    List<CourseNoticeCommentResponse> children = comment.getChildren().stream()
                            .filter(child -> !child.isDeleted())
                            .map(child -> {
                                String childAuthorName = getAuthorNameFromMap(userInfoMap, child.getAuthorId());
                                return CourseNoticeCommentResponse.from(child, childAuthorName);
                            })
                            .collect(Collectors.toList());
                    return CourseNoticeCommentResponse.from(comment, authorName, children);
                })
                .collect(Collectors.toList());
    }

    private String getAuthorName(Long userId) {
        Map<Long, UserBasicInfoDto> userInfoMap = userInfoPort.getUserInfoMap(Set.of(userId));
        UserBasicInfoDto userInfo = userInfoMap.get(userId);
        return userInfo != null ? userInfo.getName() : "알 수 없음";
    }

    private String getAuthorNameFromMap(Map<Long, UserBasicInfoDto> userInfoMap, Long userId) {
        UserBasicInfoDto userInfo = userInfoMap.get(userId);
        return userInfo != null ? userInfo.getName() : "알 수 없음";
    }
}
