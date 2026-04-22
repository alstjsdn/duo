package org.duo.duo.notification;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {

    COMMENT("댓글"),
    REPLY("답글"),
    JOIN_REQUEST("참가 요청"),
    JOIN_APPROVED("참가 승인"),
    JOIN_REJECTED("참가 거절"),
    JOIN_KICKED("파티 추방");

    private final String name;
}