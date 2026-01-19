package com.mzc.backend.lms.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

/**
 * API 요청/응답 로깅 인터셉터
 * - MDC 기반 Request ID 추적
 * - 응답 시간 기록
 * - 느린 요청 경고
 */
@Slf4j
@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {

    private static final String REQUEST_ID = "requestId";
    private static final String START_TIME = "startTime";
    private static final long SLOW_REQUEST_THRESHOLD_MS = 3000L;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestId = UUID.randomUUID().toString().substring(0, 8);
        MDC.put(REQUEST_ID, requestId);
        request.setAttribute(START_TIME, System.currentTimeMillis());

        String userId = getCurrentUserId();
        String userAgent = request.getHeader("User-Agent");
        String clientIp = getClientIp(request);

        log.info("[{}] --> {} {} - User: {} - IP: {} - UA: {}",
                requestId,
                request.getMethod(),
                request.getRequestURI(),
                userId != null ? userId : "anonymous",
                clientIp,
                userAgent != null ? truncate(userAgent, 50) : "unknown");

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        Long startTime = (Long) request.getAttribute(START_TIME);
        long duration = startTime != null ? System.currentTimeMillis() - startTime : 0;

        String requestId = MDC.get(REQUEST_ID);
        int status = response.getStatus();

        if (ex != null) {
            log.error("[{}] <-- {} {} - Status: {} - Duration: {}ms - Error: {}",
                    requestId,
                    request.getMethod(),
                    request.getRequestURI(),
                    status,
                    duration,
                    ex.getMessage());
        } else if (duration > SLOW_REQUEST_THRESHOLD_MS) {
            log.warn("[{}] <-- {} {} - Status: {} - Duration: {}ms [SLOW REQUEST]",
                    requestId,
                    request.getMethod(),
                    request.getRequestURI(),
                    status,
                    duration);
        } else {
            log.info("[{}] <-- {} {} - Status: {} - Duration: {}ms",
                    requestId,
                    request.getMethod(),
                    request.getRequestURI(),
                    status,
                    duration);
        }

        MDC.clear();
    }

    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal())) {
            return authentication.getName();
        }
        return null;
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // X-Forwarded-For의 경우 첫 번째 IP가 실제 클라이언트 IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    private String truncate(String str, int maxLength) {
        if (str == null) {
            return null;
        }
        return str.length() > maxLength ? str.substring(0, maxLength) + "..." : str;
    }
}
