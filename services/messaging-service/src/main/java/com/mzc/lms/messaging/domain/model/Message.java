package com.mzc.lms.messaging.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Builder
public class Message {

    private String id;
    private String roomId;
    private Long senderId;
    private String senderName;
    private MessageType messageType;
    private String content;
    private String replyToMessageId;
    private String attachmentUrl;
    private String attachmentName;
    private Set<Long> readByUserIds;
    private Boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Message createText(String roomId, Long senderId, String senderName, String content) {
        return Message.builder()
                .roomId(roomId)
                .senderId(senderId)
                .senderName(senderName)
                .messageType(MessageType.TEXT)
                .content(content)
                .readByUserIds(new HashSet<>())
                .isDeleted(false)
                .build();
    }

    public static Message createReply(String roomId, Long senderId, String senderName,
                                      String content, String replyToMessageId) {
        return Message.builder()
                .roomId(roomId)
                .senderId(senderId)
                .senderName(senderName)
                .messageType(MessageType.REPLY)
                .content(content)
                .replyToMessageId(replyToMessageId)
                .readByUserIds(new HashSet<>())
                .isDeleted(false)
                .build();
    }

    public static Message createWithAttachment(String roomId, Long senderId, String senderName,
                                                String content, String attachmentUrl, String attachmentName,
                                                MessageType messageType) {
        return Message.builder()
                .roomId(roomId)
                .senderId(senderId)
                .senderName(senderName)
                .messageType(messageType)
                .content(content)
                .attachmentUrl(attachmentUrl)
                .attachmentName(attachmentName)
                .readByUserIds(new HashSet<>())
                .isDeleted(false)
                .build();
    }

    public static Message createSystem(String roomId, String content) {
        return Message.builder()
                .roomId(roomId)
                .messageType(MessageType.SYSTEM)
                .content(content)
                .readByUserIds(new HashSet<>())
                .isDeleted(false)
                .build();
    }

    public Message markAsRead(Long userId) {
        Set<Long> newReadBy = new HashSet<>(this.readByUserIds != null ? this.readByUserIds : new HashSet<>());
        newReadBy.add(userId);

        return Message.builder()
                .id(this.id)
                .roomId(this.roomId)
                .senderId(this.senderId)
                .senderName(this.senderName)
                .messageType(this.messageType)
                .content(this.content)
                .replyToMessageId(this.replyToMessageId)
                .attachmentUrl(this.attachmentUrl)
                .attachmentName(this.attachmentName)
                .readByUserIds(newReadBy)
                .isDeleted(this.isDeleted)
                .createdAt(this.createdAt)
                .build();
    }

    public Message delete() {
        return Message.builder()
                .id(this.id)
                .roomId(this.roomId)
                .senderId(this.senderId)
                .senderName(this.senderName)
                .messageType(this.messageType)
                .content("삭제된 메시지입니다")
                .replyToMessageId(this.replyToMessageId)
                .attachmentUrl(null)
                .attachmentName(null)
                .readByUserIds(this.readByUserIds)
                .isDeleted(true)
                .createdAt(this.createdAt)
                .build();
    }

    public boolean isReadBy(Long userId) {
        return readByUserIds != null && readByUserIds.contains(userId);
    }

    public String getPreview() {
        if (isDeleted) return "삭제된 메시지";
        if (content == null) return "";
        return content.length() > 50 ? content.substring(0, 50) + "..." : content;
    }
}
