package com.mzc.lms.board.application.service;

import com.mzc.lms.board.application.port.in.CommentUseCase;
import com.mzc.lms.board.application.port.out.BoardEventPublisher;
import com.mzc.lms.board.application.port.out.CommentRepository;
import com.mzc.lms.board.application.port.out.PostRepository;
import com.mzc.lms.board.domain.event.BoardEvent;
import com.mzc.lms.board.domain.model.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService implements CommentUseCase {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final BoardEventPublisher eventPublisher;

    @Override
    public Comment createComment(Long postId, Long authorId, String authorName,
                                String content, Long parentCommentId, Boolean isAnonymous) {
        postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다: " + postId));

        if (parentCommentId != null) {
            commentRepository.findById(parentCommentId)
                    .orElseThrow(() -> new IllegalArgumentException("부모 댓글을 찾을 수 없습니다: " + parentCommentId));
        }

        Comment comment = Comment.create(postId, authorId, authorName, content, parentCommentId, isAnonymous);
        Comment savedComment = commentRepository.save(comment);

        eventPublisher.publish(BoardEvent.commentCreated(savedComment.getId(), postId, authorId));

        return savedComment;
    }

    @Override
    public Comment updateComment(Long id, String content) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다: " + id));

        if (comment.getIsDeleted()) {
            throw new IllegalStateException("삭제된 댓글은 수정할 수 없습니다");
        }

        Comment updatedComment = comment.update(content);
        return commentRepository.save(updatedComment);
    }

    @Override
    public Comment deleteComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다: " + id));
        Comment deletedComment = comment.delete();
        return commentRepository.save(deletedComment);
    }

    @Override
    public Comment likeComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다: " + id));

        if (comment.getIsDeleted()) {
            throw new IllegalStateException("삭제된 댓글에 좋아요를 할 수 없습니다");
        }

        Comment likedComment = comment.incrementLikeCount();
        return commentRepository.save(likedComment);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Comment> findById(Long id) {
        return commentRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> findByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> findByAuthorId(Long authorId) {
        return commentRepository.findByAuthorId(authorId);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer countByPostId(Long postId) {
        return commentRepository.countByPostId(postId);
    }
}
