package com.mzc.lms.messaging.domain.event;

import com.mzc.lms.messaging.domain.model.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageEvent {

    private String eventId;
    private String eventType;
    private String messageId;
    private String roomId;
    private Long senderId;
    private String senderName;
    private MessageType messageType;
    private String content;
    private Set<Long> recipientIds;
    private LocalDateTime timestamp;

    public static MessageEvent sent(String messageId, String roomId, Long senderId,
                                    String senderName, MessageType messageType,
                                    String content, Set<Long> recipientIds) {
        return MessageEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType("MESSAGE_SENT")
                .messageId(messageId)
                .roomId(roomId)
                .senderId(senderId)
                .senderName(senderName)
                .messageType(messageType)
                .content(content)
                .recipientIds(recipientIds)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static MessageEvent read(String messageId, String roomId, Long userId) {
        return MessageEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType("MESSAGE_READ")
                .messageId(messageId)
                .roomId(roomId)
                .senderId(userId)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static MessageEvent deleted(String messageId, String roomId, Long deletedBy) {
        return MessageEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType("MESSAGE_DELETED")
                .messageId(messageId)
                .roomId(roomId)
                .senderId(deletedBy)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
