package com.mzc.backend.lms.common.ratelimit;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Rate Limit 설정
 * application.yml에서 설정 가능
 */
@Configuration
@ConfigurationProperties(prefix = "rate-limit")
@Getter
@Setter
public class RateLimitConfig {

    private boolean enabled = true;
    private int defaultRequests = 100;
    private int defaultDurationSeconds = 60;
    private Map<String, EndpointLimit> endpoints = new HashMap<>();

    @Getter
    @Setter
    public static class EndpointLimit {
        private int requests;
        private int durationSeconds;
    }

    /**
     * 기본 Rate Limit 설정 초기화
     */
    public void initDefaults() {
        // 수강신청 - 분당 10회
        endpoints.putIfAbsent("POST:/api/v1/enrollments", createLimit(10, 60));
        // 로그인 - 분당 5회
        endpoints.putIfAbsent("POST:/api/v1/auth/login", createLimit(5, 60));
        // 메시지 전송 - 분당 30회
        endpoints.putIfAbsent("POST:/api/v1/messages", createLimit(30, 60));
    }

    private EndpointLimit createLimit(int requests, int durationSeconds) {
        EndpointLimit limit = new EndpointLimit();
        limit.setRequests(requests);
        limit.setDurationSeconds(durationSeconds);
        return limit;
    }

    public EndpointLimit getEndpointLimit(String method, String path) {
        String key = method + ":" + path;
        return endpoints.get(key);
    }
}
