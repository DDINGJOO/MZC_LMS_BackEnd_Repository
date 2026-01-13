package com.mzc.backend.lms.domains.user.application.service;

import com.mzc.backend.lms.domains.dashboard.student.application.event.LoginSuccessEvent;
import com.mzc.backend.lms.domains.user.adapter.in.web.dto.auth.LoginRequestDto;
import com.mzc.backend.lms.domains.user.adapter.in.web.dto.auth.LoginResponseDto;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.RefreshToken;
import com.mzc.backend.lms.domains.user.application.port.in.LoginUseCase;
import com.mzc.backend.lms.domains.user.application.port.out.*;
import com.mzc.backend.lms.domains.user.exception.AuthException;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.Professor;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.UserProfile;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.UserProfileImage;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.Student;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.StudentDepartment;
import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 로그인 서비스
 * LoginUseCase 구현체 (Port 기반)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService implements LoginUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final StudentRepositoryPort studentRepositoryPort;
    private final ProfessorRepositoryPort professorRepositoryPort;
    private final UserProfileRepositoryPort userProfileRepositoryPort;
    private final UserProfileImageRepositoryPort userProfileImageRepositoryPort;
    private final RefreshTokenRepositoryPort refreshTokenRepositoryPort;
    private final StudentDepartmentRepositoryPort studentDepartmentRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final DataEncryptorPort dataEncryptorPort;
    private final JwtProviderPort jwtProviderPort;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public LoginResponseDto execute(LoginRequestDto dto, String ipAddress) {
        User user = findUserByUsername(dto.getUsername());

        if (user == null) {
            throw AuthException.invalidCredentials();
        }

        if (!passwordEncoderPort.matches(dto.getPassword(), user.getPassword())) {
            throw AuthException.invalidCredentials();
        }

        UserInfo userInfo = getUserInfo(user);

        String accessToken = jwtProviderPort.generateAccessToken(
                user, userInfo.userType, userInfo.userNumber
        );
        String refreshToken = jwtProviderPort.generateRefreshToken(user);

        saveRefreshToken(user, refreshToken, dto, ipAddress);

        String decryptedEmail = dataEncryptorPort.decryptEmail(user.getEmail());

        String thumbnailUrl = userProfileImageRepositoryPort.findByUserId(user.getId())
                .map(UserProfileImage::getThumbnailUrl)
                .orElse(null);

        Long departmentId = null;
        String departmentName = null;
        if ("STUDENT".equals(userInfo.userType)) {
            try {
                Optional<StudentDepartment> studentDepartment = studentDepartmentRepositoryPort.findByStudentId(user.getId());
                if (studentDepartment.isPresent()) {
                    departmentId = studentDepartment.get().getDepartment().getId();
                    departmentName = studentDepartment.get().getDepartment().getDepartmentName();
                }
            } catch (Exception e) {
                log.warn("학과 정보 조회 실패: userId={}", user.getId(), e);
            }
        }

        log.info("로그인 성공: userId={}, userType={}", user.getId(), userInfo.userType);

        eventPublisher.publishEvent(new LoginSuccessEvent(user.getId(), userInfo.userType));

        return LoginResponseDto.of(
                accessToken,
                refreshToken,
                userInfo.userType,
                userInfo.userNumber != null ? userInfo.userNumber.toString() : null,
                userInfo.name,
                decryptedEmail,
                user.getId().toString(),
                thumbnailUrl,
                departmentId,
                departmentName
        );
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isAuthenticatable(String username) {
        return findUserByUsername(username) != null;
    }

    private User findUserByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return null;
        }

        if (username.contains("@")) {
            String encryptedEmail = dataEncryptorPort.encryptEmail(username);
            return userRepositoryPort.findByEmail(encryptedEmail).orElse(null);
        }

        try {
            Long userNumber = Long.parseLong(username);

            Optional<Student> student = studentRepositoryPort.findById(userNumber);
            if (student.isPresent()) {
                return student.get().getUser();
            }

            Optional<Professor> professor = professorRepositoryPort.findById(userNumber);
            if (professor.isPresent()) {
                return professor.get().getUser();
            }
        } catch (NumberFormatException e) {
            log.debug("Invalid user number format: {}", username);
        }

        return null;
    }

    private UserInfo getUserInfo(User user) {
        String userType = "USER";
        Long userNumber = null;
        String name = null;

        Long userId = user.getId();

        Optional<Student> student = studentRepositoryPort.findById(userId);
        if (student.isPresent()) {
            userType = "STUDENT";
            userNumber = userId;
        } else {
            Optional<Professor> professor = professorRepositoryPort.findById(userId);
            if (professor.isPresent()) {
                userType = "PROFESSOR";
                userNumber = userId;
            }
        }

        Optional<UserProfile> profile = userProfileRepositoryPort.findByUserId(userId);
        if (profile.isPresent()) {
            name = dataEncryptorPort.decryptName(profile.get().getName());
        }

        return new UserInfo(userType, userNumber, name);
    }

    private void saveRefreshToken(User user, String refreshToken,
                                  LoginRequestDto dto, String ipAddress) {
        RefreshToken refreshTokenEntity = RefreshToken.create(
                user,
                refreshToken,
                dto.getDeviceInfo(),
                ipAddress != null ? ipAddress : dto.getIpAddress(),
                LocalDateTime.now().plusDays(7)
        );
        refreshTokenRepositoryPort.save(refreshTokenEntity);
    }

    private static class UserInfo {
        final String userType;
        final Long userNumber;
        final String name;

        UserInfo(String userType, Long userNumber, String name) {
            this.userType = userType;
            this.userNumber = userNumber;
            this.name = name;
        }
    }
}
