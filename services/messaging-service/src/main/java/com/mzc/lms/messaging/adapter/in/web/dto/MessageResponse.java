package com.mzc.lms.messaging.adapter.in.web.dto;

import com.mzc.lms.messaging.domain.model.Message;
import com.mzc.lms.messaging.domain.model.MessageType;

import java.time.LocalDateTime;
import java.util.Set;

public class MessageResponse {

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

    public MessageResponse() {}

    public static MessageResponse from(Message message) {
        MessageResponse response = new MessageResponse();
        response.id = message.getId();
        response.roomId = message.getRoomId();
        response.senderId = message.getSenderId();
        response.senderName = message.getSenderName();
        response.messageType = message.getMessageType();
        response.content = message.getContent();
        response.replyToMessageId = message.getReplyToMessageId();
        response.attachmentUrl = message.getAttachmentUrl();
        response.attachmentName = message.getAttachmentName();
        response.readByUserIds = message.getReadByUserIds();
        response.isDeleted = message.getIsDeleted();
        response.createdAt = message.getCreatedAt();
        return response;
    }

    public String getId() { return id; }
    public String getRoomId() { return roomId; }
    public Long getSenderId() { return senderId; }
    public String getSenderName() { return senderName; }
    public MessageType getMessageType() { return messageType; }
    public String getContent() { return content; }
    public String getReplyToMessageId() { return replyToMessageId; }
    public String getAttachmentUrl() { return attachmentUrl; }
    public String getAttachmentName() { return attachmentName; }
    public Set<Long> getReadByUserIds() { return readByUserIds; }
    public Boolean getIsDeleted() { return isDeleted; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
