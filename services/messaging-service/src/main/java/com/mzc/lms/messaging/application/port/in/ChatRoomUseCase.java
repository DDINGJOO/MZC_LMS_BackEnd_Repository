package com.mzc.lms.messaging.application.port.in;

import com.mzc.lms.messaging.domain.model.ChatRoom;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ChatRoomUseCase {

    ChatRoom createDirectRoom(Long user1Id, Long user2Id);

    ChatRoom createGroupRoom(String name, Long creatorId, Set<Long> participantIds);

    ChatRoom createCourseRoom(String name, Long courseId, Long creatorId);

    Optional<ChatRoom> getRoom(String roomId);

    Optional<ChatRoom> findDirectRoom(Long user1Id, Long user2Id);

    List<ChatRoom> getUserRooms(Long userId);

    List<ChatRoom> getCourseRooms(Long courseId);

    ChatRoom addParticipant(String roomId, Long userId);

    ChatRoom removeParticipant(String roomId, Long userId);

    void leaveRoom(String roomId, Long userId);
}
