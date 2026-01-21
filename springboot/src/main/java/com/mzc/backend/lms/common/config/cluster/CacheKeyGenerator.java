package com.mzc.backend.lms.common.config.cluster;

/**
 * Redis Cluster용 캐시 키 생성 유틸리티
 *
 * Hash Tag 기반 키 생성:
 * - {user:1}:profile, {user:1}:courses 등 동일한 user:1을 가진 키들은 같은 슬롯에 저장
 * - 이를 통해 multi-key 연산 시 크로스 슬롯 에러 방지
 * - Hash tag는 {} 내부의 문자열로 슬롯 결정
 *
 * 사용 예시:
 * <pre>
 * {@code
 * String cacheKey = CacheKeyGenerator.userKey(userId, "profile");
 * // 결과: {user:123}:profile
 *
 * String courseKey = CacheKeyGenerator.courseKey(courseId, "details");
 * // 결과: {course:456}:details
 * }
 * </pre>
 *
 * Redis Cluster 슬롯 배치:
 * - 총 16384개 슬롯
 * - CRC16({user:123}) % 16384 = 슬롯 번호
 * - 동일한 hash tag를 가진 키는 항상 같은 슬롯에 배치
 */
public class CacheKeyGenerator {

    /**
     * 사용자 관련 캐시 키 생성
     *
     * @param userId 사용자 ID
     * @param suffix 키 접미사 (예: "profile", "courses", "enrollments")
     * @return Hash tag 기반 캐시 키 (예: "{user:123}:profile")
     */
    public static String userKey(Long userId, String suffix) {
        return "{user:" + userId + "}:" + suffix;
    }

    /**
     * 코스 관련 캐시 키 생성
     *
     * @param courseId 코스 ID
     * @param suffix 키 접미사 (예: "details", "enrollments", "lectures")
     * @return Hash tag 기반 캐시 키 (예: "{course:456}:details")
     */
    public static String courseKey(Long courseId, String suffix) {
        return "{course:" + courseId + "}:" + suffix;
    }

    /**
     * 학기 관련 캐시 키 생성
     *
     * @param termId 학기 ID
     * @param suffix 키 접미사 (예: "info", "courses", "schedule")
     * @return Hash tag 기반 캐시 키 (예: "{term:789}:info")
     */
    public static String termKey(Long termId, String suffix) {
        return "{term:" + termId + "}:" + suffix;
    }

    /**
     * 과목 관련 캐시 키 생성
     *
     * @param subjectId 과목 ID
     * @param suffix 키 접미사 (예: "info", "courses")
     * @return Hash tag 기반 캐시 키 (예: "{subject:101}:info")
     */
    public static String subjectKey(Long subjectId, String suffix) {
        return "{subject:" + subjectId + "}:" + suffix;
    }

    /**
     * 단과대학 관련 캐시 키 생성
     *
     * @param collegeId 단과대학 ID
     * @param suffix 키 접미사 (예: "info", "departments")
     * @return Hash tag 기반 캐시 키 (예: "{college:10}:info")
     */
    public static String collegeKey(Long collegeId, String suffix) {
        return "{college:" + collegeId + "}:" + suffix;
    }

    /**
     * 학과 관련 캐시 키 생성
     *
     * @param departmentId 학과 ID
     * @param suffix 키 접미사 (예: "info", "courses", "students")
     * @return Hash tag 기반 캐시 키 (예: "{department:20}:info")
     */
    public static String departmentKey(Long departmentId, String suffix) {
        return "{department:" + departmentId + "}:" + suffix;
    }

    /**
     * 알림 관련 캐시 키 생성
     *
     * @param notificationId 알림 ID
     * @param suffix 키 접미사 (예: "content", "read-status")
     * @return Hash tag 기반 캐시 키 (예: "{notification:99}:content")
     */
    public static String notificationKey(Long notificationId, String suffix) {
        return "{notification:" + notificationId + "}:" + suffix;
    }

    /**
     * 글로벌 캐시 키 생성 (Hash tag 없음)
     *
     * 모든 노드에서 접근 가능한 단일 키가 필요한 경우 사용
     * 주의: multi-key 연산 시 크로스 슬롯 에러 발생 가능
     *
     * @param prefix 키 접두사 (예: "global", "config")
     * @param suffix 키 접미사
     * @return 일반 캐시 키 (예: "global:config")
     */
    public static String globalKey(String prefix, String suffix) {
        return prefix + ":" + suffix;
    }
}
