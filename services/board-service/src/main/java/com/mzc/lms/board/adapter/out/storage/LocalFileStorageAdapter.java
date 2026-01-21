package com.mzc.lms.board.adapter.out.storage;

import com.mzc.lms.board.application.port.out.FileStoragePort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
@Slf4j
public class LocalFileStorageAdapter implements FileStoragePort {

    @Value("${file.storage.base-path:/var/lms/files}")
    private String basePath;

    @Value("${file.storage.base-url:http://localhost/files}")
    private String baseUrl;

    @Override
    public FileUploadResult uploadFile(InputStream inputStream, String originalFileName,
                                        String contentType, Long fileSize) {
        try {
            String storedFileName = generateStoredFileName(originalFileName);
            String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String storagePath = datePath + "/" + storedFileName;
            Path fullPath = Paths.get(basePath, storagePath);

            Files.createDirectories(fullPath.getParent());
            Files.copy(inputStream, fullPath, StandardCopyOption.REPLACE_EXISTING);

            String downloadUrl = baseUrl + "/" + storagePath;

            log.info("파일 업로드 완료: {} -> {}", originalFileName, storagePath);

            return new FileUploadResult(storedFileName, storagePath, downloadUrl);
        } catch (IOException e) {
            log.error("파일 업로드 실패: {}", originalFileName, e);
            throw new RuntimeException("파일 업로드에 실패했습니다", e);
        }
    }

    @Override
    public void deleteFile(String storagePath) {
        try {
            Path fullPath = Paths.get(basePath, storagePath);
            Files.deleteIfExists(fullPath);
            log.info("파일 삭제 완료: {}", storagePath);
        } catch (IOException e) {
            log.error("파일 삭제 실패: {}", storagePath, e);
            throw new RuntimeException("파일 삭제에 실패했습니다", e);
        }
    }

    @Override
    public boolean exists(String storagePath) {
        Path fullPath = Paths.get(basePath, storagePath);
        return Files.exists(fullPath);
    }

    private String generateStoredFileName(String originalFileName) {
        String extension = "";
        int lastDotIndex = originalFileName.lastIndexOf('.');
        if (lastDotIndex > 0) {
            extension = originalFileName.substring(lastDotIndex);
        }
        return UUID.randomUUID().toString() + extension;
    }
}
