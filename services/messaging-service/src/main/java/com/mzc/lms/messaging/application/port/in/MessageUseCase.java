package com.mzc.lms.messaging.application.port.in;

import com.mzc.lms.messaging.domain.model.Message;
import com.mzc.lms.messaging.domain.model.MessageType;

import java.util.List;
import java.util.Optional;

public interface MessageUseCase {

    Message sendMessage(String roomId, Long senderId, String senderName, String content);

    Message sendReply(String roomId, Long senderId, String senderName, String content, String replyToMessageId);

    Message sendWithAttachment(String roomId, Long senderId, String senderName,
                               String content, String attachmentUrl, String attachmentName,
                               MessageType messageType);

    Optional<Message> getMessage(String messageId);

    List<Message> getRoomMessages(String roomId, int page, int size);

    List<Message> getMessagesBefore(String roomId, String beforeMessageId, int limit);

    void markAsRead(String roomId, Long userId, List<String> messageIds);

    void deleteMessage(String messageId, Long userId);

    int getUnreadCount(String roomId, Long userId);
}
