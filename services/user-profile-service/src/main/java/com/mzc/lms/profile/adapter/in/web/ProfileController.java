package com.mzc.lms.profile.adapter.in.web;

import com.mzc.lms.profile.adapter.in.web.dto.ProfileResponse;
import com.mzc.lms.profile.adapter.in.web.dto.ProfileSearchRequest;
import com.mzc.lms.profile.adapter.in.web.dto.ProfileSearchResponse;
import com.mzc.lms.profile.adapter.in.web.dto.ProfileUpdateRequest;
import com.mzc.lms.profile.application.port.in.GetProfileUseCase;
import com.mzc.lms.profile.application.port.in.SearchProfileUseCase;
import com.mzc.lms.profile.application.port.in.UpdateProfileUseCase;
import com.mzc.lms.profile.domain.model.ProfessorProfile;
import com.mzc.lms.profile.domain.model.StudentProfile;
import com.mzc.lms.profile.domain.model.UserProfile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
@Tag(name = "Profile", description = "Profile API")
public class ProfileController {

    private final GetProfileUseCase getProfileUseCase;
    private final UpdateProfileUseCase updateProfileUseCase;
    private final SearchProfileUseCase searchProfileUseCase;

    @GetMapping("/{userId}")
    @Operation(summary = "Get profile", description = "Get user profile by userId")
    public ResponseEntity<ProfileResponse> getProfile(@PathVariable Long userId) {
        log.debug("Getting profile for userId: {}", userId);

        UserProfile profile = getProfileUseCase.getProfile(userId);
        StudentProfile studentProfile = getProfileUseCase.getStudentProfile(userId).orElse(null);
        ProfessorProfile professorProfile = getProfileUseCase.getProfessorProfile(userId).orElse(null);

        return ResponseEntity.ok(ProfileResponse.from(profile, studentProfile, professorProfile));
    }

    @GetMapping("/me")
    @Operation(summary = "Get my profile", description = "Get current user's profile")
    public ResponseEntity<ProfileResponse> getMyProfile(@RequestHeader("X-User-Id") Long userId) {
        log.debug("Getting my profile for userId: {}", userId);

        UserProfile profile = getProfileUseCase.getProfile(userId);
        StudentProfile studentProfile = getProfileUseCase.getStudentProfile(userId).orElse(null);
        ProfessorProfile professorProfile = getProfileUseCase.getProfessorProfile(userId).orElse(null);

        return ResponseEntity.ok(ProfileResponse.from(profile, studentProfile, professorProfile));
    }

    @PatchMapping("/me")
    @Operation(summary = "Update my profile", description = "Update current user's profile")
    public ResponseEntity<ProfileResponse> updateMyProfile(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody ProfileUpdateRequest request) {
        log.debug("Updating profile for userId: {}", userId);

        UserProfile profile = updateProfileUseCase.updateProfile(userId, request);
        StudentProfile studentProfile = getProfileUseCase.getStudentProfile(userId).orElse(null);
        ProfessorProfile professorProfile = getProfileUseCase.getProfessorProfile(userId).orElse(null);

        return ResponseEntity.ok(ProfileResponse.from(profile, studentProfile, professorProfile));
    }

    @PostMapping("/me/image")
    @Operation(summary = "Update profile image", description = "Update current user's profile image URL")
    public ResponseEntity<Map<String, String>> updateProfileImage(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody Map<String, String> request) {
        log.debug("Updating profile image for userId: {}", userId);

        String imageUrl = request.get("imageUrl");
        updateProfileUseCase.updateProfileImage(userId, imageUrl);

        return ResponseEntity.ok(Map.of("message", "Profile image updated successfully"));
    }

    @DeleteMapping("/me/image")
    @Operation(summary = "Delete profile image", description = "Delete current user's profile image")
    public ResponseEntity<Map<String, String>> deleteProfileImage(@RequestHeader("X-User-Id") Long userId) {
        log.debug("Deleting profile image for userId: {}", userId);

        updateProfileUseCase.deleteProfileImage(userId);

        return ResponseEntity.ok(Map.of("message", "Profile image deleted successfully"));
    }

    @GetMapping("/search")
    @Operation(summary = "Search profiles", description = "Search profiles with filters")
    public ResponseEntity<Page<ProfileSearchResponse>> searchProfiles(
            ProfileSearchRequest request,
            Pageable pageable) {
        log.debug("Searching profiles with request: {}", request);

        Page<ProfileSearchResponse> result = searchProfileUseCase.searchProfiles(request, pageable);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Check service health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "user-profile-service"));
    }
}
