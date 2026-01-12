package com.mzc.backend.lms.domains.user.adapter.out.cache;

import com.mzc.backend.lms.domains.user.application.port.out.UserCachePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * User 캐시 어댑터
 */
@Component
@RequiredArgsConstructor
public class UserCacheAdapter implements UserCachePort {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void set(String key, String value, long ttlHours) {
        redisTemplate.opsForValue().set(key, value, ttlHours, TimeUnit.HOURS);
    }

    @Override
    public Map<String, String> multiGet(Set<String> keys) {
        Map<String, String> result = new HashMap<>();

        if (keys == null || keys.isEmpty()) {
            return result;
        }

        List<String> keyList = keys.stream().toList();
        List<String> values = redisTemplate.opsForValue().multiGet(keyList);

        if (values != null) {
            int i = 0;
            for (String key : keyList) {
                String value = values.get(i++);
                if (value != null) {
                    result.put(key, value);
                }
            }
        }

        return result;
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
