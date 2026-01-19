package com.mzc.backend.lms.domains.notification.adapter.out.persistence.specification;

import com.mzc.backend.lms.domains.notification.adapter.out.persistence.entity.Notification;
import org.springframework.data.jpa.domain.Specification;

/**
 * Notification 엔티티용 Specification 클래스
 */
public final class NotificationSpecifications {

    private NotificationSpecifications() {
    }

    public static Specification<Notification> byRecipientId(Long recipientId) {
        return (root, query, cb) ->
                recipientId == null ? null : cb.equal(root.get("recipient").get("id"), recipientId);
    }

    public static Specification<Notification> byCourseId(Long courseId) {
        return (root, query, cb) ->
                courseId == null ? null : cb.equal(root.get("courseId"), courseId);
    }

    public static Specification<Notification> byNotificationTypeId(Integer typeId) {
        return (root, query, cb) ->
                typeId == null ? null : cb.equal(root.get("notificationType").get("id"), typeId);
    }

    public static Specification<Notification> isRead(Boolean read) {
        return (root, query, cb) ->
                read == null ? null : cb.equal(root.get("isRead"), read);
    }

    public static Specification<Notification> unread() {
        return (root, query, cb) -> cb.equal(root.get("isRead"), false);
    }

    public static Specification<Notification> read() {
        return (root, query, cb) -> cb.equal(root.get("isRead"), true);
    }

    public static Specification<Notification> byRelatedEntity(String entityType, Long entityId) {
        return (root, query, cb) -> {
            if (entityType == null || entityId == null) {
                return null;
            }
            return cb.and(
                    cb.equal(root.get("relatedEntityType"), entityType),
                    cb.equal(root.get("relatedEntityId"), entityId)
            );
        };
    }

    public static Specification<Notification> idLessThan(Long cursor) {
        return (root, query, cb) ->
                cursor == null ? null : cb.lessThan(root.get("id"), cursor);
    }
}
