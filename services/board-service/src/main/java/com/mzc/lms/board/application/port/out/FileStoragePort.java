package com.mzc.lms.board.application.port.out;

import java.io.InputStream;

public interface FileStoragePort {

    FileUploadResult uploadFile(InputStream inputStream, String originalFileName, String contentType, Long fileSize);

    void deleteFile(String storagePath);

    boolean exists(String storagePath);

    record FileUploadResult(
            String storedFileName,
            String storagePath,
            String downloadUrl
    ) {}
}
