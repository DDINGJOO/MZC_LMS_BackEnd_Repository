package com.mzc.lms.messaging.adapter.out.persistence;

import com.mzc.lms.messaging.adapter.out.persistence.entity.MessageDocument;
import com.mzc.lms.messaging.adapter.out.persistence.repository.MessageMongoRepository;
import com.mzc.lms.messaging.application.port.out.MessageRepository;
import com.mzc.lms.messaging.domain.model.Message;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Component
public class MessagePersistenceAdapter implements MessageRepository {

    private final MessageMongoRepository messageMongoRepository;

    public MessagePersistenceAdapter(MessageMongoRepository messageMongoRepository) {
        this.messageMongoRepository = messageMongoRepository;
    }

    @Override
    public Message save(Message message) {
        MessageDocument document = toDocument(message);
        MessageDocument saved = messageMongoRepository.save(document);
        return toDomain(saved);
    }

    @Override
    public Optional<Message> findById(String id) {
        return messageMongoRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Message> findByRoomId(String roomId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return messageMongoRepository.findByRoomIdOrderByCreatedAtDesc(roomId, pageRequest).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Message> findByRoomIdBeforeMessageId(String roomId, String beforeMessageId, int limit) {
        PageRequest pageRequest = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        return messageMongoRepository.findByRoomIdAndIdLessThanOrderByCreatedAtDesc(roomId, beforeMessageId, pageRequest)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Message> findUnreadByRoomIdAndUserId(String roomId, Long userId) {
        return messageMongoRepository.findUnreadByRoomIdAndUserId(roomId, userId).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public int countUnreadByRoomIdAndUserId(String roomId, Long userId) {
        return messageMongoRepository.countUnreadByRoomIdAndUserId(roomId, userId);
    }

    @Override
    public void deleteById(String id) {
        messageMongoRepository.deleteById(id);
    }

    @Override
    public void deleteByRoomId(String roomId) {
        messageMongoRepository.deleteByRoomId(roomId);
    }

    private MessageDocument toDocument(Message domain) {
        MessageDocument doc = new MessageDocument();
        doc.setId(domain.getId());
        doc.setRoomId(domain.getRoomId());
        doc.setSenderId(domain.getSenderId());
        doc.setSenderName(domain.getSenderName());
        doc.setMessageType(domain.getMessageType());
        doc.setContent(domain.getContent());
        doc.setReplyToMessageId(domain.getReplyToMessageId());
        doc.setAttachmentUrl(domain.getAttachmentUrl());
        doc.setAttachmentName(domain.getAttachmentName());
        doc.setReadByUserIds(domain.getReadByUserIds() != null ?
                new HashSet<>(domain.getReadByUserIds()) : new HashSet<>());
        doc.setIsDeleted(domain.getIsDeleted());
        doc.setCreatedAt(domain.getCreatedAt());
        doc.setUpdatedAt(domain.getUpdatedAt());
        return doc;
    }

    private Message toDomain(MessageDocument doc) {
        return Message.builder()
                .id(doc.getId())
                .roomId(doc.getRoomId())
                .senderId(doc.getSenderId())
                .senderName(doc.getSenderName())
                .messageType(doc.getMessageType())
                .content(doc.getContent())
                .replyToMessageId(doc.getReplyToMessageId())
                .attachmentUrl(doc.getAttachmentUrl())
                .attachmentName(doc.getAttachmentName())
                .readByUserIds(doc.getReadByUserIds() != null ?
                        new HashSet<>(doc.getReadByUserIds()) : new HashSet<>())
                .isDeleted(doc.getIsDeleted())
                .createdAt(doc.getCreatedAt())
                .updatedAt(doc.getUpdatedAt())
                .build();
    }
}
