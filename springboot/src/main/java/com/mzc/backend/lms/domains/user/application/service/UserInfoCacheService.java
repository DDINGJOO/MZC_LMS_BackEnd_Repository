package com.mzc.backend.lms.domains.user.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mzc.backend.lms.domains.user.adapter.in.web.dto.profile.UserBasicInfoDto;
import com.mzc.backend.lms.domains.user.application.port.in.GetUserInfoCacheUseCase;
import com.mzc.backend.lms.domains.user.application.port.out.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 유저 정보 캐시 서비스
 * (Hexagonal Architecture - Application Service)
 * Redis Cache-Aside 패턴으로 유저 기본 정보 조회
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserInfoCacheService implements GetUserInfoCacheUseCase {

    private static final String CACHE_KEY_PREFIX = "user:info:";
    private static final long CACHE_TTL_HOURS = 1;

    private final ObjectMapper objectMapper;
    private final UserCachePort userCachePort;
    private final DataEncryptionPort dataEncryptionPort;
    private final UserProfileQueryPort userProfileQueryPort;
    private final StudentQueryPort studentQueryPort;
    private final ProfessorQueryPort professorQueryPort;

    @Override
    public Map<Long, UserBasicInfoDto> getUserInfoMap(Set<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<Long, UserBasicInfoDto> result = new HashMap<>();
        Set<Long> cacheMissIds = new HashSet<>();

        // 1. Redis에서 캐시 조회
        for (Long userId : userIds) {
            String cacheKey = CACHE_KEY_PREFIX + userId;
            String cachedValue = userCachePort.get(cacheKey);

            if (cachedValue != null) {
                try {
                    UserBasicInfoDto dto = objectMapper.readValue(cachedValue, UserBasicInfoDto.class);
                    result.put(userId, dto);
                } catch (JsonProcessingException e) {
                    log.warn("캐시 역직렬화 실패 userId={}", userId, e);
                    cacheMissIds.add(userId);
                }
            } else {
                cacheMissIds.add(userId);
            }
        }

        // 2. 캐시 미스인 ID들은 DB에서 조회
        if (!cacheMissIds.isEmpty()) {
            Map<Long, UserBasicInfoDto> dbResult = fetchFromDatabase(cacheMissIds);

            // 3. 조회 결과 캐싱 및 결과에 추가
            for (Map.Entry<Long, UserBasicInfoDto> entry : dbResult.entrySet()) {
                cacheUserInfo(entry.getKey(), entry.getValue());
                result.put(entry.getKey(), entry.getValue());
            }
        }

        return result;
    }

    private Map<Long, UserBasicInfoDto> fetchFromDatabase(Set<Long> userIds) {
        Map<Long, UserBasicInfoDto> result = new HashMap<>();
        List<Long> userIdList = new ArrayList<>(userIds);

        // 이름 조회 (암호화된 상태)
        Map<Long, String> encryptedNames = userProfileQueryPort.findNamesByUserIds(userIdList)
                .stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> (String) row[1]
                ));

        // 학생 ID 조회
        Set<Long> studentIds = studentQueryPort.findAllById(userIdList)
                .stream()
                .map(s -> s.getStudentId())
                .collect(Collectors.toSet());

        // 교수 ID 조회
        Set<Long> professorIds = professorQueryPort.findAllById(userIdList)
                .stream()
                .map(p -> p.getProfessorId())
                .collect(Collectors.toSet());

        // DTO 생성
        for (Long userId : userIds) {
            String encryptedName = encryptedNames.get(userId);
            String decryptedName;

            if (encryptedName != null) {
                try {
                    // 복호화 시도
                    decryptedName = dataEncryptionPort.decryptName(encryptedName);
                } catch (Exception e) {
                    // 복호화 실패 시 평문으로 간주 (V5 더미 데이터 등)
                    log.warn("복호화 실패 userId={}, 평문 사용", userId);
                    decryptedName = encryptedName;
                }
            } else {
                decryptedName = null;
            }

            UserBasicInfoDto dto;
            if (studentIds.contains(userId)) {
                dto = UserBasicInfoDto.ofStudent(userId, decryptedName);
            } else if (professorIds.contains(userId)) {
                dto = UserBasicInfoDto.ofProfessor(userId, decryptedName);
            } else {
                // 학생도 교수도 아닌 경우 (관리자 등)
                dto = UserBasicInfoDto.builder()
                        .id(userId)
                        .name(decryptedName)
                        .userType("UNKNOWN")
                        .build();
            }
            result.put(userId, dto);
        }

        return result;
    }

    private void cacheUserInfo(Long userId, UserBasicInfoDto dto) {
        try {
            String cacheKey = CACHE_KEY_PREFIX + userId;
            String jsonValue = objectMapper.writeValueAsString(dto);
            userCachePort.set(cacheKey, jsonValue, CACHE_TTL_HOURS);
        } catch (JsonProcessingException e) {
            log.warn("캐시 직렬화 실패 userId={}", userId, e);
        }
    }
}
