package com.mzc.lms.media.application.port.out;

import java.io.InputStream;
import java.util.Map;

public interface ImageProcessorPort {

    ImageInfo getImageInfo(InputStream inputStream);

    InputStream createThumbnail(InputStream inputStream, int width);

    Map<String, InputStream> createThumbnails(InputStream inputStream, int[] widths);

    boolean isValidImage(InputStream inputStream, String mimeType);

    record ImageInfo(
            int width,
            int height,
            String format
    ) {}
}
