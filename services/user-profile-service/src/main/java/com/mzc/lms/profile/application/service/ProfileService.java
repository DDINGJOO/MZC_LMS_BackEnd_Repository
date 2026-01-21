package com.mzc.lms.profile.application.service;

import com.mzc.lms.profile.adapter.in.web.dto.ProfileSearchRequest;
import com.mzc.lms.profile.adapter.in.web.dto.ProfileSearchResponse;
import com.mzc.lms.profile.adapter.in.web.dto.ProfileUpdateRequest;
import com.mzc.lms.profile.application.port.in.GetProfileUseCase;
import com.mzc.lms.profile.application.port.in.SearchProfileUseCase;
import com.mzc.lms.profile.application.port.in.UpdateProfileUseCase;
import com.mzc.lms.profile.application.port.out.EventPublisherPort;
import com.mzc.lms.profile.application.port.out.ProfessorProfileRepositoryPort;
import com.mzc.lms.profile.application.port.out.StudentProfileRepositoryPort;
import com.mzc.lms.profile.application.port.out.UserProfileRepositoryPort;
import com.mzc.lms.profile.domain.event.ProfileUpdatedEvent;
import com.mzc.lms.profile.domain.model.ProfessorProfile;
import com.mzc.lms.profile.domain.model.StudentProfile;
import com.mzc.lms.profile.domain.model.UserProfile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileService implements GetProfileUseCase, UpdateProfileUseCase, SearchProfileUseCase {

    private final UserProfileRepositoryPort userProfileRepository;
    private final StudentProfileRepositoryPort studentProfileRepository;
    private final ProfessorProfileRepositoryPort professorProfileRepository;
    private final EventPublisherPort eventPublisher;

    @Override
    public UserProfile getProfile(Long userId) {
        log.debug("Getting profile for userId: {}", userId);
        return userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found for userId: " + userId));
    }

    @Override
    public Optional<StudentProfile> getStudentProfile(Long userId) {
        log.debug("Getting student profile for userId: {}", userId);
        return studentProfileRepository.findByUserId(userId);
    }

    @Override
    public Optional<ProfessorProfile> getProfessorProfile(Long userId) {
        log.debug("Getting professor profile for userId: {}", userId);
        return professorProfileRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public UserProfile updateProfile(Long userId, ProfileUpdateRequest request) {
        log.info("Updating profile for userId: {}", userId);

        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found for userId: " + userId));

        String oldName = profile.getName();

        if (request.getName() != null && !request.getName().equals(oldName)) {
            profile.updateName(request.getName());
            eventPublisher.publishProfileUpdated(
                    ProfileUpdatedEvent.nameChanged(userId, oldName, request.getName())
            );
        }

        return userProfileRepository.save(profile);
    }

    @Override
    @Transactional
    public void updateProfileImage(Long userId, String imageUrl) {
        log.info("Updating profile image for userId: {}", userId);

        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found for userId: " + userId));

        String oldImageUrl = profile.getProfileImageUrl();
        profile.updateProfileImage(imageUrl);

        userProfileRepository.save(profile);
        eventPublisher.publishProfileUpdated(
                ProfileUpdatedEvent.imageChanged(userId, oldImageUrl, imageUrl)
        );
    }

    @Override
    @Transactional
    public void deleteProfileImage(Long userId) {
        log.info("Deleting profile image for userId: {}", userId);

        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found for userId: " + userId));

        String oldImageUrl = profile.getProfileImageUrl();
        profile.updateProfileImage(null);

        userProfileRepository.save(profile);
        eventPublisher.publishProfileUpdated(
                ProfileUpdatedEvent.imageChanged(userId, oldImageUrl, null)
        );
    }

    @Override
    public Page<ProfileSearchResponse> searchProfiles(ProfileSearchRequest request, Pageable pageable) {
        log.debug("Searching profiles with request: {}", request);
        // Implement search logic with repository
        return Page.empty(pageable);
    }
}
