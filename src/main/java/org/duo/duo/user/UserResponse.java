package org.duo.duo.user;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserResponse {
    private Long userId;
    private String username;
    private String name;
    private String bio;
    private String profileImage;
    private String riotId;
    private String riotTag;
    private Role role;
    private LocalDateTime createdAt;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .name(user.getName())
                .bio(user.getBio())
                .profileImage(user.getProfileImage())
                .riotId(user.getRiotId())
                .riotTag(user.getRiotTag())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }
}