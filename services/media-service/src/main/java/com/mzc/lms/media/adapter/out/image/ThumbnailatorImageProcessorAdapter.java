package com.mzc.lms.media.adapter.out.image;

import com.mzc.lms.media.application.port.out.ImageProcessorPort;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class ThumbnailatorImageProcessorAdapter implements ImageProcessorPort {

    private static final Logger log = LoggerFactory.getLogger(ThumbnailatorImageProcessorAdapter.class);

    @Value("${media.image.allowed-types:image/jpeg,image/png,image/gif,image/webp}")
    private String allowedTypes;

    @Value("${media.image.thumbnail.quality:0.8}")
    private double thumbnailQuality;

    private static final Set<String> SUPPORTED_FORMATS = Set.of("jpg", "jpeg", "png", "gif", "webp", "bmp");

    @Override
    public ImageInfo getImageInfo(InputStream inputStream) {
        try {
            BufferedImage image = ImageIO.read(inputStream);
            if (image == null) {
                throw new RuntimeException("Failed to read image");
            }

            // Detect format from image (simplified, actual format detection would need more logic)
            String format = "unknown";

            return new ImageInfo(image.getWidth(), image.getHeight(), format);

        } catch (IOException e) {
            log.error("Failed to get image info: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get image info", e);
        }
    }

    @Override
    public InputStream createThumbnail(InputStream inputStream, int width) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            Thumbnails.of(inputStream)
                    .width(width)
                    .keepAspectRatio(true)
                    .outputQuality(thumbnailQuality)
                    .toOutputStream(outputStream);

            log.debug("Thumbnail created: width={}", width);

            return new ByteArrayInputStream(outputStream.toByteArray());

        } catch (IOException e) {
            log.error("Failed to create thumbnail: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create thumbnail", e);
        }
    }

    @Override
    public Map<String, InputStream> createThumbnails(InputStream inputStream, int[] widths) {
        Map<String, InputStream> thumbnails = new HashMap<>();

        try {
            // Read input stream into byte array for multiple uses
            byte[] imageBytes = inputStream.readAllBytes();

            for (int width : widths) {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                Thumbnails.of(new ByteArrayInputStream(imageBytes))
                        .width(width)
                        .keepAspectRatio(true)
                        .outputQuality(thumbnailQuality)
                        .toOutputStream(outputStream);

                thumbnails.put(String.valueOf(width), new ByteArrayInputStream(outputStream.toByteArray()));
            }

            log.debug("Thumbnails created: widths={}", Arrays.toString(widths));

            return thumbnails;

        } catch (IOException e) {
            log.error("Failed to create thumbnails: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create thumbnails", e);
        }
    }

    @Override
    public boolean isValidImage(InputStream inputStream, String mimeType) {
        // Check MIME type
        if (mimeType == null || !isAllowedMimeType(mimeType)) {
            return false;
        }

        // Try to read image to validate it
        try {
            BufferedImage image = ImageIO.read(inputStream);
            return image != null;
        } catch (IOException e) {
            log.warn("Invalid image: {}", e.getMessage());
            return false;
        }
    }

    private boolean isAllowedMimeType(String mimeType) {
        return Arrays.stream(allowedTypes.split(","))
                .map(String::trim)
                .anyMatch(type -> type.equalsIgnoreCase(mimeType));
    }
}
