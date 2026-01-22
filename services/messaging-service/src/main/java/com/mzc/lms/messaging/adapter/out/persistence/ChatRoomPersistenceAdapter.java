package com.mzc.lms.messaging.adapter.out.persistence;

import com.mzc.lms.messaging.adapter.out.persistence.entity.ChatRoomEntity;
import com.mzc.lms.messaging.adapter.out.persistence.repository.ChatRoomJpaRepository;
import com.mzc.lms.messaging.application.port.out.ChatRoomRepository;
import com.mzc.lms.messaging.domain.model.ChatRoom;
import com.mzc.lms.messaging.domain.model.RoomType;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Component
public class ChatRoomPersistenceAdapter implements ChatRoomRepository {

    private final ChatRoomJpaRepository chatRoomJpaRepository;

    public ChatRoomPersistenceAdapter(ChatRoomJpaRepository chatRoomJpaRepository) {
        this.chatRoomJpaRepository = chatRoomJpaRepository;
    }

    @Override
    public ChatRoom save(ChatRoom chatRoom) {
        ChatRoomEntity entity = toEntity(chatRoom);
        ChatRoomEntity saved = chatRoomJpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<ChatRoom> findById(String id) {
        return chatRoomJpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<ChatRoom> findDirectRoom(Long user1Id, Long user2Id) {
        return chatRoomJpaRepository.findDirectRoom(user1Id, user2Id).map(this::toDomain);
    }

    @Override
    public List<ChatRoom> findByParticipantId(Long userId) {
        return chatRoomJpaRepository.findByParticipantId(userId).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<ChatRoom> findByCourseId(Long courseId) {
        return chatRoomJpaRepository.findByCourseIdOrderByCreatedAtDesc(courseId).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<ChatRoom> findByRoomTypeAndParticipantId(RoomType roomType, Long userId) {
        return chatRoomJpaRepository.findByRoomTypeAndParticipantId(roomType, userId).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public void deleteById(String id) {
        chatRoomJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(String id) {
        return chatRoomJpaRepository.existsById(id);
    }

    private ChatRoomEntity toEntity(ChatRoom domain) {
        ChatRoomEntity entity = new ChatRoomEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setRoomType(domain.getRoomType());
        entity.setCourseId(domain.getCourseId());
        entity.setCreatorId(domain.getCreatorId());
        entity.setParticipantIds(domain.getParticipantIds() != null ?
                new HashSet<>(domain.getParticipantIds()) : new HashSet<>());
        entity.setLastMessagePreview(domain.getLastMessagePreview());
        entity.setLastMessageAt(domain.getLastMessageAt());
        entity.setIsActive(domain.getIsActive());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        return entity;
    }

    private ChatRoom toDomain(ChatRoomEntity entity) {
        return ChatRoom.builder()
                .id(entity.getId())
                .name(entity.getName())
                .roomType(entity.getRoomType())
                .courseId(entity.getCourseId())
                .creatorId(entity.getCreatorId())
                .participantIds(entity.getParticipantIds() != null ?
                        new HashSet<>(entity.getParticipantIds()) : new HashSet<>())
                .lastMessagePreview(entity.getLastMessagePreview())
                .lastMessageAt(entity.getLastMessageAt())
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
