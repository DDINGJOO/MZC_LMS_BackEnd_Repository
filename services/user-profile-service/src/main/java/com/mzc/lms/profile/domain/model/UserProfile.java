package com.mzc.lms.profile.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    private Long userId;
    private String name;
    private String email;
    private String userType;
    private String profileImageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public boolean isStudent() {
        return "STUDENT".equals(userType);
    }

    public boolean isProfessor() {
        return "PROFESSOR".equals(userType);
    }

    public boolean isAdmin() {
        return "ADMIN".equals(userType);
    }

    public void updateName(String newName) {
        this.name = newName;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateProfileImage(String imageUrl) {
        this.profileImageUrl = imageUrl;
        this.updatedAt = LocalDateTime.now();
    }
}
