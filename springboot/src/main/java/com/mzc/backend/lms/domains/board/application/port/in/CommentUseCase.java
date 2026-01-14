package com.mzc.backend.lms.domains.board.application.port.in;

import com.mzc.backend.lms.domains.board.adapter.in.web.dto.request.CommentCreateRequestDto;
import com.mzc.backend.lms.domains.board.adapter.in.web.dto.request.CommentUpdateRequestDto;
import com.mzc.backend.lms.domains.board.adapter.in.web.dto.response.CommentResponseDto;

import java.util.List;

/**
 * 댓글 UseCase (Inbound Port)
 * Controller에서 이 인터페이스를 호출
 */
public interface CommentUseCase {

    /**
     * 댓글 생성
     */
    CommentResponseDto createComment(CommentCreateRequestDto request);

    /**
     * 게시글의 모든 댓글 조회
     */
    List<CommentResponseDto> getCommentsByPost(Long postId);

    /**
     * 댓글 수정
     */
    CommentResponseDto updateComment(Long commentId, CommentUpdateRequestDto request, Long updatedBy);

    /**
     * 댓글 삭제 (Soft Delete)
     */
    void deleteComment(Long commentId, Long deletedBy);
}
