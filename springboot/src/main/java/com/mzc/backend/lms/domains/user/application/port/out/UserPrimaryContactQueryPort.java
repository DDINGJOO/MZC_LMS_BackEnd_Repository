package com.mzc.backend.lms.domains.user.application.port.out;

import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.UserPrimaryContact;

import java.util.List;
import java.util.Optional;

/**
 * UserPrimaryContact 조회 Port
 * User 도메인에서 UserPrimaryContact 엔티티 조회를 위한 인터페이스
 */
public interface UserPrimaryContactQueryPort {

    /**
     * 사용자 ID로 주 연락처 조회
     */
    Optional<UserPrimaryContact> findByUserId(Long userId);

    /**
     * 여러 사용자의 주 연락처 일괄 조회
     */
    List<UserPrimaryContact> findByUserIds(List<Long> userIds);

    /**
     * 모바일 번호 존재 여부 확인 (암호화된 값으로 체크)
     */
    boolean existsByMobileNumber(String encryptedMobileNumber);
}
