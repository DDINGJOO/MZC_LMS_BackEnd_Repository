package com.mzc.backend.lms.domains.user.adapter.out.persistence;

import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.UserPrimaryContact;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.repository.UserPrimaryContactRepository;
import com.mzc.backend.lms.domains.user.application.port.out.UserPrimaryContactCommandPort;
import com.mzc.backend.lms.domains.user.application.port.out.UserPrimaryContactQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * UserPrimaryContact 영속성 어댑터
 */
@Component
@RequiredArgsConstructor
public class UserPrimaryContactPersistenceAdapter implements UserPrimaryContactQueryPort, UserPrimaryContactCommandPort {

    private final UserPrimaryContactRepository userPrimaryContactRepository;

    @Override
    public Optional<UserPrimaryContact> findByUserId(Long userId) {
        return userPrimaryContactRepository.findByUserId(userId);
    }

    @Override
    public List<UserPrimaryContact> findByUserIds(List<Long> userIds) {
        return userPrimaryContactRepository.findByUserIds(userIds);
    }

    @Override
    public boolean existsByMobileNumber(String encryptedMobileNumber) {
        return userPrimaryContactRepository.existsByMobileNumber(encryptedMobileNumber);
    }

    @Override
    public UserPrimaryContact save(UserPrimaryContact contact) {
        return userPrimaryContactRepository.save(contact);
    }
}
