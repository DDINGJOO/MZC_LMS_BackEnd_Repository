package com.mzc.backend.lms.common.ratelimit;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Rate Limiting 인터셉터
 * Bucket4j 기반 Rate Limiting
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RateLimitInterceptor implements HandlerInterceptor {

    private static final String HEADER_LIMIT = "X-RateLimit-Limit";
    private static final String HEADER_REMAINING = "X-RateLimit-Remaining";
    private static final String HEADER_RESET = "X-RateLimit-Reset";

    private final RateLimitConfig rateLimitConfig;
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        rateLimitConfig.initDefaults();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!rateLimitConfig.isEnabled()) {
            return true;
        }

        String method = request.getMethod();
        String path = request.getRequestURI();
        String clientKey = resolveClientKey(request);

        // 엔드포인트별 제한 조회
        RateLimitConfig.EndpointLimit endpointLimit = rateLimitConfig.getEndpointLimit(method, path);
        int maxRequests = endpointLimit != null ? endpointLimit.getRequests() : rateLimitConfig.getDefaultRequests();
        int durationSeconds = endpointLimit != null ? endpointLimit.getDurationSeconds() : rateLimitConfig.getDefaultDurationSeconds();

        // Bucket 조회 또는 생성
        String bucketKey = clientKey + ":" + method + ":" + path;
        Bucket bucket = resolveBucket(bucketKey, maxRequests, durationSeconds);

        // 요청 소비 시도
        if (bucket.tryConsume(1)) {
            long availableTokens = bucket.getAvailableTokens();

            // Rate Limit 헤더 추가
            response.setHeader(HEADER_LIMIT, String.valueOf(maxRequests));
            response.setHeader(HEADER_REMAINING, String.valueOf(availableTokens));
            response.setHeader(HEADER_RESET, String.valueOf(System.currentTimeMillis() / 1000 + durationSeconds));

            return true;
        } else {
            // Rate Limit 초과
            log.warn("Rate limit exceeded - Client: {}, Method: {}, Path: {}", clientKey, method, path);

            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setHeader(HEADER_LIMIT, String.valueOf(maxRequests));
            response.setHeader(HEADER_REMAINING, "0");
            response.setHeader(HEADER_RESET, String.valueOf(System.currentTimeMillis() / 1000 + durationSeconds));
            response.setHeader("Retry-After", String.valueOf(durationSeconds));

            try {
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"Too Many Requests\",\"message\":\"Rate limit exceeded. Please try again later.\"}");
            } catch (Exception e) {
                log.error("Failed to write rate limit response", e);
            }

            return false;
        }
    }

    private Bucket resolveBucket(String key, int maxRequests, int durationSeconds) {
        return buckets.computeIfAbsent(key, k -> {
            Bandwidth limit = Bandwidth.classic(
                    maxRequests,
                    Refill.intervally(maxRequests, Duration.ofSeconds(durationSeconds))
            );
            return Bucket.builder()
                    .addLimit(limit)
                    .build();
        });
    }

    private String resolveClientKey(HttpServletRequest request) {
        // 인증된 사용자는 userId 사용
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal())) {
            return "user:" + authentication.getName();
        }

        // 비인증 사용자는 IP 사용
        return "ip:" + getClientIp(request);
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
