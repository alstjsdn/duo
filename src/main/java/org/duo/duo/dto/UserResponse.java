package org.duo.duo.dto;

import lombok.Builder;
import lombok.Getter;
import org.duo.duo.entity.User;
import org.duo.duo.entity.enums.Role;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserResponse {
    private Long userId;
    private String username;
    private String name;
    private Role role;
    private LocalDateTime createdAt;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .name(user.getName())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }
}