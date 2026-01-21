package com.mzc.lms.media.adapter.out.storage;

import com.mzc.lms.media.application.port.out.MediaStoragePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Local Storage Adapter for Nginx static file serving.
 *
 * This adapter stores files to local filesystem, which is mounted as a shared volume
 * between the media-service and Nginx. Nginx serves files directly without going through
 * the application server.
 *
 * K8s deployment architecture:
 * - PersistentVolume: Shared storage (NFS or cloud storage like EFS/Azure Files)
 * - media-service pod: Writes files to /var/lms/media
 * - nginx pod/sidecar: Serves files from /var/lms/media via http://cdn.lms.local/media
 *
 * Storage structure:
 * /var/lms/media/
 *   ├── images/
 *   │   └── 2024/01/21/
 *   │       ├── {uuid}.jpg
 *   │       └── thumbnails/
 *   │           ├── {uuid}_150.jpg
 *   │           ├── {uuid}_300.jpg
 *   │           └── {uuid}_600.jpg
 *   ├── videos/
 *   ├── documents/
 *   └── others/
 */
@Component
public class LocalStorageAdapter implements MediaStoragePort {

    private static final Logger log = LoggerFactory.getLogger(LocalStorageAdapter.class);

    @Value("${media.storage.base-path:/var/lms/media}")
    private String basePath;

    @Value("${media.storage.base-url:http://cdn.lms.local/media}")
    private String baseUrl;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    @Override
    public StorageResult store(InputStream inputStream, String originalFileName, String contentType, Long fileSize) {
        try {
            String mediaTypeDir = getMediaTypeDirectory(contentType);
            String dateDir = LocalDate.now().format(DATE_FORMATTER);
            String extension = getExtension(originalFileName);
            String storedFileName = UUID.randomUUID().toString() + extension;

            // Build storage path: /var/lms/media/images/2024/01/21/uuid.jpg
            String relativePath = String.join("/", mediaTypeDir, dateDir, storedFileName);
            Path fullPath = Paths.get(basePath, relativePath);

            // Create directories if not exist
            Files.createDirectories(fullPath.getParent());

            // Copy file to storage
            Files.copy(inputStream, fullPath, StandardCopyOption.REPLACE_EXISTING);

            // Build URL: http://cdn.lms.local/media/images/2024/01/21/uuid.jpg
            String url = baseUrl + "/" + relativePath;

            log.info("File stored: path={}, url={}", fullPath, url);

            return new StorageResult(storedFileName, relativePath, url);

        } catch (IOException e) {
            log.error("Failed to store file: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to store file", e);
        }
    }

    @Override
    public StorageResult storeThumbnail(InputStream inputStream, String originalFileName, String size) {
        try {
            // Extract base path from original file name
            String baseName = getBaseName(originalFileName);
            String extension = getExtension(originalFileName);
            String thumbnailFileName = baseName + "_" + size + extension;

            String dateDir = LocalDate.now().format(DATE_FORMATTER);
            String relativePath = String.join("/", "images", dateDir, "thumbnails", thumbnailFileName);
            Path fullPath = Paths.get(basePath, relativePath);

            // Create directories if not exist
            Files.createDirectories(fullPath.getParent());

            // Copy thumbnail to storage
            Files.copy(inputStream, fullPath, StandardCopyOption.REPLACE_EXISTING);

            String url = baseUrl + "/" + relativePath;

            log.info("Thumbnail stored: path={}, size={}, url={}", fullPath, size, url);

            return new StorageResult(thumbnailFileName, relativePath, url);

        } catch (IOException e) {
            log.error("Failed to store thumbnail: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to store thumbnail", e);
        }
    }

    @Override
    public void delete(String storagePath) {
        try {
            Path fullPath = Paths.get(basePath, storagePath);
            Files.deleteIfExists(fullPath);
            log.info("File deleted: path={}", fullPath);
        } catch (IOException e) {
            log.error("Failed to delete file: {}", e.getMessage(), e);
        }
    }

    @Override
    public void deleteThumbnails(String storagePath) {
        try {
            // Get the directory and base name
            Path originalPath = Paths.get(basePath, storagePath);
            Path thumbnailDir = originalPath.getParent().resolve("thumbnails");

            if (Files.exists(thumbnailDir)) {
                String baseName = getBaseName(originalPath.getFileName().toString());

                Files.list(thumbnailDir)
                        .filter(path -> path.getFileName().toString().startsWith(baseName + "_"))
                        .forEach(path -> {
                            try {
                                Files.delete(path);
                                log.info("Thumbnail deleted: path={}", path);
                            } catch (IOException e) {
                                log.error("Failed to delete thumbnail: {}", e.getMessage());
                            }
                        });
            }
        } catch (IOException e) {
            log.error("Failed to delete thumbnails: {}", e.getMessage(), e);
        }
    }

    @Override
    public boolean exists(String storagePath) {
        Path fullPath = Paths.get(basePath, storagePath);
        return Files.exists(fullPath);
    }

    @Override
    public InputStream retrieve(String storagePath) {
        try {
            Path fullPath = Paths.get(basePath, storagePath);
            return new FileInputStream(fullPath.toFile());
        } catch (FileNotFoundException e) {
            log.error("File not found: {}", storagePath);
            throw new RuntimeException("File not found: " + storagePath, e);
        }
    }

    private String getMediaTypeDirectory(String contentType) {
        if (contentType == null) return "others";
        if (contentType.startsWith("image/")) return "images";
        if (contentType.startsWith("video/")) return "videos";
        if (contentType.startsWith("audio/")) return "audios";
        if (contentType.contains("document") || contentType.contains("pdf") ||
            contentType.contains("spreadsheet") || contentType.contains("presentation")) {
            return "documents";
        }
        return "others";
    }

    private String getExtension(String fileName) {
        if (fileName == null) return "";
        int lastDot = fileName.lastIndexOf('.');
        if (lastDot > 0) {
            return fileName.substring(lastDot).toLowerCase();
        }
        return "";
    }

    private String getBaseName(String fileName) {
        if (fileName == null) return "";
        int lastDot = fileName.lastIndexOf('.');
        if (lastDot > 0) {
            return fileName.substring(0, lastDot);
        }
        return fileName;
    }
}
