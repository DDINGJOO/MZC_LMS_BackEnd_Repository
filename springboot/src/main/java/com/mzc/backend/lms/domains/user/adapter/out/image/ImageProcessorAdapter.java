package com.mzc.backend.lms.domains.user.adapter.out.image;

import com.mzc.backend.lms.domains.user.application.port.out.ImageProcessorPort;
import com.mzc.backend.lms.util.image.ImageProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * 이미지 처리 어댑터
 */
@Component
@RequiredArgsConstructor
public class ImageProcessorAdapter implements ImageProcessorPort {

    private final ImageProcessor imageProcessor;

    @Override
    public void validate(MultipartFile file) {
        imageProcessor.validate(file);
    }

    @Override
    public String generateFileName() {
        return imageProcessor.generateFileName();
    }

    @Override
    public byte[] convertToWebpFromBytes(byte[] fileBytes) {
        return imageProcessor.convertToWebpFromBytes(fileBytes);
    }

    @Override
    public byte[] createThumbnailFromBytes(byte[] fileBytes) {
        return imageProcessor.createThumbnailFromBytes(fileBytes);
    }
}
