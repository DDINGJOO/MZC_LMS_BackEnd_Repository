package com.mzc.backend.lms.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * .env 파일 로드 설정
 * 개발 환경에서 환경 변수를 .env 파일로 관리
 */
@Configuration
public class DotenvConfig {

    @Bean
    public Dotenv dotenv() {
        // .env 파일 로드
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();

        // .env 파일의 환경 변수를 시스템 프로퍼티로 설정
        dotenv.entries().forEach(entry ->
            System.setProperty(entry.getKey(), entry.getValue())
        );

        return dotenv;
    }
}