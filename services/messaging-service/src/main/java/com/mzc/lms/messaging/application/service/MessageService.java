package com.mzc.lms.messaging.application.service;

import com.mzc.lms.messaging.application.port.in.MessageUseCase;
import com.mzc.lms.messaging.application.port.out.ChatRoomRepository;
import com.mzc.lms.messaging.application.port.out.MessageEventPublisher;
import com.mzc.lms.messaging.application.port.out.MessageRepository;
import com.mzc.lms.messaging.domain.event.MessageEvent;
import com.mzc.lms.messaging.domain.model.ChatRoom;
import com.mzc.lms.messaging.domain.model.Message;
import com.mzc.lms.messaging.domain.model.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class MessageService implements MessageUseCase {

    private static final Logger log = LoggerFactory.getLogger(MessageService.class);

    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MessageEventPublisher messageEventPublisher;

    public MessageService(MessageRepository messageRepository,
                          ChatRoomRepository chatRoomRepository,
                          MessageEventPublisher messageEventPublisher) {
        this.messageRepository = messageRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.messageEventPublisher = messageEventPublisher;
    }

    @Override
    @Transactional
    public Message sendMessage(String roomId, Long senderId, String senderName, String content) {
        ChatRoom room = validateRoomAndParticipant(roomId, senderId);

        Message message = Message.createText(roomId, senderId, senderName, content);
        Message savedMessage = messageRepository.save(message);

        // Update room's last message
        ChatRoom updatedRoom = room.updateLastMessage(savedMessage.getPreview(), LocalDateTime.now());
        chatRoomRepository.save(updatedRoom);

        // Publish event
        Set<Long> recipientIds = new HashSet<>(room.getParticipantIds());
        recipientIds.remove(senderId);

        messageEventPublisher.publish(MessageEvent.sent(
                savedMessage.getId(),
                roomId,
                senderId,
                senderName,
                MessageType.TEXT,
                content,
                recipientIds
        ));

        log.info("Message sent: {} in room {} by user {}", savedMessage.getId(), roomId, senderId);
        return savedMessage;
    }

    @Override
    @Transactional
    public Message sendReply(String roomId, Long senderId, String senderName, String content, String replyToMessageId) {
        ChatRoom room = validateRoomAndParticipant(roomId, senderId);

        // Validate reply-to message exists
        messageRepository.findById(replyToMessageId)
                .orElseThrow(() -> new IllegalArgumentException("Reply-to message not found: " + replyToMessageId));

        Message message = Message.createReply(roomId, senderId, senderName, content, replyToMessageId);
        Message savedMessage = messageRepository.save(message);

        // Update room's last message
        ChatRoom updatedRoom = room.updateLastMessage(savedMessage.getPreview(), LocalDateTime.now());
        chatRoomRepository.save(updatedRoom);

        // Publish event
        Set<Long> recipientIds = new HashSet<>(room.getParticipantIds());
        recipientIds.remove(senderId);

        messageEventPublisher.publish(MessageEvent.sent(
                savedMessage.getId(),
                roomId,
                senderId,
                senderName,
                MessageType.REPLY,
                content,
                recipientIds
        ));

        log.info("Reply sent: {} in room {} by user {}", savedMessage.getId(), roomId, senderId);
        return savedMessage;
    }

    @Override
    @Transactional
    public Message sendWithAttachment(String roomId, Long senderId, String senderName,
                                       String content, String attachmentUrl, String attachmentName,
                                       MessageType messageType) {
        ChatRoom room = validateRoomAndParticipant(roomId, senderId);

        Message message = Message.createWithAttachment(
                roomId, senderId, senderName, content, attachmentUrl, attachmentName, messageType
        );
        Message savedMessage = messageRepository.save(message);

        // Update room's last message
        String preview = messageType == MessageType.IMAGE ? "[이미지]" :
                         messageType == MessageType.FILE ? "[파일] " + attachmentName : savedMessage.getPreview();
        ChatRoom updatedRoom = room.updateLastMessage(preview, LocalDateTime.now());
        chatRoomRepository.save(updatedRoom);

        // Publish event
        Set<Long> recipientIds = new HashSet<>(room.getParticipantIds());
        recipientIds.remove(senderId);

        messageEventPublisher.publish(MessageEvent.sent(
                savedMessage.getId(),
                roomId,
                senderId,
                senderName,
                messageType,
                content,
                recipientIds
        ));

        log.info("Message with attachment sent: {} in room {} by user {}", savedMessage.getId(), roomId, senderId);
        return savedMessage;
    }

    @Override
    public Optional<Message> getMessage(String messageId) {
        return messageRepository.findById(messageId);
    }

    @Override
    public List<Message> getRoomMessages(String roomId, int page, int size) {
        return messageRepository.findByRoomId(roomId, page, size);
    }

    @Override
    public List<Message> getMessagesBefore(String roomId, String beforeMessageId, int limit) {
        return messageRepository.findByRoomIdBeforeMessageId(roomId, beforeMessageId, limit);
    }

    @Override
    @Transactional
    public void markAsRead(String roomId, Long userId, List<String> messageIds) {
        for (String messageId : messageIds) {
            messageRepository.findById(messageId).ifPresent(message -> {
                if (!message.isReadBy(userId)) {
                    Message readMessage = message.markAsRead(userId);
                    messageRepository.save(readMessage);

                    messageEventPublisher.publish(MessageEvent.read(messageId, roomId, userId));
                }
            });
        }

        log.debug("Marked {} messages as read by user {} in room {}", messageIds.size(), userId, roomId);
    }

    @Override
    @Transactional
    public void deleteMessage(String messageId, Long userId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Message not found: " + messageId));

        if (!message.getSenderId().equals(userId)) {
            throw new IllegalStateException("Only sender can delete the message");
        }

        Message deletedMessage = message.delete();
        messageRepository.save(deletedMessage);

        messageEventPublisher.publish(MessageEvent.deleted(messageId, message.getRoomId(), userId));

        log.info("Message deleted: {} by user {}", messageId, userId);
    }

    @Override
    public int getUnreadCount(String roomId, Long userId) {
        return messageRepository.countUnreadByRoomIdAndUserId(roomId, userId);
    }

    private ChatRoom validateRoomAndParticipant(String roomId, Long userId) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found: " + roomId));

        if (!room.isParticipant(userId)) {
            throw new IllegalStateException("User is not a participant of this room");
        }

        return room;
    }
}
