package org.duo.duo.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ADMIN("ROLE_ADMIN", "관리자"),
    USER("ROLE_USER", "일반 사용자"),
    ANONYMOUS("ROLE_ANONYMOUS", "익명 사용자");

    private final String key;
    private final String description;
}