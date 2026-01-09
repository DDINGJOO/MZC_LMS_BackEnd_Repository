package com.mzc.backend.lms.domains.message.exception;

import com.mzc.backend.lms.common.exceptions.DomainErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Message 도메인 에러 코드
 * <p>
 * 대화방, 메시지 관련 에러 코드를 정의합니다.
 * </p>
 */
@Getter
public enum MessageErrorCode implements DomainErrorCode {

    // 대화방 관련 (CONVERSATION_0XX)
    SELF_CONVERSATION_NOT_ALLOWED("MESSAGE_CONV_001", "자기 자신과의 대화방은 생성할 수 없습니다", HttpStatus.BAD_REQUEST),
    CONVERSATION_DELETED("MESSAGE_CONV_002", "삭제된 대화방입니다", HttpStatus.BAD_REQUEST),
    NOT_PARTICIPANT("MESSAGE_CONV_003", "대화방에 참여하지 않은 사용자입니다", HttpStatus.FORBIDDEN),
    CONVERSATION_NOT_FOUND("MESSAGE_CONV_004", "대화방을 찾을 수 없습니다", HttpStatus.NOT_FOUND),

    // 메시지 관련 (MESSAGE_0XX)
    MESSAGE_NOT_FOUND("MESSAGE_001", "메시지를 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    MESSAGE_ALREADY_DELETED("MESSAGE_002", "이미 삭제된 메시지입니다", HttpStatus.BAD_REQUEST),
    EMPTY_MESSAGE("MESSAGE_003", "메시지 내용이 비어있습니다", HttpStatus.BAD_REQUEST),
    MESSAGE_TOO_LONG("MESSAGE_004", "메시지가 너무 깁니다", HttpStatus.BAD_REQUEST),
    ;

    private static final String DOMAIN = "MESSAGE";

    private final String code;
    private final String message;
    private final HttpStatus status;

    MessageErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    @Override
    public String getDomain() {
        return DOMAIN;
    }
}
