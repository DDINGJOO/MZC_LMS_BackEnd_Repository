package com.mzc.backend.lms.domains.user.application.service;

import com.mzc.backend.lms.domains.user.adapter.out.persistence.entity.UserProfileImage;
import com.mzc.backend.lms.domains.user.application.port.in.DeleteProfileImageUseCase;
import com.mzc.backend.lms.domains.user.application.port.in.UploadProfileImageUseCase;
import com.mzc.backend.lms.domains.user.application.port.out.*;
import com.mzc.backend.lms.domains.user.exception.UserErrorCode;
import com.mzc.backend.lms.domains.user.exception.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * 프로필 이미지 서비스
 * (Hexagonal Architecture - Application Service)
 * 비동기로 이미지 처리 (WebP 변환, 썸네일 생성)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileImageService implements UploadProfileImageUseCase, DeleteProfileImageUseCase {

    private final UserQueryPort userQueryPort;
    private final UserProfileImageQueryPort userProfileImageQueryPort;
    private final UserProfileImageCommandPort userProfileImageCommandPort;
    private final ImageProcessorPort imageProcessorPort;
    private final ImageStoragePort imageStoragePort;

    // Self-injection for @Async proxy call
    private ProfileImageService self;

    @Autowired
    @Lazy
    public void setSelf(ProfileImageService self) {
        this.self = self;
    }

    @Override
    @Transactional
    public void uploadProfileImage(Long userId, MultipartFile file) {
        // 1. 사용자 검증
        userQueryPort.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        // 2. 파일 검증
        imageProcessorPort.validate(file);

        // 3. 기존 이미지 파일 삭제 (DB 레코드는 비동기에서 업데이트)
        deleteExistingImageFiles(userId);

        // 4. 이미지 데이터 미리 읽기 (비동기 스레드에서 MultipartFile 접근 불가)
        byte[] fileBytes;
        try {
            fileBytes = file.getBytes();
        } catch (Exception e) {
            throw new RuntimeException("파일 읽기 실패", e);
        }

        // 5. 비동기로 이미지 처리 및 저장 (self-proxy를 통해 호출해야 @Async 동작)
        self.processAndSaveImageAsync(userId, fileBytes);
    }

    @Async("imageProcessingExecutor")
    public CompletableFuture<Void> processAndSaveImageAsync(Long userId, byte[] fileBytes) {
        try {
            log.info("프로필 이미지 처리 시작: userId={}", userId);

            // 1. 파일명 생성 (ULID)
            String fileName = imageProcessorPort.generateFileName();

            // 2. WebP 변환 및 저장
            byte[] webpData = imageProcessorPort.convertToWebpFromBytes(fileBytes);
            String imageUrl = imageStoragePort.store(webpData, fileName, "image/webp");

            // 3. 썸네일 생성 및 저장
            byte[] thumbnailData = imageProcessorPort.createThumbnailFromBytes(fileBytes);
            String thumbnailUrl = imageStoragePort.storeThumbnail(thumbnailData, fileName);

            // 4. DB 저장 (새로운 트랜잭션에서 실행)
            saveProfileImage(userId, imageUrl, thumbnailUrl);

            log.info("프로필 이미지 처리 완료: userId={}, imageUrl={}", userId, imageUrl);
            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            log.error("프로필 이미지 처리 실패: userId={}", userId, e);
            return CompletableFuture.failedFuture(e);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveProfileImage(Long userId, String imageUrl, String thumbnailUrl) {
        Optional<UserProfileImage> existingImage = userProfileImageQueryPort.findByUserId(userId);

        if (existingImage.isPresent()) {
            // 기존 이미지 업데이트 - 더티 체킹으로 자동 저장
            existingImage.get().updateImage(imageUrl, thumbnailUrl);
        } else {
            // 새 이미지 생성 - userId만 사용
            UserProfileImage profileImage = UserProfileImage.createWithUserId(userId, imageUrl, thumbnailUrl);
            userProfileImageCommandPort.save(profileImage);
        }
    }

    @Override
    @Transactional
    public void deleteProfileImage(Long userId) {
        userQueryPort.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        // 파일 삭제
        deleteExistingImageFiles(userId);

        // DB 삭제
        userProfileImageCommandPort.deleteByUserId(userId);

        log.info("프로필 이미지 삭제 완료: userId={}", userId);
    }

    private void deleteExistingImageFiles(Long userId) {
        userProfileImageQueryPort.findByUserId(userId)
                .ifPresent(image -> {
                    imageStoragePort.delete(image.getImageUrl());
                    imageStoragePort.delete(image.getThumbnailUrl());
                });
    }
}
