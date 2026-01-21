package com.mzc.lms.profile.application.port.in;

import com.mzc.lms.profile.adapter.in.web.dto.ProfileUpdateRequest;
import com.mzc.lms.profile.domain.model.UserProfile;

public interface UpdateProfileUseCase {

    UserProfile updateProfile(Long userId, ProfileUpdateRequest request);

    void updateProfileImage(Long userId, String imageUrl);

    void deleteProfileImage(Long userId);
}
