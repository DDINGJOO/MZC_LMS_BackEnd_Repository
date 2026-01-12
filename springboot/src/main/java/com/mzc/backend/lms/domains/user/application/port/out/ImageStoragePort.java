package com.mzc.backend.lms.domains.user.application.port.out;

/**
 * 이미지 저장소 Port
 * User 도메인에서 프로필 이미지 저장/삭제를 위한 인터페이스
 */
public interface ImageStoragePort {

    /**
     * 이미지 저장
     * @param data 이미지 데이터
     * @param fileName 파일명
     * @param contentType 콘텐츠 타입
     * @return 저장된 이미지 URL
     */
    String store(byte[] data, String fileName, String contentType);

    /**
     * 썸네일 저장
     * @param data 썸네일 데이터
     * @param fileName 파일명
     * @return 저장된 썸네일 URL
     */
    String storeThumbnail(byte[] data, String fileName);

    /**
     * 이미지 삭제
     * @param imageUrl 이미지 URL
     */
    void delete(String imageUrl);
}
