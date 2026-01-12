package com.mzc.backend.lms.domains.user.application.port.in;

/**
 * 프로필 이미지 삭제 UseCase
 */
public interface DeleteProfileImageUseCase {

    /**
     * 프로필 이미지 삭제
     * @param userId 사용자 ID
     */
    void deleteProfileImage(Long userId);
}
