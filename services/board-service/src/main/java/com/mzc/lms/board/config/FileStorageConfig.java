package com.mzc.lms.board.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Configuration
public class FileStorageConfig {

    @Value("${file.storage.base-path:/var/lms/files}")
    private String basePath;

    @PostConstruct
    public void init() throws IOException {
        Files.createDirectories(Paths.get(basePath));
    }
}
