package com.mzc.backend.lms.domains.user.profile.controller;

import com.mzc.backend.lms.domains.user.profile.dto.ProfileResponseDto;
import com.mzc.backend.lms.domains.user.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 프로필 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    /**
     * 내 프로필 조회
     * GET /api/v1/profile/me
     */
    @GetMapping("/me")
    public ResponseEntity<ProfileResponseDto> getMyProfile(@AuthenticationPrincipal Long userId) {
        log.debug("프로필 조회 요청: userId={}", userId);

        ProfileResponseDto profile = profileService.getMyProfile(userId);

        return ResponseEntity.ok(profile);
    }
}