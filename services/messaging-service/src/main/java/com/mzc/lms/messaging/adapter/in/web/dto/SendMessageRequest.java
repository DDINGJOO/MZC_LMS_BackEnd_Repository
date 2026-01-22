package com.mzc.lms.messaging.adapter.in.web.dto;

import com.mzc.lms.messaging.domain.model.MessageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SendMessageRequest {

    @NotNull(message = "발신자 ID는 필수입니다")
    private Long senderId;

    @NotBlank(message = "발신자 이름은 필수입니다")
    private String senderName;

    @NotBlank(message = "메시지 내용은 필수입니다")
    private String content;

    private MessageType messageType = MessageType.TEXT;

    private String replyToMessageId;

    private String attachmentUrl;

    private String attachmentName;

    public SendMessageRequest() {}

    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }

    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public MessageType getMessageType() { return messageType; }
    public void setMessageType(MessageType messageType) { this.messageType = messageType; }

    public String getReplyToMessageId() { return replyToMessageId; }
    public void setReplyToMessageId(String replyToMessageId) { this.replyToMessageId = replyToMessageId; }

    public String getAttachmentUrl() { return attachmentUrl; }
    public void setAttachmentUrl(String attachmentUrl) { this.attachmentUrl = attachmentUrl; }

    public String getAttachmentName() { return attachmentName; }
    public void setAttachmentName(String attachmentName) { this.attachmentName = attachmentName; }
}
