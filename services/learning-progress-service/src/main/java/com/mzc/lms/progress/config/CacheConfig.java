package com.mzc.lms.progress.config;

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

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(30))
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .disableCachingNullValues();

        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

        // 개별 학습 진도 캐시 - 15분
        cacheConfigurations.put("learning-progress",
                defaultConfig.entryTtl(Duration.ofMinutes(15)));

        // 학생별 진도 캐시 - 10분
        cacheConfigurations.put("progress-by-student",
                defaultConfig.entryTtl(Duration.ofMinutes(10)));

        // 강좌별 진도 캐시 - 10분
        cacheConfigurations.put("progress-by-course",
                defaultConfig.entryTtl(Duration.ofMinutes(10)));

        // 콘텐츠 진도 캐시 - 5분 (자주 업데이트됨)
        cacheConfigurations.put("content-progress",
                defaultConfig.entryTtl(Duration.ofMinutes(5)));

        // 학습 진도별 콘텐츠 캐시 - 5분
        cacheConfigurations.put("content-progress-by-learning",
                defaultConfig.entryTtl(Duration.ofMinutes(5)));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
                .transactionAware()
                .build();
    }
}
