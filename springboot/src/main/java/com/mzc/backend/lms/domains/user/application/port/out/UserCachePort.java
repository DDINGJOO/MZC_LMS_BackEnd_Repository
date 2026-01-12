package com.mzc.backend.lms.domains.user.application.port.out;

import java.util.Map;
import java.util.Set;

/**
 * User 캐시 Port
 * User 도메인에서 Redis 캐시 접근을 위한 인터페이스
 */
public interface UserCachePort {

    /**
     * 캐시에서 값 조회
     * @param key 캐시 키
     * @return 캐시된 값 (없으면 null)
     */
    String get(String key);

    /**
     * 캐시에 값 저장
     * @param key 캐시 키
     * @param value 저장할 값
     * @param ttlHours TTL (시간)
     */
    void set(String key, String value, long ttlHours);

    /**
     * 여러 키에서 값 일괄 조회
     * @param keys 캐시 키 Set
     * @return 키-값 Map
     */
    Map<String, String> multiGet(Set<String> keys);

    /**
     * 캐시에서 값 삭제
     * @param key 캐시 키
     */
    void delete(String key);
}
