package com.mzc.backend.lms.domains.message.message.adapter.out.persistence.specification;

import com.mzc.backend.lms.domains.message.message.adapter.out.persistence.entity.Message;
import org.springframework.data.jpa.domain.Specification;

/**
 * Message 엔티티용 Specification 클래스
 */
public final class MessageSpecifications {

    private MessageSpecifications() {
    }

    public static Specification<Message> byConversationId(Long conversationId) {
        return (root, query, cb) ->
                conversationId == null ? null : cb.equal(root.get("conversation").get("id"), conversationId);
    }

    public static Specification<Message> bySenderId(Long senderId) {
        return (root, query, cb) ->
                senderId == null ? null : cb.equal(root.get("sender").get("id"), senderId);
    }

    public static Specification<Message> unread() {
        return (root, query, cb) -> cb.isNull(root.get("readAt"));
    }

    public static Specification<Message> read() {
        return (root, query, cb) -> cb.isNotNull(root.get("readAt"));
    }

    public static Specification<Message> deletedBySender() {
        return (root, query, cb) -> cb.equal(root.get("deletedBySender"), true);
    }

    public static Specification<Message> deletedByReceiver() {
        return (root, query, cb) -> cb.equal(root.get("deletedByReceiver"), true);
    }

    public static Specification<Message> deletedByBoth() {
        return (root, query, cb) -> cb.and(
                cb.equal(root.get("deletedBySender"), true),
                cb.equal(root.get("deletedByReceiver"), true)
        );
    }

    public static Specification<Message> idLessThan(Long cursor) {
        return (root, query, cb) ->
                cursor == null ? null : cb.lessThan(root.get("id"), cursor);
    }
}
