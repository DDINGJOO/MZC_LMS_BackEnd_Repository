package com.mzc.backend.lms.domains.user.adapter.out.image;

import com.mzc.backend.lms.domains.user.application.port.out.ImageStoragePort;
import com.mzc.backend.lms.util.image.LocalImageStorageStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 이미지 저장소 어댑터
 */
@Component
@RequiredArgsConstructor
public class ImageStorageAdapter implements ImageStoragePort {

    private final LocalImageStorageStrategy imageStorageStrategy;

    @Override
    public String store(byte[] data, String fileName, String contentType) {
        return imageStorageStrategy.store(data, fileName, contentType);
    }

    @Override
    public String storeThumbnail(byte[] data, String fileName) {
        return imageStorageStrategy.storeThumbnail(data, fileName);
    }

    @Override
    public void delete(String imageUrl) {
        imageStorageStrategy.delete(imageUrl);
    }
}
