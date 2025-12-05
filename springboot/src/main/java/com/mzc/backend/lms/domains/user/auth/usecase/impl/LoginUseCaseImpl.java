package com.mzc.backend.lms.domains.user.auth.usecase.impl;

import com.mzc.backend.lms.domains.user.auth.dto.LoginRequestDto;
import com.mzc.backend.lms.domains.user.auth.dto.LoginResponseDto;
import com.mzc.backend.lms.domains.user.auth.encryption.service.EncryptionService;
import com.mzc.backend.lms.domains.user.auth.jwt.service.JwtTokenService;
import com.mzc.backend.lms.domains.user.auth.token.entity.RefreshToken;
import com.mzc.backend.lms.domains.user.auth.token.repository.RefreshTokenRepository;
import com.mzc.backend.lms.domains.user.auth.usecase.LoginUseCase;
import com.mzc.backend.lms.domains.user.professor.entity.Professor;
import com.mzc.backend.lms.domains.user.professor.repository.ProfessorRepository;
import com.mzc.backend.lms.domains.user.profile.entity.UserProfile;
import com.mzc.backend.lms.domains.user.profile.repository.UserProfileRepository;
import com.mzc.backend.lms.domains.user.student.entity.Student;
import com.mzc.backend.lms.domains.user.student.repository.StudentRepository;
import com.mzc.backend.lms.domains.user.user.entity.User;
import com.mzc.backend.lms.domains.user.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 로그인 유스케이스 구현체
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LoginUseCaseImpl implements LoginUseCase {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final ProfessorRepository professorRepository;
    private final UserProfileRepository userProfileRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final EncryptionService encryptionService;
    private final JwtTokenService jwtTokenService;

    @Override
    @Transactional
    public LoginResponseDto execute(LoginRequestDto dto, String ipAddress) {
        User user = findUserByUsername(dto.getUsername());

        if (user == null) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        if (!encryptionService.matchesPassword(dto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        UserInfo userInfo = getUserInfo(user, dto.getUsername());

        String accessToken = jwtTokenService.generateAccessToken(
            user, userInfo.userType, userInfo.userNumber
        );
        String refreshToken = jwtTokenService.generateRefreshToken(user);

        saveRefreshToken(user, refreshToken, dto, ipAddress);

        String decryptedEmail = encryptionService.decryptEmail(user.getEmail());

        log.info("로그인 성공: userId={}, userType={}", user.getId(), userInfo.userType);

        return LoginResponseDto.of(
            accessToken,
            refreshToken,
            userInfo.userType,
            userInfo.userNumber,
            userInfo.name,
            decryptedEmail,
            user.getId()
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

        // 이메일 형식인 경우
        if (username.contains("@")) {
            String encryptedEmail = encryptionService.encryptEmail(username);
            return userRepository.findByEmail(encryptedEmail).orElse(null);
        }

        // 학번으로 조회
        Optional<Student> student = studentRepository.findById(username);
        if (student.isPresent()) {
            return student.get().getUser();
        }

        // 교번으로 조회 (교번은 1로 시작)
        Optional<Professor> professor = professorRepository.findById(username);
        if (professor.isPresent()) {
            return professor.get().getUser();
        }

        return null;
    }

    private UserInfo getUserInfo(User user, String username) {
        String userType = "USER";
        String userNumber = null;
        String name = null;

        // username이 이메일이 아닌 경우 학번/교번으로 처리
        if (!username.contains("@")) {
            // 학번으로 조회 시도
            Optional<Student> student = studentRepository.findById(username);
            if (student.isPresent()) {
                userType = "STUDENT";
                userNumber = username;  // username이 곧 학번
            } else {
                // 교번으로 조회 시도
                Optional<Professor> professor = professorRepository.findById(username);
                if (professor.isPresent()) {
                    userType = "PROFESSOR";
                    userNumber = username;  // username이 곧 교번
                }
            }
        }

        // 프로필에서 이름 가져오기
        Optional<UserProfile> profile = userProfileRepository.findByUserId(user.getId());
        if (profile.isPresent()) {
            // 이름 복호화
            name = encryptionService.decryptName(profile.get().getName());
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
        refreshTokenRepository.save(refreshTokenEntity);
    }

    private static class UserInfo {
        final String userType;
        final String userNumber;
        final String name;

        UserInfo(String userType, String userNumber, String name) {
            this.userType = userType;
            this.userNumber = userNumber;
            this.name = name;
        }
    }
}
