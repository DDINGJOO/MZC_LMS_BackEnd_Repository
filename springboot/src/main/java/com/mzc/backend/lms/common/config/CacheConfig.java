package com.mzc.backend.lms.common.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Redis 캐시 설정
 *
 * 캐시 대상별 TTL 설정:
 * - users: 1시간 (사용자 정보, 변경 빈도 낮음)
 * - subjects: 24시간 (과목 목록, 매우 낮음)
 * - terms: 24시간 (학기 정보, 매우 낮음)
 * - notificationTypes: 24시간 (알림 타입, 매우 낮음)
 * - courses: 30분 (코스 상세, 중간 빈도)
 * - courseTypes: 24시간 (이수구분, 매우 낮음)
 * - colleges: 24시간 (단과대학, 매우 낮음)
 * - departments: 24시간 (학과, 매우 낮음)
 */
@Configuration
@EnableCaching
public class CacheConfig {

    public static final String CACHE_USERS = "users";
    public static final String CACHE_SUBJECTS = "subjects";
    public static final String CACHE_TERMS = "terms";
    public static final String CACHE_NOTIFICATION_TYPES = "notificationTypes";
    public static final String CACHE_COURSES = "courses";
    public static final String CACHE_COURSE_TYPES = "courseTypes";
    public static final String CACHE_COLLEGES = "colleges";
    public static final String CACHE_DEPARTMENTS = "departments";

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        // ObjectMapper 설정 (Java 8 날짜 지원 + 타입 정보 포함)
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );

        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        // 기본 캐시 설정 (30분)
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(30))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jsonSerializer))
                .disableCachingNullValues();

        // 캐시별 TTL 설정
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

        // 사용자 정보 - 1시간
        cacheConfigurations.put(CACHE_USERS, defaultConfig.entryTtl(Duration.ofHours(1)));

        // 과목 목록 - 24시간
        cacheConfigurations.put(CACHE_SUBJECTS, defaultConfig.entryTtl(Duration.ofHours(24)));

        // 학기 정보 - 24시간
        cacheConfigurations.put(CACHE_TERMS, defaultConfig.entryTtl(Duration.ofHours(24)));

        // 알림 타입 - 24시간
        cacheConfigurations.put(CACHE_NOTIFICATION_TYPES, defaultConfig.entryTtl(Duration.ofHours(24)));

        // 코스 상세 - 30분 (기본값)
        cacheConfigurations.put(CACHE_COURSES, defaultConfig.entryTtl(Duration.ofMinutes(30)));

        // 이수구분 - 24시간
        cacheConfigurations.put(CACHE_COURSE_TYPES, defaultConfig.entryTtl(Duration.ofHours(24)));

        // 단과대학 - 24시간
        cacheConfigurations.put(CACHE_COLLEGES, defaultConfig.entryTtl(Duration.ofHours(24)));

        // 학과 - 24시간
        cacheConfigurations.put(CACHE_DEPARTMENTS, defaultConfig.entryTtl(Duration.ofHours(24)));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
                .transactionAware()
                .build();
    }
}
