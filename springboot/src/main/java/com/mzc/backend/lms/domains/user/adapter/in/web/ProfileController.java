package com.mzc.backend.lms.domains.user.adapter.in.web;

import com.mzc.backend.lms.domains.user.adapter.in.web.dto.profile.ProfileResponseDto;
import com.mzc.backend.lms.domains.user.adapter.in.web.dto.profile.ProfileUpdateRequestDto;
import com.mzc.backend.lms.domains.user.adapter.in.web.swagger.ProfileControllerSwagger;
import com.mzc.backend.lms.domains.user.application.port.in.DeleteProfileImageUseCase;
import com.mzc.backend.lms.domains.user.application.port.in.GetMyProfileUseCase;
import com.mzc.backend.lms.domains.user.application.port.in.UpdateProfileUseCase;
import com.mzc.backend.lms.domains.user.application.port.in.UploadProfileImageUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 프로필 컨트롤러
 * (Hexagonal Architecture - Inbound Adapter)
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class ProfileController implements ProfileControllerSwagger {

    private final GetMyProfileUseCase getMyProfileUseCase;
    private final UpdateProfileUseCase updateProfileUseCase;
    private final UploadProfileImageUseCase uploadProfileImageUseCase;
    private final DeleteProfileImageUseCase deleteProfileImageUseCase;

    @Override
    @GetMapping("/me")
    public ResponseEntity<ProfileResponseDto> getMyProfile(@AuthenticationPrincipal Long userId) {
        log.debug("프로필 조회 요청: userId={}", userId);

        ProfileResponseDto profile = getMyProfileUseCase.getMyProfile(userId);

        return ResponseEntity.ok(profile);
    }

    @Override
    @PatchMapping("/me")
    public ResponseEntity<ProfileResponseDto> updateProfile(
            @AuthenticationPrincipal Long userId,
            @RequestBody ProfileUpdateRequestDto request) {
        log.debug("프로필 수정 요청: userId={}", userId);

        ProfileResponseDto updatedProfile = updateProfileUseCase.updateProfile(userId, request);

        return ResponseEntity.ok(updatedProfile);
    }

    @Override
    @PostMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadProfileImage(
            @AuthenticationPrincipal Long userId,
            @RequestParam("image") MultipartFile file) {
        log.debug("프로필 이미지 업로드 요청: userId={}, fileName={}", userId, file.getOriginalFilename());

        uploadProfileImageUseCase.uploadProfileImage(userId, file);

        return ResponseEntity.accepted().build();
    }

    @Override
    @DeleteMapping("/me/image")
    public ResponseEntity<Void> deleteProfileImage(@AuthenticationPrincipal Long userId) {
        log.debug("프로필 이미지 삭제 요청: userId={}", userId);

        deleteProfileImageUseCase.deleteProfileImage(userId);

        return ResponseEntity.ok().build();
    }
}
