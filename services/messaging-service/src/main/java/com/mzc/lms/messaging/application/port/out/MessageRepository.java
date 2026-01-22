package com.mzc.lms.messaging.application.port.out;

import com.mzc.lms.messaging.domain.model.Message;

import java.util.List;
import java.util.Optional;

public interface MessageRepository {

    Message save(Message message);

    Optional<Message> findById(String id);

    List<Message> findByRoomId(String roomId, int page, int size);

    List<Message> findByRoomIdBeforeMessageId(String roomId, String beforeMessageId, int limit);

    List<Message> findUnreadByRoomIdAndUserId(String roomId, Long userId);

    int countUnreadByRoomIdAndUserId(String roomId, Long userId);

    void deleteById(String id);

    void deleteByRoomId(String roomId);
}
