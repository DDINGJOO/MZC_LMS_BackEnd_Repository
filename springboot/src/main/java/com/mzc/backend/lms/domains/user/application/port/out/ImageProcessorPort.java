package com.mzc.backend.lms.domains.user.application.port.out;

import org.springframework.web.multipart.MultipartFile;

/**
 * 이미지 처리 Port
 * User 도메인에서 프로필 이미지 처리를 위한 인터페이스
 */
public interface ImageProcessorPort {

    /**
     * 이미지 파일 검증
     * @param file 이미지 파일
     */
    void validate(MultipartFile file);

    /**
     * 고유 파일명 생성 (ULID)
     * @return 생성된 파일명
     */
    String generateFileName();

    /**
     * 바이트 배열을 WebP 형식으로 변환
     * @param fileBytes 원본 이미지 바이트 배열
     * @return WebP 형식 바이트 배열
     */
    byte[] convertToWebpFromBytes(byte[] fileBytes);

    /**
     * 바이트 배열에서 썸네일 생성
     * @param fileBytes 원본 이미지 바이트 배열
     * @return 썸네일 바이트 배열
     */
    byte[] createThumbnailFromBytes(byte[] fileBytes);
}
