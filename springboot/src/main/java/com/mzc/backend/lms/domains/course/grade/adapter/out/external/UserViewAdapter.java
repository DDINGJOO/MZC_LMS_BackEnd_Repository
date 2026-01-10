package com.mzc.backend.lms.domains.course.grade.adapter.out.external;

import com.mzc.backend.lms.domains.course.grade.application.port.out.UserViewPort;
import com.mzc.backend.lms.views.UserViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 사용자 정보 외부 Adapter - grade 전용
 */
@Component
@RequiredArgsConstructor
public class UserViewAdapter implements UserViewPort {

    private final UserViewService userViewService;

    @Override
    public Map<Long, String> getUserNames(List<Long> userIds) {
        Map<Long, String> result = new HashMap<>();
        if (userIds == null || userIds.isEmpty()) {
            return result;
        }

        Map<String, String> raw = userViewService.getUserNames(
                userIds.stream().map(String::valueOf).toList()
        );

        for (Map.Entry<String, String> entry : raw.entrySet()) {
            try {
                result.put(Long.parseLong(entry.getKey()), entry.getValue());
            } catch (NumberFormatException ignore) {
                // skip
            }
        }

        return result;
    }
}
