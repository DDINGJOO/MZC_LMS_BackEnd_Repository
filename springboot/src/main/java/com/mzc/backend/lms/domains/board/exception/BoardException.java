package com.mzc.backend.lms.domains.board.exception;

import com.mzc.backend.lms.common.exceptions.CommonErrorCode;
import com.mzc.backend.lms.common.exceptions.CommonException;
import lombok.Getter;

/**
 * Board 도메인 예외
 * <p>
 * 게시판, 게시글, 댓글, 첨부파일, 과제 관련 예외를 처리합니다.
 * BoardErrorCode를 통해 세분화된 에러 코드와 HTTP 상태를 제공합니다.
 * </p>
 */
@Getter
public class BoardException extends CommonException {

    private final BoardErrorCode errorCode;

    public BoardException(BoardErrorCode errorCode) {
        super(CommonErrorCode.INTERNAL_SERVER_ERROR, errorCode.getStatus(), errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BoardException(BoardErrorCode errorCode, String message) {
        super(CommonErrorCode.INTERNAL_SERVER_ERROR, errorCode.getStatus(), message);
        this.errorCode = errorCode;
    }

    public BoardException(BoardErrorCode errorCode, Throwable cause) {
        super(CommonErrorCode.INTERNAL_SERVER_ERROR, errorCode.getStatus(), errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }

    @Override
    public String getExceptionType() {
        return "BOARD_DOMAIN";
    }

    /**
     * 게시판 카테고리를 찾을 수 없을 때 발생하는 예외 생성
     */
    public static BoardException categoryNotFound(Long categoryId) {
        return new BoardException(BoardErrorCode.BOARD_CATEGORY_NOT_FOUND,
                String.format("게시판 카테고리를 찾을 수 없습니다 (ID: %d)", categoryId));
    }

    /**
     * 게시글을 찾을 수 없을 때 발생하는 예외 생성
     */
    public static BoardException postNotFound(Long postId) {
        return new BoardException(BoardErrorCode.POST_NOT_FOUND,
                String.format("게시글을 찾을 수 없습니다 (ID: %d)", postId));
    }

    /**
     * 댓글을 찾을 수 없을 때 발생하는 예외 생성
     */
    public static BoardException commentNotFound(Long commentId) {
        return new BoardException(BoardErrorCode.COMMENT_NOT_FOUND,
                String.format("댓글을 찾을 수 없습니다 (ID: %d)", commentId));
    }

    public static BoardException unauthorized(String action) {
        return new BoardException(BoardErrorCode.UNAUTHORIZED_ACTION,
                String.format("해당 작업을 수행할 권한이 없습니다: %s", action));
    }

    public static BoardException emptyFile() {
        return new BoardException(BoardErrorCode.EMPTY_FILE);
    }

    public static BoardException invalidFilename() {
        return new BoardException(BoardErrorCode.INVALID_FILENAME);
    }

    public static BoardException fileSaveFailed(Throwable cause) {
        return new BoardException(BoardErrorCode.FILE_SAVE_FAILED, cause);
    }

    public static BoardException fileNotFound() {
        return new BoardException(BoardErrorCode.FILE_NOT_FOUND);
    }

    public static BoardException attachmentNotFound(Long attachmentId) {
        return new BoardException(BoardErrorCode.ATTACHMENT_NOT_FOUND,
                String.format("첨부파일을 찾을 수 없습니다 (ID: %d)", attachmentId));
    }

    public static BoardException invalidHashtag() {
        return new BoardException(BoardErrorCode.INVALID_HASHTAG);
    }
}
