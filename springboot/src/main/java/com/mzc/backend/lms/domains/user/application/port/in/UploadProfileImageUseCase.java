package com.mzc.backend.lms.domains.user.application.port.in;

import org.springframework.web.multipart.MultipartFile;

/**
 * 프로필 이미지 업로드 UseCase
 */
public interface UploadProfileImageUseCase {

    /**
     * 프로필 이미지 업로드
     * @param userId 사용자 ID
     * @param file 이미지 파일
     */
    void uploadProfileImage(Long userId, MultipartFile file);
}
