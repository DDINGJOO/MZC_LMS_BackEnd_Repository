package com.mzc.lms.messaging.adapter.in.web;

import com.mzc.lms.messaging.adapter.in.web.dto.MessageResponse;
import com.mzc.lms.messaging.adapter.in.web.dto.SendMessageRequest;
import com.mzc.lms.messaging.application.port.in.MessageUseCase;
import com.mzc.lms.messaging.domain.model.Message;
import com.mzc.lms.messaging.domain.model.MessageType;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/messages")
public class MessageController {

    private static final Logger log = LoggerFactory.getLogger(MessageController.class);

    private final MessageUseCase messageUseCase;

    public MessageController(MessageUseCase messageUseCase) {
        this.messageUseCase = messageUseCase;
    }

    @PostMapping("/rooms/{roomId}")
    public ResponseEntity<MessageResponse> sendMessage(@PathVariable String roomId,
                                                       @Valid @RequestBody SendMessageRequest request) {
        log.info("Send message request: roomId={}, sender={}", roomId, request.getSenderId());

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

        return ResponseEntity.status(HttpStatus.CREATED).body(MessageResponse.from(message));
    }

    @GetMapping("/{messageId}")
    public ResponseEntity<MessageResponse> getMessage(@PathVariable String messageId) {
        return messageUseCase.getMessage(messageId)
                .map(message -> ResponseEntity.ok(MessageResponse.from(message)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<List<MessageResponse>> getRoomMessages(
            @PathVariable String roomId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {

        List<Message> messages = messageUseCase.getRoomMessages(roomId, page, size);
        List<MessageResponse> responses = messages.stream()
                .map(MessageResponse::from)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/rooms/{roomId}/before/{beforeMessageId}")
    public ResponseEntity<List<MessageResponse>> getMessagesBefore(
            @PathVariable String roomId,
            @PathVariable String beforeMessageId,
            @RequestParam(defaultValue = "50") int limit) {

        List<Message> messages = messageUseCase.getMessagesBefore(roomId, beforeMessageId, limit);
        List<MessageResponse> responses = messages.stream()
                .map(MessageResponse::from)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @PostMapping("/rooms/{roomId}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable String roomId,
                                           @RequestParam Long userId,
                                           @RequestBody List<String> messageIds) {
        messageUseCase.markAsRead(roomId, userId, messageIds);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable String messageId,
                                               @RequestParam Long userId) {
        messageUseCase.deleteMessage(messageId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/rooms/{roomId}/unread-count")
    public ResponseEntity<Integer> getUnreadCount(@PathVariable String roomId,
                                                  @RequestParam Long userId) {
        int count = messageUseCase.getUnreadCount(roomId, userId);
        return ResponseEntity.ok(count);
    }
}
