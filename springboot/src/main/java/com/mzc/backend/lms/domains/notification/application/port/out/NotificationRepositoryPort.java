package com.mzc.backend.lms.domains.notification.application.port.out;

import com.mzc.backend.lms.domains.notification.adapter.out.persistence.entity.Notification;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * 알림 Repository Port
 */
public interface NotificationRepositoryPort {

    /**
     * 알림 저장
     */
    Notification save(Notification notification);

    /**
     * 알림 일괄 저장
     */
    List<Notification> saveAll(List<Notification> notifications);

    /**
     * ID로 알림 조회
     */
    Optional<Notification> findById(Long id);

    /**
     * 커서 기반 알림 목록 조회 (최초)
     */
    List<Notification> findByRecipientIdOrderByIdDesc(Long recipientId, Pageable pageable);

    /**
     * 커서 기반 알림 목록 조회 (다음 페이지)
     */
    List<Notification> findByRecipientIdAndIdLessThanOrderByIdDesc(Long recipientId, Long cursor, Pageable pageable);

    /**
     * 커서 기반 읽지 않은 알림 목록 조회 (최초)
     */
    List<Notification> findUnreadByRecipientIdOrderByIdDesc(Long recipientId, Pageable pageable);

    /**
     * 커서 기반 읽지 않은 알림 목록 조회 (다음 페이지)
     */
    List<Notification> findUnreadByRecipientIdAndIdLessThanOrderByIdDesc(Long recipientId, Long cursor, Pageable pageable);

    /**
     * 읽지 않은 알림 개수 조회
     */
    long countUnreadByRecipientId(Long recipientId);

    /**
     * 모든 알림 읽음 처리
     */
    int markAllAsReadByRecipientId(Long recipientId);

    /**
     * 알림 삭제
     */
    void deleteById(Long id);

    /**
     * 읽은 알림 일괄 삭제
     */
    int deleteReadByRecipientId(Long recipientId);

    /**
     * 모든 알림 삭제
     */
    int deleteAllByRecipientId(Long recipientId);
}
