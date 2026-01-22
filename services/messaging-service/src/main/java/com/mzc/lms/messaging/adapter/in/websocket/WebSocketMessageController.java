package com.mzc.lms.messaging.adapter.in.websocket;

import com.mzc.lms.messaging.adapter.in.web.dto.MessageResponse;
import com.mzc.lms.messaging.adapter.in.web.dto.SendMessageRequest;
import com.mzc.lms.messaging.application.port.in.MessageUseCase;
import com.mzc.lms.messaging.domain.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketMessageController {

    private static final Logger log = LoggerFactory.getLogger(WebSocketMessageController.class);

    private final MessageUseCase messageUseCase;
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketMessageController(MessageUseCase messageUseCase,
                                       SimpMessagingTemplate messagingTemplate) {
        this.messageUseCase = messageUseCase;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat/{roomId}/send")
    public void sendMessage(@DestinationVariable String roomId,
                            @Payload SendMessageRequest request) {
        log.debug("WebSocket message received: roomId={}, sender={}", roomId, request.getSenderId());

        try {
            Message message;

            if (request.getReplyToMessageId() != null) {
                message = messageUseCase.sendReply(
                        roomId,
                        request.getSenderId(),
                        request.getSenderName(),
                        request.getContent(),
                        request.getReplyToMessageId()
                );
            } else if (request.getAttachmentUrl() != null) {
                message = messageUseCase.sendWithAttachment(
                        roomId,
                        request.getSenderId(),
                        request.getSenderName(),
                        request.getContent(),
                        request.getAttachmentUrl(),
                        request.getAttachmentName(),
                        request.getMessageType()
                );
            } else {
                message = messageUseCase.sendMessage(
                        roomId,
                        request.getSenderId(),
                        request.getSenderName(),
                        request.getContent()
                );
            }

            // Broadcast to all subscribers of this room
            messagingTemplate.convertAndSend("/topic/room/" + roomId, MessageResponse.from(message));

        } catch (Exception e) {
            log.error("Failed to process WebSocket message: {}", e.getMessage(), e);
        }
    }

    @MessageMapping("/chat/{roomId}/typing")
    public void userTyping(@DestinationVariable String roomId,
                           @Payload TypingNotification notification) {
        // Broadcast typing notification to room (except sender)
        messagingTemplate.convertAndSend("/topic/room/" + roomId + "/typing", notification);
    }

    @MessageMapping("/chat/{roomId}/read")
    public void markRead(@DestinationVariable String roomId,
                         @Payload ReadNotification notification) {
        messageUseCase.markAsRead(roomId, notification.getUserId(), notification.getMessageIds());

        // Broadcast read receipt
        messagingTemplate.convertAndSend("/topic/room/" + roomId + "/read", notification);
    }

    public static class TypingNotification {
        private Long userId;
        private String userName;
        private boolean typing;

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public String getUserName() { return userName; }
        public void setUserName(String userName) { this.userName = userName; }
        public boolean isTyping() { return typing; }
        public void setTyping(boolean typing) { this.typing = typing; }
    }

    public static class ReadNotification {
        private Long userId;
        private java.util.List<String> messageIds;

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public java.util.List<String> getMessageIds() { return messageIds; }
        public void setMessageIds(java.util.List<String> messageIds) { this.messageIds = messageIds; }
    }
}
