package com.mzc.backend.lms.domains.message.exception;

import com.mzc.backend.lms.common.exceptions.CommonException;
import lombok.Getter;

/**
 * Message 도메인 예외 클래스
 * <p>
 * 대화방, 메시지 관련 예외를 처리합니다.
 * </p>
 */
@Getter
public class MessageException extends CommonException {

    private final MessageErrorCode messageErrorCode;

    public MessageException(MessageErrorCode errorCode) {
        super(errorCode, errorCode.getStatus(), errorCode.getMessage());
        this.messageErrorCode = errorCode;
    }

    public MessageException(MessageErrorCode errorCode, String detailMessage) {
        super(errorCode, errorCode.getStatus(),
              String.format("%s - %s", errorCode.getMessage(), detailMessage));
        this.messageErrorCode = errorCode;
    }

    public MessageException(MessageErrorCode errorCode, Throwable cause) {
        super(errorCode, errorCode.getStatus(), errorCode.getMessage(), cause);
        this.messageErrorCode = errorCode;
    }

    public MessageException(MessageErrorCode errorCode, String detailMessage, Throwable cause) {
        super(errorCode, errorCode.getStatus(),
              String.format("%s - %s", errorCode.getMessage(), detailMessage), cause);
        this.messageErrorCode = errorCode;
    }

    @Override
    public String getExceptionType() {
        return "MESSAGE_DOMAIN";
    }

    // 팩토리 메서드들 - 대화방 관련

    public static MessageException selfConversationNotAllowed(Long userId) {
        return new MessageException(MessageErrorCode.SELF_CONVERSATION_NOT_ALLOWED,
            String.format("사용자 ID: %d", userId));
    }

    public static MessageException conversationDeleted(Long conversationId) {
        return new MessageException(MessageErrorCode.CONVERSATION_DELETED,
            String.format("대화방 ID: %d", conversationId));
    }

    public static MessageException notParticipant(Long userId, Long conversationId) {
        return new MessageException(MessageErrorCode.NOT_PARTICIPANT,
            String.format("사용자 ID: %d, 대화방 ID: %d", userId, conversationId));
    }

    public static MessageException conversationNotFound(Long conversationId) {
        return new MessageException(MessageErrorCode.CONVERSATION_NOT_FOUND,
            String.format("대화방 ID: %d", conversationId));
    }

    // 팩토리 메서드들 - 메시지 관련

    public static MessageException messageNotFound(Long messageId) {
        return new MessageException(MessageErrorCode.MESSAGE_NOT_FOUND,
            String.format("메시지 ID: %d", messageId));
    }

    public static MessageException messageAlreadyDeleted(Long messageId) {
        return new MessageException(MessageErrorCode.MESSAGE_ALREADY_DELETED,
            String.format("메시지 ID: %d", messageId));
    }

    public static MessageException emptyMessage() {
        return new MessageException(MessageErrorCode.EMPTY_MESSAGE);
    }

    public static MessageException messageTooLong(int length, int maxLength) {
        return new MessageException(MessageErrorCode.MESSAGE_TOO_LONG,
            String.format("입력 길이: %d, 최대 길이: %d", length, maxLength));
    }
}
