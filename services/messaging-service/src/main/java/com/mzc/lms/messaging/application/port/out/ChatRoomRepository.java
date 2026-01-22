package com.mzc.lms.messaging.application.port.out;

import com.mzc.lms.messaging.domain.model.ChatRoom;
import com.mzc.lms.messaging.domain.model.RoomType;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository {

    ChatRoom save(ChatRoom chatRoom);

    Optional<ChatRoom> findById(String id);

    Optional<ChatRoom> findDirectRoom(Long user1Id, Long user2Id);

    List<ChatRoom> findByParticipantId(Long userId);

    List<ChatRoom> findByCourseId(Long courseId);

    List<ChatRoom> findByRoomTypeAndParticipantId(RoomType roomType, Long userId);

    void deleteById(String id);

    boolean existsById(String id);
}
