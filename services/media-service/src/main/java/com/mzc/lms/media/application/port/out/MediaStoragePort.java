package com.mzc.lms.media.application.port.out;

import java.io.InputStream;

public interface MediaStoragePort {

    StorageResult store(InputStream inputStream, String originalFileName, String contentType, Long fileSize);

    StorageResult storeThumbnail(InputStream inputStream, String originalFileName, String size);

    void delete(String storagePath);

    void deleteThumbnails(String storagePath);

    boolean exists(String storagePath);

    InputStream retrieve(String storagePath);

    record StorageResult(
            String storedFileName,
            String storagePath,
            String url
    ) {}
}
