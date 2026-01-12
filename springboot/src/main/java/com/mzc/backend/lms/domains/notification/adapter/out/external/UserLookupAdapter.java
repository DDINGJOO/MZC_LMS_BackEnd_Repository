package com.mzc.backend.lms.domains.notification.adapter.out.external;

import com.mzc.backend.lms.domains.notification.application.port.out.UserLookupPort;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.User;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 사용자 조회 Adapter (외부 도메인 연동)
 */
@Component
@RequiredArgsConstructor
public class UserLookupAdapter implements UserLookupPort {

    private final UserRepository userRepository;

    @Override
    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }
}
