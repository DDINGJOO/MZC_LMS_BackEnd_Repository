package com.mzc.backend.lms.domains.user.auth.encryption.advice;

import com.mzc.backend.lms.domains.user.auth.encryption.annotation.Encrypted;
import com.mzc.backend.lms.domains.user.auth.encryption.service.EncryptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 응답 객체의 @Encrypted 필드를 자동으로 복호화하는 Advice
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class EncryptionResponseAdvice implements ResponseBodyAdvice<Object> {

    private final EncryptionService encryptionService;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        if (body == null) {
            return null;
        }

        try {
            decryptFields(body, new HashSet<>());
        } catch (Exception e) {
            log.error("응답 복호화 처리 중 오류 발생", e);
        }

        return body;
    }

    /**
     * 객체의 @Encrypted 필드를 재귀적으로 복호화
     */
    private void decryptFields(Object obj, Set<Object> visited) {
        if (obj == null || visited.contains(obj)) {
            return;
        }

        // 기본 타입, 래퍼 타입, String은 스킵
        if (isPrimitiveOrWrapper(obj.getClass())) {
            return;
        }

        visited.add(obj);

        // Collection 처리
        if (obj instanceof Collection<?> collection) {
            for (Object item : collection) {
                decryptFields(item, visited);
            }
            return;
        }

        // 객체의 필드 처리
        Class<?> clazz = obj.getClass();
        while (clazz != null && clazz != Object.class) {
            for (Field field : clazz.getDeclaredFields()) {
                processField(obj, field, visited);
            }
            clazz = clazz.getSuperclass();
        }
    }

    /**
     * 개별 필드 처리
     */
    private void processField(Object obj, Field field, Set<Object> visited) {
        try {
            field.setAccessible(true);
            Object value = field.get(obj);

            if (value == null) {
                return;
            }

            // @Encrypted 어노테이션이 있고 String 타입인 경우 복호화
            if (field.isAnnotationPresent(Encrypted.class) && value instanceof String encryptedValue) {
                String decrypted = decryptValue(encryptedValue);
                field.set(obj, decrypted);
                return;
            }

            // 중첩 객체 재귀 처리
            if (!isPrimitiveOrWrapper(value.getClass())) {
                decryptFields(value, visited);
            }
        } catch (IllegalAccessException e) {
            log.debug("필드 접근 실패: {}", field.getName());
        }
    }

    /**
     * 암호화된 값 복호화
     */
    private String decryptValue(String encryptedValue) {
        if (encryptedValue == null || encryptedValue.isEmpty()) {
            return encryptedValue;
        }

        try {
            return encryptionService.decryptPersonalInfo(encryptedValue);
        } catch (Exception e) {
            log.warn("복호화 실패, 원본 반환: {}", e.getMessage());
            return encryptedValue;
        }
    }

    /**
     * 기본 타입 또는 래퍼 타입 확인
     */
    private boolean isPrimitiveOrWrapper(Class<?> clazz) {
        return clazz.isPrimitive() ||
                clazz == String.class ||
                clazz == Boolean.class ||
                clazz == Character.class ||
                clazz == Byte.class ||
                clazz == Short.class ||
                clazz == Integer.class ||
                clazz == Long.class ||
                clazz == Float.class ||
                clazz == Double.class ||
                clazz == Void.class ||
                clazz.isEnum() ||
                clazz.getPackage() != null && clazz.getPackage().getName().startsWith("java.");
    }
}
