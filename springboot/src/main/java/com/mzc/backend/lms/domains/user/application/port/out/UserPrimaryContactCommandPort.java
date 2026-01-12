package com.mzc.backend.lms.domains.user.application.port.out;

import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.UserPrimaryContact;

/**
 * UserPrimaryContact 명령 Port
 * User 도메인에서 UserPrimaryContact 엔티티 저장/수정을 위한 인터페이스
 */
public interface UserPrimaryContactCommandPort {

    /**
     * 연락처 저장
     */
    UserPrimaryContact save(UserPrimaryContact contact);
}
