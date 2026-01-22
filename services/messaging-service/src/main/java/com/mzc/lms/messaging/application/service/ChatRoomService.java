package com.mzc.lms.messaging.application.service;

import com.mzc.lms.messaging.application.port.in.ChatRoomUseCase;
import com.mzc.lms.messaging.application.port.out.ChatRoomRepository;
import com.mzc.lms.messaging.domain.model.ChatRoom;
import com.mzc.lms.messaging.domain.model.RoomType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class ChatRoomService implements ChatRoomUseCase {

    private static final Logger log = LoggerFactory.getLogger(ChatRoomService.class);

    private final ChatRoomRepository chatRoomRepository;

    public ChatRoomService(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    @Override
    @Transactional
    public ChatRoom createDirectRoom(Long user1Id, Long user2Id) {
        // Check if direct room already exists
        Optional<ChatRoom> existingRoom = chatRoomRepository.findDirectRoom(user1Id, user2Id);
        if (existingRoom.isPresent()) {
            log.info("Direct room already exists between {} and {}", user1Id, user2Id);
            return existingRoom.get();
        }

        ChatRoom room = ChatRoom.createDirect(user1Id, user2Id);
        ChatRoom savedRoom = chatRoomRepository.save(room);

        log.info("Created direct room: {} between {} and {}", savedRoom.getId(), user1Id, user2Id);
        return savedRoom;
    }

    @Override
    @Transactional
    public ChatRoom createGroupRoom(String name, Long creatorId, Set<Long> participantIds) {
        ChatRoom room = ChatRoom.createGroup(name, creatorId, participantIds);
        ChatRoom savedRoom = chatRoomRepository.save(room);

        log.info("Created group room: {} by user {}", savedRoom.getId(), creatorId);
        return savedRoom;
    }

    @Override
    @Transactional
    public ChatRoom createCourseRoom(String name, Long courseId, Long creatorId) {
        ChatRoom room = ChatRoom.createCourseRoom(name, courseId, creatorId);
        ChatRoom savedRoom = chatRoomRepository.save(room);

        log.info("Created course room: {} for course {}", savedRoom.getId(), courseId);
        return savedRoom;
    }

    @Override
    public Optional<ChatRoom> getRoom(String roomId) {
        return chatRoomRepository.findById(roomId);
    }

    @Override
    public Optional<ChatRoom> findDirectRoom(Long user1Id, Long user2Id) {
        return chatRoomRepository.findDirectRoom(user1Id, user2Id);
    }

    @Override
    public List<ChatRoom> getUserRooms(Long userId) {
        return chatRoomRepository.findByParticipantId(userId);
    }

    @Override
    public List<ChatRoom> getCourseRooms(Long courseId) {
        return chatRoomRepository.findByCourseId(courseId);
    }

    @Override
    @Transactional
    public ChatRoom addParticipant(String roomId, Long userId) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found: " + roomId));

        if (room.getRoomType() == RoomType.DIRECT) {
            throw new IllegalStateException("Cannot add participant to direct room");
        }

        ChatRoom updatedRoom = room.addParticipant(userId);
        ChatRoom savedRoom = chatRoomRepository.save(updatedRoom);

        log.info("Added user {} to room {}", userId, roomId);
        return savedRoom;
    }

    @Override
    @Transactional
    public ChatRoom removeParticipant(String roomId, Long userId) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found: " + roomId));

        ChatRoom updatedRoom = room.removeParticipant(userId);
        ChatRoom savedRoom = chatRoomRepository.save(updatedRoom);

        log.info("Removed user {} from room {}", userId, roomId);
        return savedRoom;
    }

    @Override
    @Transactional
    public void leaveRoom(String roomId, Long userId) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found: " + roomId));

        if (room.getRoomType() == RoomType.DIRECT) {
            // For direct rooms, we just mark it as inactive or handle differently
            log.info("User {} left direct room {}", userId, roomId);
            return;
        }

        ChatRoom updatedRoom = room.removeParticipant(userId);
        chatRoomRepository.save(updatedRoom);

        log.info("User {} left room {}", userId, roomId);
    }
}
